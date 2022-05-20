package com.darden.dash.capacity.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vraviran
 * 
 *         This Model class is written for the purpose of showing the values of
 *         Response body for Capacity Template
 */
@Getter
@Setter
public class CreateCapacityTemplateResponse extends ServiceResponse {

	private CreateTemplateResponse capacityTemplate;

	public CreateCapacityTemplateResponse(CreateTemplateResponse capacityTemplate) {
		super();
		this.capacityTemplate = capacityTemplate;
	}
}
