package com.darden.dash.capacity.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vijmohit
 * 
 *         This Model class is written for the purpose of showing the values of
 *         Create Request for Capacity Slot Request
 */

@Getter
@Setter
public class CapacitySlotRequest {

	private Integer capacitytemplateId;

	private List<CapacitySlotCount> slots;
}
