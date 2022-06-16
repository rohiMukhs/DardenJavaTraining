package com.darden.dash.capacity.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 *  @author skashala
 * 
 * This Model class is written for the purpose of showing the
 *  values of Create Request for Capacity Model
 */
@Getter
@Setter
public class CapacityModelRequest {

	private String templateModelName;
	private List<TemplatesAssigned> templatesAssigned;
	private List<RestaurantsAssigned> restaurantsAssigned;

}

