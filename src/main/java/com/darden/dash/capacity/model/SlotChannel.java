package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @author skashala
 * This Model class is written for the purpose of showing the
 *  values of Slot Channel
 *
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SlotChannel {

	private BigInteger channelId;
	private String channelName;
	private String isSelectedFlag;
	private List<SlotDetail> slotDetails = new ArrayList<>();

}
