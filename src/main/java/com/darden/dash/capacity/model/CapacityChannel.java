package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 * 
 * This Model class is written for the purpose of showing the
 *       values of capacity channel
 *
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CapacityChannel implements Serializable{

	private BigInteger capacityChannelId;
	private String capacityChannelName;
	private String posName;
	private String interval;
	private String operationalHoursStartTime;
	private String operationalHoursEndTime;
	private String isCombinedFlg;
	private List<Channel> combinedChannels=new ArrayList<>();
  
}