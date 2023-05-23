package com.darden.dash.capacity.boh.validator;

import org.springframework.stereotype.Component;

import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.util.ValidatorUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class RestaurantCapacityTemplateValidator {

	public void validate(Object object, String operation, String... parameters) throws JsonProcessingException {

		/**
		 * This validation method is used to validate if the values of concept id and
		 * correlation id have been passed in header if not application error is raised.
		 */
		ApplicationErrors applicationErrors = new ApplicationErrors();
		ValidatorUtils.validateCorrelationAndConceptModel(applicationErrors);

		/**
		 * This validation method is used for the validation of the header passed for
		 * the GET operation. Based on the requirement of validation check if validation
		 * fails application error is raised.
		 */
		if (OperationConstants.OPERATION_GET.getCode().equals(operation)) {
			return;
		}

	}

}
