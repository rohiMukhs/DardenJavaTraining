package com.darden.dash.capacity.client;

import java.util.List;

import com.darden.dash.capacity.model.OrderTemplate;

/**
 * 
 * @author asdattat
 *      The purpose of writing this ClientCall interface is to
 * 		declare all the methods used in order micro service 
 * 		client call
 */
public interface OrderClient {
	
	/**
	 * The purpose of this method is retrieve orderTemplate list data from
	 * the order micro service
	 * 
	 * @return List<OrderTemplate> list of orderTemplate data is retrieved.
	 */
	
	public List<OrderTemplate> getAllOrderTemplates();

}
