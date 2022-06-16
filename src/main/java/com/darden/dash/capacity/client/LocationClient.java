package com.darden.dash.capacity.client;

import java.util.List;

import com.darden.dash.capacity.model.Locations;
/**
 * 
 * @author vraviran
 * 
 *		The purpose of writing this ClientCall interface is to
 * 		declare all the methods used in location micro service 
 * 		client call
 *
 */
public interface LocationClient {
	
	/**
	 * The purpose of this method is retrieve location list data from
	 * the location micro service
	 * 
	 * @return List<Locations> list of location data is retrieved.
	 */
	public List<Locations> getAllRestaurants();
	
}
