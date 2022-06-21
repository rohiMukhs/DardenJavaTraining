package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This Model class is written for the purpose of showing the
 *       values of location from client call.
 *
 */
@Setter
@Getter
public class Locations implements Serializable{
	
	private BigInteger locationId;
	
	private BigInteger restaurantNumber;
	
	private String locationDescription;
	
	private String addressState;
	
	private String divisionName;
	
	private Region region;
	
	private Instant lastModifiedDateTime;

}
