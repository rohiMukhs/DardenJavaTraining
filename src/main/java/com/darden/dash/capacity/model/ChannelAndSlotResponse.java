package com.darden.dash.capacity.model;

import java.util.List;

import com.darden.dash.common.model.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelAndSlotResponse extends ServiceResponse{
	
	List<SlotChannel> slotChannelList;
	
}
