package com.darden.dash.capacity.boh.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *  @author TuhinM
 *
 * This Model class is written for the purpose of showing the
 *  values of Get Request for Restaurant Capacity Template
 */
@Getter
@Setter
public class ViewRestaurantCapacityTemplate implements Serializable{
	private static final long serialVersionUID = 1L;

	@JsonProperty("Template Name")
	private String templateName;
	
	@JsonProperty("Type")
	private String templateType;
	
	@JsonProperty("Effective Date")
	private String effectiveDate;
	
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	
}
