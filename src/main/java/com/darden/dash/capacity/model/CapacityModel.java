package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CapacityModel implements Serializable{
	
	private String capacityModelName;
	private BigInteger capacityModelId;
	private List<CapacityTemplate> capacityTemplateList ;
	private RestaurantDetail restaurants;
	private String isDeletedFlg;
	private String createdBy;
	private Instant createdDateTime;
	private String lastModifiedBy;
	private Instant lastModifiedDateTime;
	
}
