package com.darden.dash.capacity.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
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
public class CapacityTemplate {

	private String capacityTemplateId;
	private String templateName;
	private String effectiveDate;
	private String expiryDate;
	private String sunDay;
	private String monDay;
	private String tueDay;
	private String wedDay;
	private String thuDay;
	private String friDay;
	private String satDay;
	private String createdBy;
	private Instant createdDatetime;
	private String lastModifiedBy;
	private Instant lastModifiedDatetime;
	private List<Channel> channels = new ArrayList<>();
	private String slotStartTime;
	private String slotEndTime;
	private List<SlotChannel> slotChannels = new ArrayList<>();

	

}