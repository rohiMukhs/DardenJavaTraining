package com.darden.dash.capacity.boh.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.Channel;
import com.darden.dash.capacity.model.SlotChannel;

import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Generated
public class RestaurantCapacityTemplate implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String restaurantTemplateId;
	private String templateName;
	private Date effectiveDate;
	private Date expiryDate;
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
	private List<RestaurantChannel> restaurantChannels = new ArrayList<>();
	private List<RestaurantSlotChannel> restaurantSlotChannels = new ArrayList<>();

}
