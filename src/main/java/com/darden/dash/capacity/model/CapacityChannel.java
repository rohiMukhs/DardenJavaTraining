package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
public class CapacityChannel {

	private BigInteger capacityChannelId;
	private String capacityChannelName;
	private String firendlyName;
	private String interval;
	private String operationalHoursStartTime;
	private String operationalHoursEndTime;
	private String isCombinedFlg;
	private List<Channel> combinedChannels=new ArrayList<>();
  
}