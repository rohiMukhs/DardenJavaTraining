package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 *  @author vraviran
 * 
 * This Model class is written for the purpose of showing the
 *  values of Response of created Capacity Template, capacity Slot
 *  and Business Dates
 */
@Getter
@Setter
public class CreateTemplateResponse extends CapacityTemplateCommon {
	
	private BigInteger capacityTemplateId;
	private String capacityTemplateName;
	private String createdBy;
	private Instant createdDateTime;
	private String lastModifiedBy;
	private Instant lastModifiedDateTime;
	private List<CreateResponseSlot> slotChannels;
	
}
