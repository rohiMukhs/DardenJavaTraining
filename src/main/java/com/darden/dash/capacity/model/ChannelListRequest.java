package com.darden.dash.capacity.model;

import java.util.List;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 *
 *This Model class is written for the purpose of passing the
 * values of list of channel to be edited
 */

@Getter
@Setter
public class ChannelListRequest {

	@Valid
	List<ChannelInformationRequest> channels;
	
}
