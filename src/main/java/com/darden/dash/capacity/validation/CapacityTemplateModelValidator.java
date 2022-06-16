package com.darden.dash.capacity.validation;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
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
 * 
 * @author vraviran
 * @date 16-jun-2022
 * 
 *       This class {@link CapacityTemplateModelValidator} contains the methods
 *       used for validating the data which we get from Request Body and
 *       Parameters for Create, Update and Delete Api's of CapacityModel.
 *
 */
@Component
public class CapacityTemplateModelValidator implements DashValidator {

	private CapacityTemplateModelService capacityTemplateModelService;
	private CapacityTemplateRepo capacityTemplateRepo;

	@Autowired
	public CapacityTemplateModelValidator(CapacityTemplateModelService capacityTemplateModelService, CapacityTemplateRepo capacityTemplateRepo) {
		super();
		this.capacityTemplateModelService = capacityTemplateModelService;
		this.capacityTemplateRepo = capacityTemplateRepo;
	}
	
	@Override
	public void validate(Object object, String operation, String... parameters) throws JsonProcessingException {

		/**
		 * This validation method is used to validate if the values of concept id and
		 * correlation id have been passed in header if not application error is raised.
		 */
		if (OperationConstants.OPERATION_GET.getCode().equals(operation)) {
			ApplicationErrors applicationErrors = new ApplicationErrors();
			ValidatorUtils.validateCorrelationAndConceptModel(applicationErrors);
		}

		/**
		 * This validation method is used to validate if the values passed in request
		 * body are not null, not blank ,min and max value for specific fields
		 * ,alphanumeric for specific fields and so on if any of the validation fails
		 * certain application error is raised with respect to the failed validation.
		 * This validation method is used for the database validation of the request
		 * body which is used for the CREATE operation. Based on the requirement of
		 * validation on specific field is checked if validation fails application error
		 * is raised.
		 * 
		 */
		if (OperationConstants.OPERATION_CREATE.getCode().equals(operation)) {
			/**
			 * This validation method is used to validate if the values of concept id and
			 * correlation id have been passed in header if not application error is raised.
			 */
			ApplicationErrors applicationErrors = new ApplicationErrors();
			ValidatorUtils.validateCorrelationAndConceptModel(applicationErrors);

			CapacityModelRequest capacityModelRequest = buildCreateObject(object);
			if (!applicationErrors.isValidObject(capacityModelRequest)) {
				applicationErrors.raiseExceptionIfHasErrors();
			} else {
				validateInDbForCreate(buildCreateObject(object), applicationErrors);
				applicationErrors.raiseExceptionIfHasErrors();
			}
		}

	}

	/**
	 * This method validates capacity template Model name if already present in
	 * database
	 * 
	 * @param CapacityModelRequest request class containing the value of capacity
	 *                             template model to be created is validated.
	 * 
	 * @param applicationErrors    error class is used to raise application errors
	 *                             for invalid condition.
	 */
	private void validateInDbForCreate(CapacityModelRequest capacityModelRequest, ApplicationErrors applicationErrors) {
		validateAssignedTemplates(capacityModelRequest, applicationErrors);
		if (capacityTemplateModelService.validateModelTemplateNm(capacityModelRequest.getTemplateModelName())) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4009),
					CapacityConstants.CAPACITY_MODEL_NM);
		} else if (validateBusinessDatesAndDays(capacityModelRequest)) {
			applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4502));
		}
	}

	/**
	 * This method validates capacity template model using provided Id if already
	 * present in database
	 * 
	 * @param CapacityModelRequest request class containing the value of capacity
	 *                             template model to be created is validated.
	 * 
	 * @param applicationErrors    error class is used to raise application errors
	 *                             for invalid condition.
	 */
	private void validateAssignedTemplates(CapacityModelRequest capacityModelRequest,
			ApplicationErrors applicationErrors) {
		capacityModelRequest.getTemplatesAssigned().stream().filter(Objects::nonNull).forEach(t -> {
			Optional<CapacityTemplateEntity> dbTemplate = capacityTemplateRepo
					.findById(new BigInteger(t.getTemplateId()));
			if (dbTemplate.isEmpty()) {
				applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4503), t.getTemplateId());
			}
		});
	}

	/**
	 * This method validates BusinessDatesAndDays if already present in database
	 * 
	 * @param CapacityModelRequest request class containing the value of capacity
	 *                             template model to be created is validated.
	 * @return boolean returns the boolean value based on the condition.
	 */
	private boolean validateBusinessDatesAndDays(CapacityModelRequest capacityModelRequest) {
		return capacityModelRequest.getTemplatesAssigned().stream().filter(Objects::nonNull).anyMatch(t -> {
			Optional<CapacityTemplateEntity> dbTemplate = capacityTemplateRepo
					.findById(new BigInteger(t.getTemplateId()));
			return dbTemplate.isPresent()
					&& capacityTemplateModelService.validateCapacityModelTemplateBusinessDates(dbTemplate.get());
		});
	}

	/**
	 * This method is for validating the create request
	 * 
	 * @param object Class Object is the root of the class hierarchy.
	 * 
	 * @return CreateCapacityTemplateModelRequest returns request class with after
	 *         converting it to json and deserialized into the model class.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	private CapacityModelRequest buildCreateObject(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		String json = mapper.writeValueAsString(object);
		Gson gson = new Gson();
		return gson.fromJson(json, CapacityModelRequest.class);
	}

}
