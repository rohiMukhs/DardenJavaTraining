package com.darden.dash.capacity.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This Model class is written for the purpose of building the 
 * response body for displaying the list of channels that have been
 * updated
 *
 */
@Getter
@Setter
public class EditChannelResponse extends ServiceResponse{
	
	private List<CapacityChannel> channels;

	public EditChannelResponse(List<CapacityChannel> channels) {
		super();
		this.channels = channels;
	}
}
