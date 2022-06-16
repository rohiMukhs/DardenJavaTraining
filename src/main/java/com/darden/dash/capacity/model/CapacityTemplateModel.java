package com.darden.dash.capacity.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * @author skashala
 * 
 * This Model class is written for the purpose of showing the
 * values of Capacity Model
 *
 */
@Getter
@Setter
public class CapacityTemplateModel {

	private String templateModelName;
	private List<TemplatesAssigned> templatesAssigned = new ArrayList<>();
	private List<RestaurantsAssigned> restaurantsAssigned = new ArrayList<>();
	private String isDeletedFlg;
	private String createdBy;
	private Instant createdDateTime;
	private String lastModifiedBy;
	private Instant lastModifiedDateTime;
}
