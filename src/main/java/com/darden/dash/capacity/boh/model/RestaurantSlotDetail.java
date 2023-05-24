package com.darden.dash.capacity.boh.model;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantSlotDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigInteger slotId;
	private String startTime;
	private String endTime;
	private Integer capacityCount;
	private String slotTypeId;

}
