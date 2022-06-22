package com.darden.dash.capacity.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.DeleteCapacityTemplateRequest;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.capacity.util.CapacityManagementUtils;
import com.darden.dash.common.RequestContext;
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
	private CapacityManagementUtils capacityManagementUtils;

	@Autowired
	public CapacityValidator(CapacityManagementService capacityManagementService,CapacityManagementUtils capacityManagementUtils) {
		super();
		this.capacityManagementService = capacityManagementService;
		this.capacityManagementUtils=capacityManagementUtils;
	}
	
	/**
	* 
	* Method is used to validate the CreateCapacityTemplate data and throws exception based on the validation
	*
	* @param object request class to be validated.
	* 
	* @param operation String containing the value of
	* 				action performed.
	* 
	* @param parameters String containing the values of 
	* 						parameters
	* 
	* @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	* 
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
		capacityManagementUtils.validateConceptId(RequestContext.getConcept(), applicationErrors);
		
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
		
		/**
		 * This validation method is used for the database validation of the
		 * request body which is used for the UPDATE operation. Based on the 
		 * requirement of validation on specific field is checked if validation 
		 * fails application error is raised.
		 */
		if (OperationConstants.OPERATION_UPDATE.getCode().equals(operation)) {
			validateInDbForUpdate(buildObject(object), applicationErrors,parameters);
			applicationErrors.raiseExceptionIfHasErrors();
		}
	}

	/**
	 * This method validates capacity template name if already present in database
	 * and to validate if the date to be edited are same as other template belonging
	 * to same Capacity template Model.
	 *
	 * @param createCapacityTemplateRequest request class containing the value of
	 * 					capacity template to be updated is validated.
	 * 
	 * @param applicationErrors error class is used to raise application errors 
	 * 					for invalid condition.
	 * 
	 * @param parameters String containing the values of 
	* 						parameters 
	 */

	private void validateInDbForUpdate(CreateCapacityTemplateRequest createCapacityTemplateRequest,
			ApplicationErrors applicationErrors,String[] parameters) {
		String templateId=parameters[0];
		if (capacityManagementService.validateCapacityTemplateNmForCreate(createCapacityTemplateRequest.getCapacityTemplateName(),templateId)) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4009),
					CapacityConstants.CAPACITY_TEMPLATE_NM);
		}
		if(capacityManagementService.validateCapacityModelBusinessDates(createCapacityTemplateRequest)) {
			applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4502));
		}
	}
	/**
	 * This validation method is used to validate if the CapacityTemplate is 
	 * assigned to capacityTemplate model,based on the templateId which is passed
	 * in parameter to perform delete operation
	 * 
	 * @param buildObject request class containing the value of capacity template
	 * 					to be deleted is validated.
	 * 
	 * @param applicationErrors error class is used to raise application errors 
	 * 					for invalid condition.
	 */
	private void validateInDbForDelete(DeleteCapacityTemplateRequest buildObject, ApplicationErrors applicationErrors) {
		capacityManagementService.validateCapacityTemplateId(buildObject.getTemplateId(), applicationErrors);
	}

	/**
	 * This method validates capacity template name if already present in database
	 * 
	 * @param createCapacityTemplateRequest request class containing the value of
	 * 					capacity template to be created is validated.
	 * 
	 * @param applicationErrors error class is used to raise application errors 
	 * 					for invalid condition.
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
	 * @param object Class Object is the root of the class hierarchy.
	 * 
	 * @return CreateCapacityTemplateRequest returns request class with
	 * 					after converting it to json and  deserialized into 
	 * 					the model class. 
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
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
	 * @param object Class Object is the root of the class hierarchy.
	 * 
	 * @return DeleteCapacityTemplateRequest returns request class with
	 * 					after converting it to json and  deserialized into 
	 * 					the model class. 
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */

	private DeleteCapacityTemplateRequest buildDeleteObject(Object object) throws JsonProcessingException  {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		String json = mapper.writeValueAsString(object);
		Gson gson = new Gson();
		return gson.fromJson(json, DeleteCapacityTemplateRequest.class);
	}

}
