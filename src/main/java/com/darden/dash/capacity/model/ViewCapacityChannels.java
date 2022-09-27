package com.darden.dash.capacity.model;

import com.darden.dash.common.model.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @author vraviran
 * 
 * This model class is written to build response body to display all
 * the capacity channels both staic and combine channels within the 
 * concept.
 *
 */
@Getter
@AllArgsConstructor
public class ViewCapacityChannels extends ServiceResponse {
	
	private ReferenceDatum referenceDatum;

}
