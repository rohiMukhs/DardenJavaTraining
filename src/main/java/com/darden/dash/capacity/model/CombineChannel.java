package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vraviran
 * 
 * This model class is written for the purpose of displaying 
 * the value of created combine channel.
 *
 */
@Setter
@Getter
public class CombineChannel {

	private BigInteger combinedChannelId;
	
	private String combinedChannelName;
	
	private String friendlyName;
	
	private String combinedFlg;
	
	private Integer interval;
	
	private String operationHourStartTime;
	
	private String operationHourEndTime;
	
	private Set<String> channels;

	private String createdBy;

	private Instant createdDateTime;

	private String lastModifiedBy;
	
	private Instant lastModifiedDateTime;
}
