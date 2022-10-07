package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 *
 *The purpose of this model class is to display the restaurants details.
 */
@Setter
@Getter
public class RestaurantDetail implements Serializable {
	
	private List<BigInteger> viaTemplate;
	private List<BigInteger> viaOverride;
	private int restaurantCount;

}
