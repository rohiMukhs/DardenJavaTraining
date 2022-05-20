package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 *  @author vraviran
 * 
 * This Model class is written for the purpose of showing the
 *  values of create request for Capacity slot
 */
@Getter
@Setter
public class CreateResponseSlot {
	private BigInteger channelId;
	private String isSelectedFlag;
	private List<SlotDetail> slotDetails;
}
