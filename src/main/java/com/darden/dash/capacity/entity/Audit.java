package com.darden.dash.capacity.entity;

import java.io.Serializable;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.darden.dash.capacity.util.CapacityConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;


/**
 * @author skashala
 *
 * The persistent class is common  audit columns for all the entities
 */

@Getter
@Setter
@MappedSuperclass
public abstract class Audit implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = CapacityConstants.CREATED_BY)
	private String createdBy;

	@JsonFormat(pattern = CapacityConstants.YYYY_MM_DD_T_HH_MM_SS_Z, timezone = CapacityConstants.UTC)
	@Column(name = CapacityConstants.CREATED_DATETIME)
	private Instant createdDatetime;

	@Column(name = CapacityConstants.LAST_MODIFIED_BY)
	private String lastModifiedBy;

	@JsonFormat(pattern = CapacityConstants.YYYY_MM_DD_T_HH_MM_SS_Z, timezone = CapacityConstants.UTC)
	@Column(name = CapacityConstants.LAST_MODIFIED_DATETIME)
	private Instant lastModifiedDatetime;
}
