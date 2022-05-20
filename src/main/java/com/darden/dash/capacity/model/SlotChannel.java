package com.darden.dash.capacity.model;

import java.util.ArrayList;
import java.util.List;
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
public class SlotChannel {

	private String channelId;
	private String channelName;
	private List<SlotDetail> slotDetails = new ArrayList<>();

}
