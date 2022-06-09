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
	 * @return ResponseEntity response containing all list of 
	 * 							capacity Template.
	 */

	ResponseEntity<Object> getAllCapacityTemplates();

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
	CreateTemplateResponse createTemplate(@Valid CreateCapacityTemplateRequest createCapacityTemplateRequest,String accessToken )
			throws JsonProcessingException;
	
	/**
	 * This method is validate existing CapacityTemplate name in the database for the 
	 * validation.
	 * 
	 * @param CapacityTemplateNm Capacity Template Name to be validated.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateCapacityTemplateNm(String capacityTemplateNm);
	
	/**
	 * This method is to delete capacityTemplate value and related CapacitySlots and 
	 * capacityTemplateAndBussinessDate entity value and capacityTemplateAndCapacityTemplate
	 * entity value based on the value of CapacityTemplate Id ,deleted Flag and user deatil
	 * passed in the parameters. 
	 * 
	 * 
	 * @param departmentListId Department List Id of department List to be 
	 * 							deleted.
	 * 
	 * @param deletedFlag Deleted flag detail for the department list entity.
	 * 
	 * @param userDetail information of user operating on the create action.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	void deleteByTemplateId(String templateId, String deletedFlag, String userDetail)
			throws JsonProcessingException;
	
	/**
	 * This method is validate if the CapacityTemplate is assigned to the CapacityTemplate Model 
	 * in the database it checks if there is any templateId is present in capacityModelAndCapacityTemplate
	 * table for the validation.
	 * 
	 * @param templateId Template Id of Capacity template to be validated 
	 * 						in database.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateCapacityTemplateId(String templateId);
	
	/**
	 * This method is to validate the business dates in the database whether the given business dates
	 * exists or not for other templates in capacity template model 
	 * 
	 * @param createCapacityTemplateRequest Request class containing the detail of
	 * 								Capacity Template to be created.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	
	public boolean validateCapacityModelBusinessDates(CreateCapacityTemplateRequest createCapacityTemplateReques);

	
	/**
	 * This method is to update capacity template in the database based on template Id and 
	 * capacity template request
	 * 
	 * @param createCapacityTemplateRequest Request class containing the detail of
	 * 								Capacity Template to be updated.
	 * 
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API.
	 *                      
	 * @param templateId Template Id of Capacity template to be updated.
	 * 
	 * @return CreateTemplateResponse response class containing detail of 
	 */
	CreateTemplateResponse updateCapacityTemplate(@Valid CreateCapacityTemplateRequest createCapacityTemplateRequest,String accessToken,BigInteger templateId);
	
	/**
	* This method is validate existing CapacityTemplate name in the database for the
	* validation based on the value of templateNm and template Id.
	*
	*
	* @param capacityTemplateNm capacity Template Name of capacity template to be
	* 							validated.
	* 
	* @param templateId Template Id of capacity template.
	* 
	* @return boolean returns the boolean value based on the condition.
	*/
	public boolean validateCapacityTemplateNmForCreate(String capacityTemplateNm, String templateId);
}
