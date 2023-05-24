package com.darden.dash.capacity.boh.service;

import java.math.BigInteger;
import java.util.List;

import com.darden.dash.capacity.boh.model.RestaurantCapacityTemplate;
import com.darden.dash.capacity.boh.model.ViewRestaurantCapacityTemplate;

public interface RestaurantCapacityTemplateService {

	/**
	 * This method is used for get All capacity Template records.
	 * 
	 * @return ResponseEntity response containing all list of capacity Template.
	 */

	public List<ViewRestaurantCapacityTemplate> getAllCapacityTemplates(BigInteger conceptId);

	/**
	 * This method is used to get restaurant template by restaurantTemplateId
	 * 
	 * @param bigTemplateId
	 * @return restaurantTemplate
	 */
	public RestaurantCapacityTemplate getRestaurantCapacityTempalteById(BigInteger bigTemplateId);

}
