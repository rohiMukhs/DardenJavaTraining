package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author skashala
 * 
 * This Model class is written for the purpose of showing the
 *  values of capacity template response
 *
 */

@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CapacityResponse extends ServiceResponse implements Serializable{

	private List<CapacityTemplate> capacityTemplates = new ArrayList<>();
	private List<ReferenceDatum> referenceData = new ArrayList<>();
	public CapacityResponse(List<CapacityTemplate> capacityTemplates, List<ReferenceDatum> referenceData) {
		super();
		this.capacityTemplates = capacityTemplates;
		this.referenceData = referenceData;
	}
}