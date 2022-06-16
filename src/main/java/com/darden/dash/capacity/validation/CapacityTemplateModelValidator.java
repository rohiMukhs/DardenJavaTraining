package com.darden.dash.capacity.validation;

import org.springframework.stereotype.Component;

import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.handler.DashValidator;
import com.darden.dash.common.util.ValidatorUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * @author vraviran
 * @date 16-jun-2022
 * 
 * This class {@link CapacityTemplateModelValidator} contains the methods
*  used for validating the data which we get from Request Body and
*  Parameters for Create, Update and Delete Api's of CapacityModel.
 *
 */
@Component
public class CapacityTemplateModelValidator implements DashValidator{

	@Override
	public void validate(Object object, String operation, String... parameters) throws JsonProcessingException {
		
		/**
		 * This validation method is used to validate if the values of
		 * concept id and correlation id have been passed in header 
		 * if not application error is raised.
		 */
		if (OperationConstants.OPERATION_GET.getCode().equals(operation)) {
			ApplicationErrors applicationErrors = new ApplicationErrors();
			ValidatorUtils.validateCorrelationAndConceptModel(applicationErrors);
		}
		
	}

}
