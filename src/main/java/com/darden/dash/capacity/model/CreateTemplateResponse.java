package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"capacityTemplateId","capacityTemplateName"})
public class CreateTemplateResponse extends CapacityTemplateCommon implements Serializable{
	
	private BigInteger capacityTemplateId;
	private String capacityTemplateName;
	private String createdBy;
	private Instant createdDateTime;
	private String lastModifiedBy;
	private Instant lastModifiedDateTime;
	private List<SlotChannel> slotChannels = new ArrayList<>();
}
