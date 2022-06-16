package com.darden.dash.capacity.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.darden.dash.common.constant.ErrorCodeConstants;
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
public class SlotChannel implements Serializable{

	@NotNull(message = ErrorCodeConstants.EC_4001)
	private BigInteger channelId;
	
	private String channelName;
	private String isSelectedFlag;
	private List<SlotDetail> slotDetails = new ArrayList<>();

}
