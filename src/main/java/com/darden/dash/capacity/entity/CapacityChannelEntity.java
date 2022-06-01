package com.darden.dash.capacity.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.darden.dash.capacity.util.CapacityConstants;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;


/**
 * @author skashala
 * The persistent class for the capacity_channel database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name=CapacityConstants.CAPACITY_CHANNEL)
public class CapacityChannelEntity extends Audit implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name=CapacityConstants.CAPACITY_CHANNEL_ID)
	private BigInteger capacityChannelId;

	@Column(name=CapacityConstants.CAPACITY_CHANNEL_NM)
	private String capacityChannelNm;

	@Column(name=CapacityConstants.CONCEPT_ID)
	private BigInteger conceptId;

	@Column(name=CapacityConstants.FIRENDLY_NM)
	private String firendlyNm;

	@Column(name=CapacityConstants.INTERVAL)
	private int interval;

	@Column(name=CapacityConstants.IS_COMBINED_FLG)
	private String isCombinedFlg;

	@Column(name=CapacityConstants.IS_DELETED_FLG)
	private String isDeletedFlg;

	@Column(name=CapacityConstants.OPERATIONAL_HOURS_END_TIME)
	private Time operationalHoursEndTime;

	@Column(name=CapacityConstants.OPERATIONAL_HOURS_START_TIME)
	private Time operationalHoursStartTime;

	//bi-directional many-to-one association to CapacityChannelAndCombinedChannel
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_CHANNEL1)
	private List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannels1;

	//bi-directional many-to-one association to CapacityChannelAndCombinedChannel
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_CHANNEL2)
	private List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannels2;

	//bi-directional many-to-one association to CapacityChannelAndOrderChannel
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_CHANNEL3)
	private List<CapacityChannelAndOrderChannelEntity> capacityChannelAndOrderChannels;

	//bi-directional many-to-one association to CapacitySlot
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_CHANNEL3)
	private List<CapacitySlotEntity> capacitySlots;

	//bi-directional many-to-one association to CapacityTemplateAndCapacityChannel
	@JsonBackReference
	@OneToMany(mappedBy=CapacityConstants.CAPACITY_CHANNEL3)
	private List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannels;

}