package com.darden.dash.capacity.foh.service;

import java.util.List;

import com.darden.dash.capacity.model.CapacitySlotRequest;
import com.darden.dash.capacity.model.SlotChannel;

/**
 * 
 * @author vijmohit
 * @date 10-April-2023
 * 
 *       Service Implementation class which holds method definitions which deals
 *       with capacity Slots activities or any business logic related to
 *       CapacitySlotRequest
 * 
 */
public interface CapacityManagementFOHService {

	/**
	 * This method is used for update capacity slots records.
	 * 
	 * 
	 * 
	 */
	void updateCapacitySlot(CapacitySlotRequest capacitySlotRequest, String userName);
	
	
	List<SlotChannel> getChannelAndSlotForDateWithPopulatingSlots(String currentDate);
	
}
