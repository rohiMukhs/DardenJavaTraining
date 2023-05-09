package com.darden.dash.capacity.boh.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.model.CapacitySlotTypeRefModel;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.CreateTemplateResponse;
import com.darden.dash.common.error.ApplicationErrors;
import com.fasterxml.jackson.core.JsonProcessingException;
/**
 * 
 * @author vlsowjan
 *
 * 
 *		 Service Implementation class which holds method definitions which deals
 *       with capacity template activities or any business logic related to RestaurantCapacityTemplate
 *       
 */

public interface RestaurantCapacityTemplateService {
	
	/**This method is used for get All capacity Template records.
	 * 
	 * @return ResponseEntity response containing all list of 
	 * 							capacity Template.
	 */

	CapacityResponse getAllCapacityTemplates(String conceptId);

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
	 * @param templateId Id of capacity template to be 
	 * 							deleted.
	 * 
	 * @param deleteConfirmed Delete confirm flag confirming the delete process.
	 * 
	 * @param userDetail information of user operating on the create action.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	String deleteByTemplateId(String templateId, String deleteConfirmed, String userDetail)
			throws JsonProcessingException;
	
	/**
	 * This method is validate if the CapacityTemplate is assigned to the CapacityTemplate Model 
	 * in the database it checks if there is any templateId is present in capacityModelAndCapacityTemplate
	 * table for the validation.
	 * 
	 * @param templateId Template Id of Capacity template to be validated 
	 * 						in database.
	 * 
	 * @param applicationErrors error class to raise error if validation
	 * 						fails.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateCapacityTemplateId(String templateId, ApplicationErrors applicationErrors);
	
	/**
	 * This method is to validate the business dates in the database whether the given business dates
	 * exists or not for other templates in capacity template model 
	 * 
	 * @param createCapacityTemplateRequest Request class containing the detail of
	 * 								Capacity Template to be created.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	
	public boolean validateCapacityModelBusinessDates(CreateCapacityTemplateRequest createCapacityTemplateRequest, String templateId);

	
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
	 * @throws JsonProcessingException 
	 */
	CreateTemplateResponse updateCapacityTemplate(@Valid CreateCapacityTemplateRequest createCapacityTemplateRequest,String accessToken,BigInteger templateId) throws JsonProcessingException;
	
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
	
	/**
	 * This method is to fetch all model list based on the template id list passed
	 * in the parameter.
	 * 
	 * @param templateIds Set of bigInteger containing the value of template id.
	 * 
	 * @return List<CapacityModel> list of model containing the value of capacity model.
	 */
	public List<CapacityModel> getAllModelsRelatingToTemplateIdList(Set<BigInteger> templateIds);
	
	/**
	 * Method is used to get the CapacityTemplate buy id for the respective
	 * CapacityTemplate Info id.Used Builder Design pattern to create and set
	 * the CapacityTemplate object.**
	 *
	 * @param capacityTemplateId, Id of capacityTemplate
	 * @return capacityTemplate {@code capacityTemplate}
	 */

	CapacityTemplate getCapacityTemplateById(BigInteger capacityTemplateId);
	
	/**
	 * This method is to get list of Capacity Templates from database along with
	 * list of channel data from CapacityTemplate,
	 * CapacityTemplateAndCapacityChannel , CapacitySlot ,CapacityChannel entities
	 * and mapping the capacity channel mapper for reference data based on
	 * specified date.
	 * 
	 * @param isRefDataReq conatins flag for reference data.
	 * 
	 * @param date contains data for which date template should be displayed.
	 * 
	 * @param conceptId contains data of concept id.
	 * 
	 * @return ResponseEntity response containing all list of 
	 * 							capacity Template.
	 * 
	 */
	public CapacityResponse getAllCapacityTemplatesBasedOnDate(Boolean isRefDataReq, String conceptId, String date);
	
	/**
	 * This method is fetch all capacity slot type data.
	 * 
	 * @return List<CapacitySlotTypeRefModel> contains all 
	 * 			the data related to capacity slot type.
	 */
	public List<CapacitySlotTypeRefModel> getAllCapacitySlotTypes();
}
