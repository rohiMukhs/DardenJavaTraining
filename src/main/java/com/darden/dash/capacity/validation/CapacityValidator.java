package com.darden.dash.capacity.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.DeleteCapacityTemplateRequest;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.handler.DashValidator;
import com.darden.dash.common.util.ValidatorUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;

/**
* @author vraviran
*
* This class {@link CapacityValidator} contains the methods
* used for validating the data which we get from Request Body and
* Parameters for Create, Update and Delete Api's of CapacityTemplate.
*
*/
@Component
public class CapacityValidator implements DashValidator {

	private CapacityManagementService capacityManagementService;

	@Autowired
	public CapacityValidator(CapacityManagementService capacityManagementService) {
		super();
		this.capacityManagementService = capacityManagementService;
	}
	
	/**
	* 
	* Method is used to validate the CreateCapacityTemplate data and throws exception based on the validation
	*
	* @param object
	* @param operation
	* @param parameters
	* @throws JsonProcessingException
	* 
	* @return ResponseEntity<Object>
	*/
	@Override
	public void validate(Object object, String operation, String... parameters) throws JsonProcessingException {
		
		/**
		 * This validation method is used to validate if the values of
		 * concept id and correlation id have been passed in header 
		 * if not application error is raised.
		 */
		ApplicationErrors applicationErrors = new ApplicationErrors();
		ValidatorUtils.validateCorrelationAndConceptModel(applicationErrors);
		
		/**
		 * This validation method is used for the database validation of the
		 * parameter passed stored in a model class which is used for the UPDATE 
		 * operation. Based on the requirement of validation on specific field is 
		 * checked if validation fails application error is raised.
		 */
		if (OperationConstants.OPERATION_DELETE.getCode().equals(operation)) {
			DeleteCapacityTemplateRequest deleteCapacityTemplateRequest = buildDeleteObject(object);
			validateInDbForDelete(buildDeleteObject(object), applicationErrors);
			applicationErrors.isValidObject(deleteCapacityTemplateRequest);
			applicationErrors.raiseExceptionIfHasErrors();
			return;
		}
		
		/**
		 * This validation method is used to validate if the values passed in 
		 * request body are not null, not blank ,no duplicate values for specific field ,
		 * min and max value for specific fields ,alphanumeric for specific fields and 
		 * so on if any of the validation fails certain application error is raised 
		 * with respect to the failed validation
		 */
		CreateCapacityTemplateRequest createCapacityTemplateRequest = buildObject(object);
		if (!applicationErrors.isValidObject(createCapacityTemplateRequest)) {
			applicationErrors.raiseExceptionIfHasErrors();
		}
		
		/**
		 * This validation method is used for the database validation of the
		 * request body which is used for the CREATE operation. Based on the 
		 * requirement of validation on specific field is checked if validation 
		 * fails application error is raised.
		 */
		if (OperationConstants.OPERATION_CREATE.getCode().equals(operation)) {
			validateInDbForCreate(buildObject(object), applicationErrors);
			applicationErrors.raiseExceptionIfHasErrors();
		}
	}

	/**
	 * This validation method is used to validate if the CapacityTemplate is 
	 * assigned to capacityTemplate mode,based on the templateId which is passed
	 * in parameter to perform delete operation
	 * 
	 * @param buildObject
	 * @param applicationErrors
	 */
	private void validateInDbForDelete(DeleteCapacityTemplateRequest buildObject, ApplicationErrors applicationErrors) {
		if(capacityManagementService.validateCapacityTemplateId(buildObject.getTemplateId())) {
			applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4501));
		}
	}

/**
 * This method validates capacity template name if already present in database
 * 
 * @param createCapacityTemplateRequest
 * @param applicationErrors
 */
	private void validateInDbForCreate(CreateCapacityTemplateRequest createCapacityTemplateRequest,
			ApplicationErrors applicationErrors) {
		if (capacityManagementService.validateCapacityTemplateNm(createCapacityTemplateRequest.getCapacityTemplateName())) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4009),
					CapacityConstants.CAPACITY_TEMPLATE_NM);
		}
	}
/**
 * This method is for validating the create request
 * 
 * @param object
 * @return CreateCapacityTemplateRequest
 * @throws JsonProcessingException
 */
	private CreateCapacityTemplateRequest buildObject(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		String json = mapper.writeValueAsString(object);
		Gson gson = new Gson();
		return gson.fromJson(json, CreateCapacityTemplateRequest.class);

	}
	
	/**
	 * This method is for validating the delete request
	 * 
	 * @param object
	 * @return DeleteCapacityTemplateRequest
	 * @throws JsonProcessingException
	 */

	private DeleteCapacityTemplateRequest buildDeleteObject(Object object) throws JsonProcessingException  {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		String json = mapper.writeValueAsString(object);
		Gson gson = new Gson();
		return gson.fromJson(json, DeleteCapacityTemplateRequest.class);
	}

}
