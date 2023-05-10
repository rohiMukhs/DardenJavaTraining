package com.darden.dash.capacity.boh.service;

import javax.validation.Valid;

import com.darden.dash.capacity.boh.model.CreateRestaurantCapacityTemplateRequest;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface RestaurantCapacityTemplateService {
	/**
	 * This method to create Capacity Template, CapacityTemplateAndBusinessDates,CapacityTemplateAndCapacityChannel,
	 * CapacitySlots.
	 * 
	 * @param createCapacityTemplateRequest request class containing detail of
	 * 								Capacity Template to be created.
	 * 
	 * @param accessToken  Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 * 
	 * @return CreateTemplateResponse response containing value of Capacity
	 * 									Template created.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	public CapacityTemplateEntity createTemplate(@Valid CreateRestaurantCapacityTemplateRequest createRestaurantCapacityTemplateRequest,String user )
			throws JsonProcessingException;
	
	/**This method is used for get All capacity Template records.
	 * 
	 * @return ResponseEntity response containing all list of 
	 * 							capacity Template.
	 */

	 public CapacityResponse getAllCapacityTemplates(Boolean assignedTemplate, String conceptId);

}
