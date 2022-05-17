package com.darden.dash.capacity.model;

import java.util.ArrayList;
import java.util.List;

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
public class ReferenceDatum {
	private List<CapacityChannel> capacityChannel = new ArrayList<>();
}
