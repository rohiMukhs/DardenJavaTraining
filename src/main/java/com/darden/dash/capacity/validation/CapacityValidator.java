package com.darden.dash.capacity.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
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

	private CapacityManagementService templateService;

	@Autowired
	public CapacityValidator(CapacityManagementService templateService) {
		super();
		this.templateService = templateService;
	}
	
	/**
	* Method is used to validate the CreateCapacityTemplate data
	*
	* @return ResponseEntity<Object>
	*/
	@Override
	public void validate(Object object, String operation, String... parameters) throws JsonProcessingException {
		ApplicationErrors applicationErrors = new ApplicationErrors();
		ValidatorUtils.validateCorrelationAndConceptModel(applicationErrors);
		CreateCapacityTemplateRequest createCapacityTemplateRequest = buildObject(object);
		if (!applicationErrors.isValidObject(createCapacityTemplateRequest)) {
			applicationErrors.raiseExceptionIfHasErrors();
		}
		if (OperationConstants.OPERATION_CREATE.getCode().equals(operation)) {
			validateInDbForCreate(buildObject(object), applicationErrors);
			applicationErrors.raiseExceptionIfHasErrors();
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
		if (templateService.validateCapacityTemplateNm(createCapacityTemplateRequest.getCapacityTemplateName())) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4009),
					CapacityConstants.CAPACITY_TEMPLATE_NM);
		}
	}
/**
 * This method is for validating the create request
 * 
 * @param object
 * @return
 * @throws JsonProcessingException
 */
	private CreateCapacityTemplateRequest buildObject(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		String json = mapper.writeValueAsString(object);
		Gson gson = new Gson();
		return gson.fromJson(json, CreateCapacityTemplateRequest.class);

	}

}
