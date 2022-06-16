package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 * 
 * This Model class is written for the purpose of showing the
 * values of channel
 *
 */
@Getter
@Setter
public class Channel implements Serializable{

	private BigInteger capacityChannelId;
	private String capacityChannelName;
	private String isSelectedFlag;

}