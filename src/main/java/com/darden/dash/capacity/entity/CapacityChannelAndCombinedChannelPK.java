package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.darden.dash.capacity.util.CapacityConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 * The primary key class for the capacity_channel_and_combined_channel database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class CapacityChannelAndCombinedChannelPK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name=CapacityConstants.COMBINED_CAPACITY_CHANNEL_ID, insertable=false, updatable=false)
	private BigInteger combinedCapacityChannelId;

	@Column(name=CapacityConstants.CAPACITY_CHANNEL_ID, insertable=false, updatable=false)
	private BigInteger capacityChannelId;
}