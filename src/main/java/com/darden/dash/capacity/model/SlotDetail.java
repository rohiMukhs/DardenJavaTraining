package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 *  This Model class is written for the purpose of showing the
 *  values of Slot Detail
 *
 */
@Getter
@Setter
public class SlotDetail implements Serializable{

	private BigInteger slotId;
	private String startTime;
	private String endTime;
	private String capacityCount;
	private String slotTypeId;
	private String isDeletedFlg;

}
