package com.darden.dash.capacity.model;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditLocationDetailResponse {
	
	private Integer locationId;
	private Integer restaurantNumber;
	private Integer conceptId;
	private Integer divisionId;
	private Integer regionId;
	private String locationDesc;
	private String addressStreet;
	private String addressState;
	private String addressCity;
	private String phone;
	private String fax;
	private String zipCode;
	private String email;
	private String createdBy;
	private Instant createdDateTime;
	private String lastModifiedBy;
	private Instant lastModifiedDatetime;

}
