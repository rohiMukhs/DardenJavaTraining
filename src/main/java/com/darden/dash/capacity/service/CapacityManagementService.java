package com.darden.dash.capacity.service;

import java.math.BigInteger;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	 * @throws JsonProcessingException
	 */
	CreateTemplateResponse createTemplate(@Valid CreateCapacityTemplateRequest createCapacityTemplateRequest,String accessToken )
			throws JsonProcessingException;
	
	/**
	 * This method is validate existing CapacityTemplate name in the database for the 
	 * validation.
	 * 
	 * @param CapacityTemplateNm
	 * @return boolean
	 */
	public boolean validateCapacityTemplateNm(String capacityTemplateNm);
	
	/**
	 * This method is to delete capacityTemplate value and related CapacitySlots and 
	 * capacityTemplateAndBussinessDate entity value and capacityTemplateAndCapacityTemplate
	 * entity value based on the value of CapacityTemplate Id ,deleted Flag and user deatil
	 * passed in the parameters. 
	 * 
	 * 
	 * @param departmentListId
	 * @param deletedFlag
	 * @param userDetail
	 * @throws JsonProcessingException
	 */
	void deleteByTemplateId(String templateId, String deletedFlag, String userDetail)
			throws JsonProcessingException;
	
	/**
	 * This method is validate if the CapacityTemplate is assigned to the CapacityTemplate Model 
	 * in the database it checks if there is any templateId is present in capacityModelAndCapacityTemplate
	 * table for the validation.
	 * 
	 * @param templateId
	 * @return boolean
	 */
	public boolean validateCapacityTemplateId(String templateId);
	
	/**
	 * This method is to validate the business dates in the database whether the given business dates
	 * exists or not for other templates in capacity template model 
	 * @param createCapacityTemplateReques
	 * @return boolean
	 */
	
	public boolean validateCapacityModelBusinessDates(CreateCapacityTemplateRequest createCapacityTemplateReques);

	
	/**
	 * This method is to update capacity template in the database based on template Id and 
	 * capacity template request
	 * 
	 * @param createCapacityTemplateRequest
	 * @param accessToken
	 * @param templateId
	 * @return
	 */
	CreateTemplateResponse updateCapacityTemplate(@Valid CreateCapacityTemplateRequest createCapacityTemplateRequest,String accessToken,BigInteger templateId);
	
	/**
	* This method is validate existing CapacityTemplate name in the database for the
	* validation based on the value of templateNm and template Id.
	*
	*
	* @param capacityTemplateNm
	* @param templateId
	* @return boolean
	*/
	public boolean validateCapacityTemplateNmForCreate(String capacityTemplateNm, String templateId);
}
