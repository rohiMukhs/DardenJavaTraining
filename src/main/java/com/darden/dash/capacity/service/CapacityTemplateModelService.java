package com.darden.dash.capacity.service;

import java.math.BigInteger;
import java.util.List;

import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityTemplateModel;
import com.darden.dash.capacity.model.ConceptForCache;
import com.darden.dash.capacity.model.RestaurantsAssigned;
import com.darden.dash.common.error.ApplicationErrors;

/**
 * 
 * @author vraviran
 * @date 16-jun-2022
 * 
 * 		 Service Implementation class which holds method definitions which deals
 *       with capacity Model activities or any business logic related to CapacityModel.
 *
 */
public interface CapacityTemplateModelService {

	/**
	 * This service method is written for the purpose of retrieving the 
	 * list of capacity model based on concept Id along with the valye of
	 * capacity template and restaurant assigned to the model.
	 * 
	 * @return List<CapacityModel> list of model class containing the value 
	 * of capacity models.
	 */
	public List<CapacityModel> getAllCapacityModels();
	
	/**
	 * This service method is used to create capacity model based on the 
	 * data RestaurantAssigned and TemplateAssigned which is assigned to the capacity Model
	 * provided in capacityModel request and user detail passed in
	 * the parameter of method
	 * 
	 * @param capacityModelRequest request class containing detail of
	 * 								Capacity Model Template to be created.
	 * @param accessToken Token used to authenticate the user and extract the
	 *                      userDetails for this API
	 * @return CapacityTemplateModel 
	 */

	CapacityTemplateModel createCapacityModel(CapacityModelRequest capacityModelRequest, String accessToken);
	/**
	 * This method is to validate capacity model name using provided capacity model name
	 * It returns boolean values based on the condition if the
	 * passing values capacity model name is present in database.
	 * 
	 * @param capacityModelNm Capacity Model Name to be validated.
	 * @return boolean returns the boolean value based on the condition.
	 */
	
	public boolean validateModelTemplateNm(String capacityModelNm);
	/**
	 * This method is to validate capacity model template business dates using provided
	 * business dates in capacity template request 
	 * It returns boolean values based on the condition if the
	 * passing values Business Date is present in database.
	 * 
	 * @param capacityTemplateEntityRequest request class containing detail of
	 * 								Capacity Model Template to be created.
	 * @return boolean returns the boolean value based on the condition.
	 */
	
	public boolean validateCapacityModelTemplateBusinessDates(CapacityTemplateEntity capacityTemplateEntityRequest, List<BigInteger> otherTemplateId);
	
	/**
	 * This service method is used to update capacity model based on the 
	 * data RestaurantAssigned and TemplateAssigned which is assigned to the capacity Model
	 * provided in capacityModel request and user detail passed in
	 * the parameter of method
	 * 
	 * @param modelId string contains the value of capacity model 
	 * 					to be updated.
	 * 
	 * @param capacityModelRequest request class containing detail of
	 * 								Capacity Model Template to be created.
	 * 
	 * @param user String contains the detail of user extracted from the
	 * 								access token.
	 * 
	 * @return CapacityTemplateModel model class containing the value of
	 * 						data of updated capacity template model.
	 */
	public CapacityTemplateModel updateCapacityModel(String modelId, CapacityModelRequest capacityModelRequest, String user);
	
	/**
	 * This service method is written for purpose of validating capacity
	 * template model name in database to avoid duplicates.
	 * 
	 * @param capacityModelNm String contains the value capacity model 
	 * 					name to be validated in database.
	 * 
	 * @param id String contains the value of capacity template model id.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateModelTemplateNmForUpdate(String capacityModelNm, String id);
	
	/**
	 * This service method is written for purpose of validating restaurant 
	 * list to be assigned to check whether any restaurant is being unassigned 
	 * it will not allow previous restaurants to be unassigned.
	 * 
	 * @param restaurantsAssigned list of model class containing the value 
	 * 					of restaurants to be assigned.
	 * 
	 * @param id String contains the value of capacity template model id.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateIfRestaurantIsUnassigned(List<RestaurantsAssigned> restaurantsAssigned, String id);
	
	/**
	 * This service method is to validate if capacity template is already assigned to other
	 * capacity model for update operation.
	 * 
	 * @param capacityModelRequest request class containing the value of template model
	 * 				to be updated.
	 * 
	 * @param applicationErrors error class used to raise exception in case any validation
	 * 				is failed
	 * 
	 * @param id string containing the value of template id to be updated.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateTemplateAssignedforUpdate(CapacityModelRequest capacityModelRequest, ApplicationErrors applicationErrors, String id);
	
	/**
	 * This service method is used to cache concept data from restCall.
	 * 
	 * @return List<ConceptForCache> list of model class containing the value of
	 * 				concept data.
	 */
	public List<ConceptForCache> getCacheConceptData();

}
