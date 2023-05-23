package com.darden.dash.capacity.model;

import java.math.BigInteger;
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

	private String capacitySlotType;
	private Integer capacityCount;
	private String currentDate;
    private BigInteger locationId;
    private BigInteger channelId;
	private List<CapacitySlotTransaction> slots;
	
}