package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This Model class is written for the purpose of showing the
 *       values of capacity model along with assigned template 
 *       and restaurants.
 *
 */
@Setter
@Getter
public class CapacityModel implements Serializable{
	
	private String capacityModelName;
	private BigInteger capacityModelId;
	private Set<String> capacityTemplateList ;
	private Set<BigInteger> restaurants;
	private String isDeletedFlg;
	private String createdBy;
	private Instant createdDateTime;
	private String lastModifiedBy;
	private Instant lastModifiedDateTime;
	
}