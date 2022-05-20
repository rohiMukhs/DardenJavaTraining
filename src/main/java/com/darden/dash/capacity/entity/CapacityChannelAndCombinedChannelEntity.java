package com.darden.dash.capacity.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;


/**
 * @author skashala
 * The persistent class for the capacity_channel_and_combined_channel database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_CHANNEL_AND_COMBINED_CHANNEL)
public class CapacityChannelAndCombinedChannelEntity extends Audit implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CapacityChannelAndCombinedChannelPK id;

	//bi-directional many-to-one association to CapacityChannel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.CAPACITY_CHANNEL_ID ,insertable=false, updatable=false)
	private CapacityChannelEntity capacityChannel1;

	//bi-directional many-to-one association to CapacityChannel
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name=CapacityConstants.COMBINED_CAPACITY_CHANNEL_ID,insertable=false, updatable=false)
	private CapacityChannelEntity capacityChannel2;

}