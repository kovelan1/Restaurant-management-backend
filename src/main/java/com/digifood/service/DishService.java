package com.digifood.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.digifood.exception.FileStorageException;
import com.digifood.exception.ResourceNotFoundException;
import com.digifood.model.Dish;
import com.digifood.model.TableOrder;
import com.digifood.repository.DishRepository;
import com.digifood.repository.OrderRepository;

@Service
public class DishService {

	@Autowired
	private DishRepository dishRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	

	public Dish addDish(Dish dish) {
		
		return dishRepository.save(dish);
	}

	public Dish storeFile(MultipartFile file,long dishId) throws ResourceNotFoundException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Dish dish=dishRepository.findById(dishId).orElseThrow(()-> new ResourceNotFoundException("Dish is not available"));
            dish.setFileType(file.getContentType());
            dish.setFileName(fileName);
            dish.setFile(file.getBytes());
            
            return dishRepository.save(dish);
            
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

	public Dish getByDishId(long dishId) throws ResourceNotFoundException{
		// TODO Auto-generated method stub
		return dishRepository.findById(dishId).orElseThrow(()-> new ResourceNotFoundException("Dish is not available"));
	}

	public Object updateDish(Long dishId, Map<Object, Object> feilds) throws ResourceNotFoundException {
		Dish dish=dishRepository.findById(dishId).orElseThrow(()-> new ResourceNotFoundException("Dish is not available"));
		
		feilds.forEach((key,value)->{
			 Field field = ReflectionUtils.findField(Dish.class, (String)key);
			field.setAccessible(true);
			ReflectionUtils.setField(field, dish, value);
		});
		return dishRepository.save(dish);
	}

	public Dish getDishById(Long dishId) throws ResourceNotFoundException{
		
		return dishRepository.findById(dishId).orElseThrow(()-> new ResourceNotFoundException("Dish is not available"));
	}

	public List<Dish> getAllDishes() {
		
		return dishRepository.findAll();
	}

	public void deleteDishById(Long dishId) throws ResourceNotFoundException {
		Dish dish=dishRepository.findById(dishId).orElseThrow(()-> new ResourceNotFoundException("Dish is not available"));
		
//		List<TableOrder> orders=dish.getOrders();
//		
//		orders.forEach(order->{
//			List<Dish> dishesDishs=order.getDishes();
//			dishesDishs.remove(dish);
//			order.setDishes(dishesDishs);
//			orderRepository.save(order);
//		});
//		
		
		dishRepository.deleteById(dishId);
	}
}
