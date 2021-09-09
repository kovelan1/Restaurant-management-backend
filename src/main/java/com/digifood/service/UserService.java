package com.digifood.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.digifood.DTO.RestPasswordRequest;
import com.digifood.exception.BadRequestException;
import com.digifood.exception.FileStorageException;
import com.digifood.exception.InvalidTokenException;
import com.digifood.exception.ResourceNotFoundException;
import com.digifood.exception.UserExcistException;
import com.digifood.model.Cashier;
import com.digifood.model.Cook;
import com.digifood.model.Customer;
import com.digifood.model.Dish;
import com.digifood.model.RestaurantTable;
import com.digifood.model.User;
import com.digifood.model.VerificationToken;
import com.digifood.model.Waiter;
import com.digifood.repository.CashierRepository;
import com.digifood.repository.CookRepository;
import com.digifood.repository.CustomerRepository;
import com.digifood.repository.TableRepository;
import com.digifood.repository.UserRepository;
import com.digifood.repository.VerificationTokenRepository;
import com.digifood.repository.WaiterRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CookRepository cookRepository;
	
	@Autowired
	private WaiterRepository waiterRepository;
	
	@Autowired
	private CashierRepository cashierRepository;
	
	@Autowired
	private TableRepository tableRepository;
	
	@Autowired
    private VerificationTokenRepository tokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private MessageSource messages;
 
    @Autowired
    private JavaMailSender mailSender;
    

	public User createUser(User user, String role) throws UserExcistException {

		if(userRepository.existsById(user.getUsername())) {
			throw new UserExcistException(user.getUsername()+" already excist");
		}else {
			String password=user.getPassword();
			String encodePass=passwordEncoder.encode(password);
			user.setPassword(encodePass);
			user.setEnabled(true);
			
			
			switch (user.getRole()) {
			case "ROLE_cashier":
				Cashier cashier=user.getCashier();
				cashier.setUser(user);
				break;
			case "ROLE_waiter":
				Waiter waiter=user.getWaiter();
				List<RestaurantTable> tables=waiter.getTables();
				tables.forEach(t->t.setWaiter(waiter));
				waiter.setUser(user);
				break;
			case "ROLE_cook":
				Cook cook=user.getCook();
				cook.setUser(user);
				break;
			case "ROLE_customer":
				Customer customer=user.getCustomer();
				customer.setUser(user);
				break;
			default:
				break;
			}
			User userRes=userRepository.save(user);
			
			if(user.getRole().equals("ROLE_waiter")) {
				Waiter waiter=userRes.getWaiter();
				List<RestaurantTable> tables=waiter.getTables();
				tables.forEach(t->{
					t.setWaiter(waiter);
					tableRepository.save(t);
				});
			}
			return userRes;
		}
		
	}

	public User getUserByUsername(String userName) {
		return userRepository.getById(userName);
	}

	public void resetToken(String username) throws ResourceNotFoundException {
		
		if(!userRepository.existsById(username)) {
			throw new ResourceNotFoundException(username+" not excist");
		}else {
			int token=gen();
			createVerificationToken(userRepository.getById(username),token);
			String subject = "Reset Password";
	       // String message = messages.getMessage("message.regSucc", null, /*event.getLocale()*/null);
	        
	        SimpleMailMessage email = new SimpleMailMessage();
	        email.setTo(username);
	        email.setSubject(subject);
	        
	        email.setText("\r\n" + "Your Password Rest Code is" + token);
	        mailSender.send(email);
		}
	}
	
	private int gen() {
	    Random r = new Random( System.currentTimeMillis() );
	    return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
	}
	
	public void createVerificationToken(User user, int token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

	@Transactional
	public User resetPassword(RestPasswordRequest restPasswordRequest) throws ResourceNotFoundException, BadRequestException, InvalidTokenException {
		if(restPasswordRequest.getToken() == 0) {
			throw new ResourceNotFoundException("Reset code not found");
		}
		else if (!userRepository.existsById(restPasswordRequest.getUserName())) {
			throw new ResourceNotFoundException(restPasswordRequest.getUserName()+" not excist");
		}
		else {
			VerificationToken verificationToken=getVerificationToken(restPasswordRequest.getToken());
			
			 Calendar cal = Calendar.getInstance();
			 
			 if (verificationToken == null) {
				 throw new InvalidTokenException("Invalid Reset Code!");
			 }
			 
			 else if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			    	throw new BadRequestException("Reset code expired");
			 }
			 else {
				 User user=userRepository.findByUsername(restPasswordRequest.getUserName());
				 
				 String password=restPasswordRequest.getPassword();
				 String encodePass=passwordEncoder.encode(password);
				 user.setPassword(encodePass);
				 tokenRepository.delete(verificationToken);
				 
				 return userRepository.save(user);
			 }
		}
		
	}
	
	public VerificationToken getVerificationToken(int VerificationToken) {
		
			return tokenRepository.findByToken(VerificationToken);
		
    }

	
	public User storeFile(MultipartFile file,String username) throws ResourceNotFoundException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            User user=userRepository.findById(username).orElseThrow(()-> new ResourceNotFoundException("User is not available"));
            user.setFileType(file.getContentType());
            user.setFileName(fileName);
            user.setFile(file.getBytes());
            
            return userRepository.save(user);
            
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

	public List<User> getUsersByRole(String role) {
		List<User> users=userRepository.findByRole(role);
		
		return users;
	}

	public User getSigninUser(String name) {
		return userRepository.findByUsername(name);
		
	}
	
	
	
}
