package com.darden.dash.capacity.model;

import lombok.Getter;
import lombok.Setter;
/**
 * @author skashala
 * 
 *         This Model class is written for the purpose of showing the values of
 *         Response body for Capacity Model
 */
@Getter
@Setter
public class CapacityModelResponse extends ServiceResponse {
	
	private CapacityTemplateModel capacityTemplateModel;

	public CapacityModelResponse(CapacityTemplateModel capacityTemplateModel) {
		super();
		this.capacityTemplateModel = capacityTemplateModel;
	}

}
