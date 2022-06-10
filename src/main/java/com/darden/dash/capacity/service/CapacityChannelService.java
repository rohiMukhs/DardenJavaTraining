package com.darden.dash.capacity.service;

import java.util.List;

import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.model.CombineChannel;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
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
	 * @param editChannelInformationRequest List of Request class containing information
	 * 					of list of capacity channels to be edited.
	 * 
	 * @param user	information of user operating on the update action.
	 * 
	 * @return List<ChannelInformationRequest> List of updated channel information in response.
	 * 
	 * @throws JsonProcessingException   if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	public List<CapacityChannel> editChannelInformation(List<ChannelInformationRequest> editChannelInformationRequest,String user) throws JsonProcessingException;
	
	/**
	 * This service method is used to return boolean values based on the 
	 * condition if the passing values friendly name is present in database.
	 * This method is created for the purpose of validation of request body.
	 * 
	 * @param validateChannel request class containing detail of capacity 
	 * 						channel to be updated.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean friendlyNmValidation(ChannelInformationRequest validateChannel);
	
	/**
	 * This service method is used to create combine channel based on the 
	 * data for Combine channel and channel to be assigned to that combine channel
	 * provided in createCombinedChannelRequest request and user detail passed in
	 * the parameter of method
	 * 
	 * @param createCombinedChannelRequest request class containing detail of
	 * 								capacity combine to be created.
	 * 
	 * @param userDetail information of user operating on the create action.
	 * 
	 * @return CombineChannel model class containing detail of created 
	 * 							combined channel.
	 * 
	 * @throws JsonProcessingException if any json processing exception is thrown at
	 *                                 runtime e.g json parsing.
	 */
	public CombineChannel addCombinedChannel(CreateCombineChannelRequest createCombinedChannelRequest,String userDetail) throws JsonProcessingException;
	
	/**
	 * This service method is used to return boolean values based on the 
	 * condition if the passing values capacity Channel name is present in database.
	 * This method is created for the purpose of validation of request body.
	 * 
	 * @param capacityChannelNm Capacity Channel Name to be validated in database.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateChannelNmValidation(String capacityChannelNm);
	
	/**
	 * This service method is used to return boolean values based on the 
	 * condition if the passing values friendly name is present in database.
	 * This method is created for the purpose of validation of request body.
	 * 
	 * @param friendlyNm friendly Name to be validated in database.
	 * 
	 * @return boolean returns the boolean value based on the condition.
	 */
	public boolean validateChannelFriendlyNmValidation(String friendlyNm);
}
