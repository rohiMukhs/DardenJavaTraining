package com.darden.dash.capacity.boh.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.darden.dash.common.constant.ErrorCodeConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantSlotChannel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = ErrorCodeConstants.EC_4001)
	private BigInteger channelId;

	private String channelName;
	private String isSelectedFlag;
	private List<RestaurantSlotDetail> restaurantSlotDetails;
}