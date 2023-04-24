package com.darden.dash.capacity.foh.service.impl;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.foh.service.CapacityManagementFOHService;
import com.darden.dash.capacity.model.CapacitySlotRequest;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTypeRepository;

/**
 * 
 * @author vijmohit
 * @date 16-May-2022
 *
 *       Service Implementation class which holds method definitions which deals
 *       with Capacity Management for FOH or any business logic related to
 *       Capacity Management
 */

@Service
public class CapacityManagementFOHServiceImpl implements CapacityManagementFOHService {

	CapacitySlotRepository capacitySlotRepository;

	CapacitySlotTypeRepository capacitySlotTypeRepository;

	@Autowired
	public CapacityManagementFOHServiceImpl(CapacitySlotRepository capacitySlotRepository,
			CapacitySlotTypeRepository capacitySlotTypeRepository) {
		super();
		this.capacitySlotRepository = capacitySlotRepository;
		this.capacitySlotTypeRepository = capacitySlotTypeRepository;
	}

	/**
	 * Method is for UPDATE operation. first it check capacity slot is empty or not
	 * then by using capacity slot id check capacity slot already present or not in
	 * DB if capacity slot present then update the capacity slot count and update
	 * capacity slot type then save the updated capacity slot.
	 * 
	 * @param capacitySlotRequest Request class containing the detail of Capacity
	 *                            Slot to be updated.
	 * 
	 * @param accessToken         Token used to authenticate the user and extract
	 *                            the userDetails for this API.
	 * 
	 * @param slotId              Slot Id of Capacity template to be updated.
	 * 
	 */

	@Override
	public void updateCapacitySlot(CapacitySlotRequest capacitySlotRequest, String userName) {

		if (CollectionUtils.isNotEmpty(capacitySlotRequest.getSlots())) {

			capacitySlotRequest.getSlots().forEach(capacitySlot -> {
				BigInteger slotId = capacitySlot.getCapacitySlotId();

				Optional<CapacitySlotEntity> findByCapacitySlotId = capacitySlotRepository.findById(slotId);

				CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();

				Instant dateTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);

				if (findByCapacitySlotId.isPresent()) {
					capacitySlotEntity = findByCapacitySlotId.get();
					capacitySlotEntity.setCapacityCnt(capacitySlot.getCapacityCount());
					capacitySlotEntity.setLastModifiedDatetime(dateTime);
					capacitySlotEntity.setLastModifiedBy(userName);

					Optional<CapacitySlotTypeEntity> findById = capacitySlotTypeRepository
							.findById(capacitySlot.getCapacitySlotTypeId());

					capacitySlotEntity.setCapacitySlotType(findById.get());
					capacitySlotRepository.save(capacitySlotEntity);
				}

			});

		}
	}

}
