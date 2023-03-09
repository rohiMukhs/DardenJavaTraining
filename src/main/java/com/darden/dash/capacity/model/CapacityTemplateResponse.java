package com.darden.dash.capacity.model;

import java.io.Serializable;

import com.darden.dash.common.model.ServiceResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CapacityTemplateResponse extends ServiceResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private CapacityTemplate capacityTemplates;
	private ReferenceDatum referenceData ;
	public CapacityTemplateResponse(CapacityTemplate capacityTemplates, ReferenceDatum referenceData) {
		super();
		this.capacityTemplates = capacityTemplates;
		this.referenceData = referenceData;
	}
	
}
