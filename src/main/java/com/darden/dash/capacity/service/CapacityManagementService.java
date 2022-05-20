package com.darden.dash.capacity.service;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
/**
 * 
 * @author skashala
 * @date 16-May-2022
 * 
 *		 Service Implementation class which holds method definitions which deals
 *       with capacity template activities or any business logic related to CapacityTemplate
 *       
 */

public interface CapacityManagementService {
	
	/**This method is used for get All capacity Template records.
	 * 
	 * @return ResponseEntity
	 */

	ResponseEntity<Object> getAllCapacityTemplates();

	/**
	 * This method to create Capacity Template, CapacityTemplateAndBusinessDates,CapacityTemplateAndCapacityChannel,
	 * CapacitySlots.
	 * 
	 * @param createCapacityTemplateRequest
	 * @param accessToken
	 * @return CreateTemplateResponse
	 */
	CreateTemplateResponse createTemplate(@Valid CreateCapacityTemplateRequest createCapacityTemplateRequest,String accessToken );
	
	/**
	 * This method is validate existing CapacityTemplate name in the database for the 
	 * validation.
	 * 
	 * @param CapacityTemplateNm
	 * @return
	 */
	public boolean validateCapacityTemplateNm(String capacityTemplateNm);
}
