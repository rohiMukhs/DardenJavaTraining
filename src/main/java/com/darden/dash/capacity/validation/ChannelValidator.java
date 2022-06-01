package com.darden.dash.capacity.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darden.dash.capacity.model.ChannelListRequest;
import com.darden.dash.capacity.service.CapacityChannelService;
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
 * 
 * This class {@link ChannelValidator} contains the methods
*  used for validating the data which we get from Request Body and
*  Parameters for Create, Update and Delete Api's of CapacityChannel.
 *
 */

@Component
public class ChannelValidator implements DashValidator {
	
	private CapacityChannelService channelService;
	
	/**
	 * Autowiring required properties
	 * 
	 * @param channelService
	 */
	@Autowired
	public ChannelValidator(CapacityChannelService channelService) {
		super();
		this.channelService = channelService;
	}


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
		 * This validation method is used to validate if the values passed in 
		 * request body are not null, not blank ,no duplicate values for specific field ,
		 * min and max value for specific fields ,alphanumeric for specific fields and 
		 * so on if any of the validation fails certain application error is raised 
		 * with respect to the failed validation.
		 * 
		 */
		ChannelListRequest channelEditRequest = buildObject(object);
		if (!applicationErrors.isValidObject(channelEditRequest)) {
			applicationErrors.raiseExceptionIfHasErrors();
		}else {
			validateInRequestBody(buildObject(object), applicationErrors);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		
		/**
		 * This validation method is used for the database validation of the
		 * request body which is used for the UPDATE operation. Based on the 
		 * requirement of validation on specific field is checked if validation 
		 * fails application error is raised.
		 * 
		 */
		if (OperationConstants.OPERATION_UPDATE.getCode().equals(operation)) {
			validateInDbForUpdate(buildObject(object), applicationErrors);
			applicationErrors.raiseExceptionIfHasErrors();
		}
		
	}

	/**
	 * This validation method is for the purpose of validating friendly name
	 * field if it contains any duplicate value among the list of channels 
	 * that is passed in the request body.If the duplicate value of friendly 
	 * name is detected in list application error is raised.
	 * 
	 * @param buildObject
	 * @param applicationErrors
	 */
	private void validateInRequestBody(ChannelListRequest buildObject, ApplicationErrors applicationErrors) {
		for(int i= 0; i < buildObject.getChannels().size(); i++) {
			for(int j = i+1; j < buildObject.getChannels().size(); j++) {
				if(buildObject.getChannels().get(i).getFriendlyName().equals(buildObject.getChannels().get(j).getFriendlyName())) {
					applicationErrors.addErrorMessage(Integer.parseInt(CapacityConstants.EC_4500));
				}
			}
		}
	}

	/**
	 * This validation method is for the purpose of validating friendly name
	 * field to avoid the duplicate value in the database.In this method value of 
	 * friendly name is checked if present in database using the channel service 
	 * method if the data is already present application error is raised
	 * 
	 * @param buildObject
	 * @param applicationErrors
	 */
	private void validateInDbForUpdate(ChannelListRequest buildObject, ApplicationErrors applicationErrors) {
		
		buildObject.getChannels().stream().forEach(c -> {
			if(channelService.friendlyNmValidation(c)) {
				applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4009),
						c.getFriendlyName());
			}
		});
		
	}

	/**
	 * This method is used to build object of request body for validation.
	 * 
	 * @param object
	 * @return ChannelListRequest
	 * @throws JsonProcessingException
	 */
	private ChannelListRequest buildObject(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		String json = mapper.writeValueAsString(object);
		Gson gson = new Gson();
		return gson.fromJson(json, ChannelListRequest.class);
	}
	
	
}