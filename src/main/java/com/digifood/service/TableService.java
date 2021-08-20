package com.digifood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digifood.exception.ResourceNotFoundException;
import com.digifood.model.Dish;
import com.digifood.model.RestaurantTable;
import com.digifood.repository.TableRepository;

@Service
public class TableService {

	@Autowired
	private TableRepository tableRepository;

	public RestaurantTable addTable(RestaurantTable restaurantTable) {
		
		return tableRepository.save(restaurantTable);
	}

	public List<RestaurantTable> getAll() {
		
		return tableRepository.findAll();
	}

	public RestaurantTable getById(Long id) throws ResourceNotFoundException {
		
		return tableRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("oder not found"));
	}

	public RestaurantTable updateTable(Long id, RestaurantTable restaurantTable) throws ResourceNotFoundException {
		RestaurantTable tablenew=tableRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("oder not found"));
		
		restaurantTable.setId(id);
		return tableRepository.save(restaurantTable);
	}
	
	//.orElseThrow(()-> new ResourceNotFoundException("oder not found"));
	
	
}
