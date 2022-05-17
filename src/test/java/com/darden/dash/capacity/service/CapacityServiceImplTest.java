package com.darden.dash.capacity.service;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacitySlotTypeEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.mapper.CapacityTemplateMapper;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.service.impl.CapacityManagementServiceImpl;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.exception.ApplicationException;

@ExtendWith({ MockitoExtension.class })
class CapacityServiceImplTest {

	@InjectMocks
	CapacityManagementServiceImpl capacityManagementServiceImpl;

	@Mock
	private CapacityTemplateRepo capacityTemplateRepo;

	@Mock
	private CapacityChannelRepo capacityChannelRepo;

	@BeforeEach
	public void init() {
		CapacityTemplateMapper capacityTemplateMapper = Mappers.getMapper(CapacityTemplateMapper.class);
		ReflectionTestUtils.setField(capacityManagementServiceImpl, "capacityTemplateMapper", capacityTemplateMapper);
		CapacityChannelMapper capacityChannelMapper = Mappers.getMapper(CapacityChannelMapper.class);
		ReflectionTestUtils.setField(capacityManagementServiceImpl, "capacityChannelMapper", capacityChannelMapper);
	}

	@Test
	void testGetAllCapacityTemplates() {
		RequestContext.setConcept("1");
		when(capacityTemplateRepo.findByConceptId(BigInteger.ONE)).thenReturn((getAllCapacityTemplates()));
		when(capacityChannelRepo.findAll()).thenReturn(Collections.singletonList(getCapacityChannel()));
		ResponseEntity<Object> res = capacityManagementServiceImpl.getAllCapacityTemplates();
		assertNotNull(res);
	}
	
	@Test
	void testGetAllCapacityTemplatesForInvalidConcept() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			capacityManagementServiceImpl.getAllCapacityTemplates();
	    });
		assertTrue(instanceOf(ApplicationException.class).matches(exception));
	}

	private List<CapacityTemplateEntity> getAllCapacityTemplates() {
		List<CapacityTemplateEntity> capacityList = new ArrayList<>();
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.valueOf(1));
		capacityTemplateEntity.setCapacityTemplateNm("Lorum Ipsum");
		capacityTemplateEntity.setEffectiveDate(new Date());
		capacityTemplateEntity.setExpiryDate(new Date());
		capacityTemplateEntity.setMonFlg("Y");
		capacityTemplateEntity.setTueFlg("Y");
		capacityTemplateEntity.setWedFlg("Y");
		capacityTemplateEntity.setThuFlg("N");
		capacityTemplateEntity.setFriFlg("N");
		capacityTemplateEntity.setSatFlg("Y");
		capacityTemplateEntity.setSunFlg("Y");
		capacityTemplateEntity.setConceptId(BigInteger.ONE);
		capacityTemplateEntity.setStartTime(java.time.LocalTime.parse("11:46:55"));
		capacityTemplateEntity.setEndTime(java.time.LocalTime.parse("12:46:55"));
		capacityTemplateEntity.setCapacityTemplateAndCapacityChannels(getCapacityTemplateAndChannels());
		capacityTemplateEntity.setCapacitySlots(getCapacitySlots());
		capacityList.add(capacityTemplateEntity);
		return capacityList;
	}

	private List<CapacitySlotEntity> getCapacitySlots() {
		List<CapacitySlotEntity> capacitySlotEntities = new ArrayList<>();
		CapacitySlotEntity capacitySlotEntity = new CapacitySlotEntity();
		capacitySlotEntity.setCapacityCnt("1");
		capacitySlotEntity.setCapacitySlotId(BigInteger.valueOf(2));
		capacitySlotEntity.setStartTime(java.time.LocalTime.parse("11:00:00"));
		capacitySlotEntity.setEndTime(java.time.LocalTime.parse("11:46:55"));
		capacitySlotEntity.setCapacitySlotType(getCapacitySlotType());
		capacitySlotEntity.setCapacityChannel(getCapacityChannel());
		capacitySlotEntity.setIsDeletedFlg("N");
		capacitySlotEntities.add(capacitySlotEntity);
		return capacitySlotEntities;
	}

	private CapacitySlotTypeEntity getCapacitySlotType() {
		CapacitySlotTypeEntity capacitySlotTypeEntity = new CapacitySlotTypeEntity();
		capacitySlotTypeEntity.setCapacitySlotTypeId(BigInteger.ONE);
		capacitySlotTypeEntity.setCapacitySlotTypeNm("Test");
		return capacitySlotTypeEntity;
	}

	private List<CapacityTemplateAndCapacityChannelEntity> getCapacityTemplateAndChannels() {
		List<CapacityTemplateAndCapacityChannelEntity> list = new ArrayList<>();
		CapacityTemplateAndCapacityChannelEntity capacityTemplateAndCapacityChannelEntity = new CapacityTemplateAndCapacityChannelEntity();
		capacityTemplateAndCapacityChannelEntity.setIsSelectedFlag("N");
		capacityTemplateAndCapacityChannelEntity.setCapacityChannel(getCapacityChannel());
		list.add(capacityTemplateAndCapacityChannelEntity);
		return list;
	}

	private CapacityChannelEntity getCapacityChannel() {
		CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
		capacityChannelEntity.setCapacityChannelId(BigInteger.ONE);
		capacityChannelEntity.setCapacityChannelNm("Togo");
		capacityChannelEntity.setOperationalHoursStartTime(Time.valueOf("11:40:55"));
		capacityChannelEntity.setOperationalHoursEndTime(Time.valueOf("12:40:55"));
		capacityChannelEntity.setIsCombinedFlg("Y");
		capacityChannelEntity.setFirendlyNm("togo");
		capacityChannelEntity.setInterval(0);
		capacityChannelEntity.setCapacityChannelAndCombinedChannels1(getCombinedCapacityChannel());
		return capacityChannelEntity;
	}

	private List<CapacityChannelAndCombinedChannelEntity> getCombinedCapacityChannel() {
		CapacityChannelAndCombinedChannelEntity capacityChannelAndCombinedChannelEntity=new CapacityChannelAndCombinedChannelEntity();
		CapacityChannelEntity capacityChannelEntity = new CapacityChannelEntity();
		capacityChannelEntity.setCapacityChannelId(BigInteger.ONE);
		capacityChannelEntity.setCapacityChannelNm("Togo");
		capacityChannelEntity.setFirendlyNm("togo");
		capacityChannelEntity.setOperationalHoursStartTime(Time.valueOf("11:40:55"));
		capacityChannelEntity.setOperationalHoursEndTime(Time.valueOf("12:40:55"));
		capacityChannelEntity.setInterval(0);
		capacityChannelAndCombinedChannelEntity.setCapacityChannel2(capacityChannelEntity);
		return Collections.singletonList(capacityChannelAndCombinedChannelEntity);
	}
}
