package com.darden.dash.capacity.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This Model class is written for the purpose of showing the
 *       values of Location data from client call.
 *
 */
@Setter
@Getter
public class GetLocationsResponse {
	
	private Integer status;
	
	private String title;
	
	private String correlationId;
	
	private List<Locations> locations;

}
