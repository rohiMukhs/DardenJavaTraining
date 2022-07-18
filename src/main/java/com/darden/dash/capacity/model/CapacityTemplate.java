package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 * 
 * This Model class is written for the purpose of showing the
 * values of capacity Template
 *
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Generated
public class CapacityTemplate implements Serializable{

	private String capacityTemplateId;
	private String templateName;
	private String effectiveDate;
	private String expiryDate;
	private String capacityTemplateType;
	private String sunDay;
	private String monDay;
	private String tueDay;
	private String wedDay;
	private String thuDay;
	private String friDay;
	private String satDay;
	private List<BusinessDate> businessDate;
	private String createdBy;
	private Instant createdDatetime;
	private String lastModifiedBy;
	private Instant lastModifiedDatetime;
	private List<Channel> channels = new ArrayList<>();
	private String slotStartTime;
	private String slotEndTime;
	private List<SlotChannel> slotChannels = new ArrayList<>();

	

}