package com.digifood.controller;


import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.MediaSize.Other;

import org.apache.catalina.LifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.digifood.DTO.AuthRequest;
import com.digifood.DTO.AuthenticationResponse;
import com.digifood.DTO.RestPasswordRequest;
import com.digifood.exception.BadRequestException;
import com.digifood.exception.InvalidTokenException;
import com.digifood.exception.ResourceNotFoundException;
import com.digifood.exception.UserExcistException;
import com.digifood.model.Dish;
import com.digifood.model.Notification;
import com.digifood.model.RestaurantTable;
import com.digifood.model.TableOrder;
import com.digifood.model.User;
import com.digifood.service.DishService;
import com.digifood.service.NotificationService;
import com.digifood.service.OrderService;
import com.digifood.service.TableService;
import com.digifood.service.UserService;
import com.digifood.util.JwtUtil;

@RestController
public class HomeController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private DishService dishService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private TableService tableService;
	
	@Autowired 
	private NotificationService notificationService;
	
	
	@PreAuthorize("hasRole('ROLE_admin')")
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	
	@PostMapping("/authenticate") //error 403
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest ) throws Exception{
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
			);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect user name or password", e);
		}
		User user=userService.getUserByUsername(authenticationRequest.getUserName());
		final String token= jwtUtil.generateToken(authenticationRequest.getUserName());
		
		return ResponseEntity.ok(new AuthenticationResponse(user.getUsername(),user.getRole(),token));
	}
	
	//user related APIs
	//create user or signup
	@PostMapping("/signup/{role}") //role should be -> ROLE_customer , ROLE_waiter, ROLE_cashier, ROLE_waiter
	public ResponseEntity<?> signup(@RequestBody User user, @PathVariable(value = "role") String role) throws UserExcistException {
		if(role.equals("ROLE_admin")) {
			return new ResponseEntity<String>("Admin role can't accepted",HttpStatus.UNAUTHORIZED);
		}else {
			return ResponseEntity.ok(userService.createUser(user,role));
		}
		
	}
	
	@PostMapping("/password/code/{username}") //error 404
	public ResponseEntity<?> createRestToken(@PathVariable(value="username") String username) throws ResourceNotFoundException{
		userService.resetToken(username);
		return ResponseEntity.ok("");
	}
	
	@PostMapping("/password/reset")
	public ResponseEntity<?> resetPassword(@RequestBody RestPasswordRequest restPasswordRequest) throws ResourceNotFoundException, BadRequestException, InvalidTokenException{
		
		return ResponseEntity.ok(userService.resetPassword(restPasswordRequest));
	}
	
	@GetMapping("/users/{role}")
	public List<User> getUserByRole(@PathVariable(value = "role") String role){
		List<User>  users=userService.getUsersByRole(role);
		users.forEach(u->u.setPassword(null));
		return users;
	}
	
	@PostMapping("/uploadImage")
    public void uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value="dishId",required = false) Long dishId,@RequestParam(value="username",required = false) String username,@RequestParam("category") String category) throws ResourceNotFoundException, BadRequestException {
        if(category.equals("user") && username!=null) {
        	userService.storeFile(file,username);
        }else if (category.equals("dish") && dishId!=null) {
        	dishService.storeFile(file,dishId);
		}
        else {
        	throw new BadRequestException("Category is not valid");
        }

    }

    @GetMapping("/downloadFile/user/{username}")
    public ResponseEntity<Resource> downloadUserFile(@PathVariable String username) throws ResourceNotFoundException {
        // Load file from database
        User user= userService.getUserByUsername(username);
        if(user.getFile()==null) {
        	throw new ResourceNotFoundException("Image not available");
        }else {
        	
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(user.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + user.getFileName() + "\"")
                .body(new ByteArrayResource(user.getFile()));
        }
     }
    
    
