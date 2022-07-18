package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 * This Model class is written for the purpose of showing the
 *  values of Reference data
 *
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceDatum implements Serializable{
	private List<CapacityChannel> capacityChannel = new ArrayList<>();
	private List<CapacityTemplate> assignTemplate  = new ArrayList<>();
}
