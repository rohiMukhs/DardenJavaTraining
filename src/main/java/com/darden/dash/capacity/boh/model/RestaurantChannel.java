package com.darden.dash.capacity.boh.model;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantChannel implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigInteger restaurantChannelId;
	private String restaurantChannelName;
	private String isSelectedFlag;
}
