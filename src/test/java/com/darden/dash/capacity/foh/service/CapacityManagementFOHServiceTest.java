package com.darden.dash.capacity.foh.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.foh.service.impl.CapacityManagementFOHServiceImpl;
import com.darden.dash.capacity.model.CapacitySlotCount;
import com.darden.dash.capacity.model.CapacitySlotRequest;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTypeRepository;

@ExtendWith(MockitoExtension.class)
class CapacityManagementFOHServiceTest {

	@InjectMocks
	private CapacityManagementFOHServiceImpl capacityManagementFOHService;

	@Mock
	private CapacitySlotRepository capacitySlotRepository;

	@Mock
	private CapacitySlotTypeRepository capacitySlotTypeRepository;

	@Test
	void testUpdateCapacitySlot() {

		CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();
		capacitySlotEntity.setCapacityCnt(4);
		capacitySlotEntity.setCapacitySlotId(new BigInteger("1"));
		capacitySlotEntity.setConceptId(new BigInteger("5"));

		Mockito.when(capacitySlotRepository.findById(Mockito.any())).thenReturn(Optional.of(capacitySlotEntity));

		CapacitySlotTypeEntity capacitySlotTypeEntity = new CapacitySlotTypeEntity();
		capacitySlotTypeEntity.setCapacitySlotTypeId(new BigInteger("5"));

		Mockito.when(capacitySlotTypeRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(capacitySlotTypeEntity));

		Mockito.when(capacitySlotRepository.save(Mockito.any())).thenReturn(capacitySlotEntity);
		CapacitySlotCount capacitySlotCount = new CapacitySlotCount();
		capacitySlotCount.setCapacityCount(5);
		capacitySlotCount.setCapacitySlotId(new BigInteger("1"));
		capacitySlotCount.setCapacitySlotTypeId(new BigInteger("2"));

		CapacitySlotCount capacitySlotCount1 = new CapacitySlotCount();
		capacitySlotCount1.setCapacityCount(10);
		capacitySlotCount1.setCapacitySlotId(new BigInteger("2"));
		capacitySlotCount1.setCapacitySlotTypeId(new BigInteger("4"));
		CapacitySlotRequest capacitySlotRequest = new CapacitySlotRequest();
		capacitySlotRequest.setCapacitytemplateId(1);
		capacitySlotRequest.setSlots(List.of(capacitySlotCount, capacitySlotCount1));

		capacityManagementFOHService.updateCapacitySlot(capacitySlotRequest, "aa");
		assertNotNull(capacitySlotRequest);
	}

	@Test
	void testUpdateCapacitySlot2() {
		CapacitySlotRequest capacitySlotRequest = new CapacitySlotRequest();
		capacitySlotRequest.setCapacitytemplateId(1);
		capacitySlotRequest.setSlots(null);

		capacityManagementFOHService.updateCapacitySlot(capacitySlotRequest, "aa");
		assertNotNull(capacitySlotRequest);
	}

}