//---------------------------------------------------------------------------------------------------------------------------
//  Table APIs
//---------------------------------------------------------------------------------------------------------------------------
    @PostMapping("/table")
	@PreAuthorize("hasRole('ROLE_admin')")
	public RestaurantTable createTable(@RequestBody RestaurantTable restaurantTable) {
		return tableService.addTable(restaurantTable);
	}
    
    @GetMapping("/tables")
    public List<RestaurantTable> getAllTables(){
    	return tableService.getAll();
    }
    
    @GetMapping("/table/{id}")
    public RestaurantTable getTableById(@PathVariable("id") Long id)throws ResourceNotFoundException{
    	return tableService.getById(id);
    }
    
    @PutMapping("/table/{id}")
    @PreAuthorize("hasRole('ROLE_admin')")
    public RestaurantTable updateTable(@PathVariable("id") Long id,@RequestBody RestaurantTable restaurantTable) throws ResourceNotFoundException {
    	return tableService.updateTable(id,restaurantTable);
    }
    
    
//---------------------------------------------------------------------------------------------------------------------------
//    Dish APIs
//---------------------------------------------------------------------------------------------------------------------------

	
	@PostMapping("/dish")
//	@PreAuthorize("hasRole('ROLE_cook')")§
	public Dish createDish(@RequestBody Dish dish) {
		return dishService.addDish(dish);
	}
	
	@GetMapping("/downloadFile/dish/{dishId}")
    public ResponseEntity<Resource> downloadDishFile(@PathVariable("dishId") Long dishId) throws ResourceNotFoundException {
        // Load file from database
        Dish dish=dishService.getByDishId(dishId);
        if(dish.getFileName()==null) {
        	throw new ResourceNotFoundException("Image not available");
        }else {
        	return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(dish.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dish.getFileName() + "\"")
                    .body(new ByteArrayResource(dish.getFile()));
        }
        
    }
	
	@PatchMapping("/dish/{dishId}")
	public ResponseEntity<?> updateDish(@PathVariable("dishId") Long dishId, @RequestBody Map<Object, Object> feilds) throws ResourceNotFoundException{
		return ResponseEntity.ok(dishService.updateDish(dishId,feilds));
	}
	 
	
	@GetMapping("/dish/{dishId}")
	public Dish getDishById(@PathVariable("dishId") Long dishId) throws ResourceNotFoundException {
		return dishService.getDishById(dishId);
	}
	
	@GetMapping("/dishes")
	public List<Dish> getAllDishes(){
		return dishService.getAllDishes();
	}
	
//	@DeleteMapping("/dish/{dishId}")
//	public void deleteDishById(@PathVariable("dishId") Long dishId) throws ResourceNotFoundException {
//		 dishService.deleteDishById(dishId);
//	}
	
//---------------------------------------------------------------------------------------------------------------------------
//  Order APIs
//---------------------------------------------------------------------------------------------------------------------------
	
	@PostMapping("/order")
	public ResponseEntity<?> createOrder(@RequestBody TableOrder order, Principal principal) throws ResourceNotFoundException{
		return ResponseEntity.ok(orderService.createOrder(order,principal.getName()));
	}
	
	@GetMapping("/orders")
	public List<TableOrder> getAllOrders(){
		return orderService.getAllOrders();
	}
	
	@GetMapping("/order/{orderId}")
	public TableOrder getOrder(@PathVariable("orderId") Long orderId) throws ResourceNotFoundException{
		return orderService.getOrderById(orderId);
	}
	
	@PatchMapping("/order/{orderId}") //other than status & 
	public ResponseEntity<?> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody Map<Object, Object> feilds,Principal principal) throws ResourceNotFoundException, InvalidTokenException{
		return ResponseEntity.ok(orderService.updateOrder(orderId,feilds,principal.getName()));
	}
	
	@PutMapping("/order/waiter")
	public ResponseEntity<?> updateWaiter(@RequestParam(value="orderId") Long orderId,@RequestParam(value="waiterId") Long waiterId) throws ResourceNotFoundException{
		return ResponseEntity.ok(orderService.updateWaiter(orderId,waiterId));
	}
	
//	@DeleteMapping("/order/{orderId}")
//	public void deleteOrderById(@PathVariable("orderId") Long orderId) throws ResourceNotFoundException {
//		orderService.deleteOrderById(orderId);
//	}
	
	
//---------------------------------------------------------------------------------------------------------------------------
//  Notification APIs
//---------------------------------------------------------------------------------------------------------------------------
	@GetMapping("/notification")
	public List<Notification> getAllNotificationByReceiver(Principal principal){
		return notificationService.getByReceiver(principal.getName());
	}
	
}
