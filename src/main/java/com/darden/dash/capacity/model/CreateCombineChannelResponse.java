package com.darden.dash.capacity.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This Model class is created for the purpose of building the
 * response body with the data of Combined channel created.
 *
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CreateCombineChannelResponse extends ServiceResponse{
	
	private CombineChannel response;

	public CreateCombineChannelResponse(CombineChannel response) {
		super();
		this.response = response;
	}

}
