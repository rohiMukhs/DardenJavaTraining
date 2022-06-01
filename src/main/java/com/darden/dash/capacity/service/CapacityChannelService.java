package com.darden.dash.capacity.service;

import java.util.List;

import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
/**
 * 
 * @author vraviran
 * @date 25-May-2022
 * 
 * 		 Service Implementation class which holds method definitions which deals
 *       with capacity template activities or any business logic related to CapacityChannel
 *
 */
public interface CapacityChannelService {

	/**
	 * This service method is created to update the list of capacity channels
	 * based on the value of list of channels and user detail passed in the 
	 * parameters
	 * 
	 * @param editChannelInformationRequest
	 * @param user
	 * @return List<ChannelInformationRequest>
	 */
	public List<CapacityChannel> editChannelInformation(List<ChannelInformationRequest> editChannelInformationRequest,String user) throws JsonProcessingException;
	
	/**
	 * This service method is used to return boolean values based on the 
	 * condition if the passing values friendly name is present in database.
	 * This method is created for the purpose of validation of request body.
	 * 
	 * @param friendlyName
	 * @return boolean
	 * @throws JsonProcessingException
	 */
	public boolean friendlyNmValidation(ChannelInformationRequest validateChannel);
}
