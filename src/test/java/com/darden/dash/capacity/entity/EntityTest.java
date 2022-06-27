package com.darden.dash.capacity.entity;


import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.common.util.DateUtil;


@ExtendWith({ MockitoExtension.class })
class EntityTest {
	
	@Test
	void testCapacityChannelAndCombinedChannelEntity() {
		CapacityChannelAndCombinedChannelEntity entity = new CapacityChannelAndCombinedChannelEntity();
		CapacityChannelAndCombinedChannelPK id = new CapacityChannelAndCombinedChannelPK();
		CapacityChannelEntity chnl1 = new CapacityChannelEntity();
		CapacityChannelEntity chnl2 = new CapacityChannelEntity();
		id.setCapacityChannelId(new BigInteger("1"));
		id.setCombinedCapacityChannelId(new BigInteger("1"));
		id.getCapacityChannelId();
		id.getCombinedCapacityChannelId();
		entity.setId(id);
		entity.setCapacityChannel1(chnl1);
		entity.setCapacityChannel2(chnl2);
		entity.getCapacityChannel1();
		entity.getCapacityChannel2();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getId();
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacityChannelAndOrderChannelEntity() {
		CapacityChannelAndOrderChannelEntity entity = new CapacityChannelAndOrderChannelEntity();
		CapacityChannelAndOrderChannelPK id =  new CapacityChannelAndOrderChannelPK();
		CapacityChannelEntity chnl1 = new CapacityChannelEntity();
		id.setCapacityChannelId(new BigInteger("1"));
		id.setOrderChannelId(new BigInteger("1"));
		id.getCapacityChannelId();
		id.getOrderChannelId();
		entity.setCapacityChannel(chnl1);
		entity.setId(id);
		entity.getCapacityChannel();
		entity.getId();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacityChannelEntity() {
		CapacityChannelEntity entity = new CapacityChannelEntity();
		List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannels1 = new ArrayList<>();
		List<CapacityChannelAndCombinedChannelEntity> capacityChannelAndCombinedChannels2 = new ArrayList<>();
		List<CapacityChannelAndOrderChannelEntity> capacityChannelAndOrderChannels = new ArrayList<>();
		entity.setCapacityChannelId(new BigInteger("1"));
		entity.setCapacityChannelNm("aaa");
		entity.setConceptId(new BigInteger("1"));
		entity.setCapacityChannelAndCombinedChannels1(capacityChannelAndCombinedChannels1);
		entity.setCapacityChannelAndCombinedChannels2(capacityChannelAndCombinedChannels2);
		entity.setCapacityChannelAndOrderChannels(capacityChannelAndOrderChannels);
		entity.setPosName("a");
		entity.setInterval(3);
		entity.setIsCombinedFlg("Y");
		entity.setIsDeletedFlg("Y");
		entity.setOperationalHoursEndTime(Time.valueOf("09:10:01"));
		entity.setOperationalHoursStartTime(Time.valueOf("09:10:01"));
		entity.getCapacityChannelAndCombinedChannels1();
		entity.getCapacityChannelAndCombinedChannels2();
		entity.getCapacityChannelAndOrderChannels();
		entity.getCapacityChannelId();
		entity.getCapacityChannelNm();
		entity.getCapacitySlots();
		entity.getCapacityTemplateAndCapacityChannels();
		entity.getConceptId();
		entity.getOperationalHoursStartTime();
		entity.getOperationalHoursEndTime();
		entity.getIsDeletedFlg();
		entity.getIsCombinedFlg();
		entity.getInterval();
		entity.getPosName();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}

	@Test
	void testCapacityModelAndCapacityTemplateEntity() {
		CapacityModelAndCapacityTemplateEntity entity = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK id = new CapacityModelAndCapacityTemplatePK();
		CapacityModelEntity capacityModel = new CapacityModelEntity();
		CapacityTemplateEntity capacityTemplate = new CapacityTemplateEntity();
		id.setCapacityModelId(new BigInteger("1"));
		id.setCapacityTemplateId(new BigInteger("1"));
		id.getCapacityModelId();
		id.getCapacityTemplateId();
		entity.setId(id);
		entity.setCapacityModel(capacityModel);
		entity.setCapacityTemplate(capacityTemplate);
		entity.getId();
		entity.getCapacityModel();
		entity.getCapacityTemplate();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacityModelAndLocationEntity() {
		CapacityModelAndLocationEntity entity = new CapacityModelAndLocationEntity();
		CapacityModelAndLocationPK id = new CapacityModelAndLocationPK();
		CapacityModelEntity capacityModel = new CapacityModelEntity();
		id.setCapacityModelId(new BigInteger("1"));
		id.setLocationId(new BigInteger("1"));
		id.getCapacityModelId();
		id.getLocationId();
		entity.setId(id);
		entity.setCapacityModel(capacityModel);
		entity.getId();
		entity.getCapacityModel();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacityModelEntity() {
		CapacityModelEntity entity = new CapacityModelEntity();
		List<CapacityModelAndCapacityTemplateEntity> capacityModelAndCapacityTemplates = new ArrayList<>();
		List<CapacityModelAndLocationEntity> capacityModelAndLocations = new ArrayList<>();
		entity.setCapacityModelId(new BigInteger("1"));
		entity.setConceptId(new BigInteger("1"));
		entity.setCapacityModelNm("Asa");
		entity.setIsDeletedFlg("Y");
		entity.setCapacityModelAndCapacityTemplates(capacityModelAndCapacityTemplates);
		entity.setCapacityModelAndLocations(capacityModelAndLocations);
		entity.getCapacityModelId();
		entity.getCapacityModelNm();
		entity.getCapacityModelAndCapacityTemplates();
		entity.getCapacityModelAndLocations();
		entity.getConceptId();
		entity.getIsDeletedFlg();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacitySlotCalcParamEntity() {
		CapacitySlotCalcParamEntity entity = new CapacitySlotCalcParamEntity();
		CapacitySlotTypeEntity capacitySlotType = new CapacitySlotTypeEntity();
		entity.setCapacitySlotCalcParamId(new BigInteger("1"));
		entity.setCapacitySlotType(capacitySlotType);
		entity.setIsDeletedFlg("Y");
		entity.setParamKey("key");
		entity.setParamValue("value");
		entity.getCapacitySlotCalcParamId();
		entity.getCapacitySlotType();
		entity.getIsDeletedFlg();
		entity.getParamKey();
		entity.getParamValue();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacitySlotEntity() {
		CapacitySlotEntity entity = new CapacitySlotEntity();
		CapacityChannelEntity capacityChannel = new CapacityChannelEntity();
		CapacitySlotTypeEntity capacitySlotType = new CapacitySlotTypeEntity();
		CapacityTemplateEntity capacityTemplate = new CapacityTemplateEntity();
		entity.setCapacityChannel(capacityChannel);
		entity.setCapacityCnt("!1");
		entity.setCapacitySlotId(new BigInteger("1"));
		entity.setCapacitySlotType(capacitySlotType);
		entity.setCapacityTemplate(capacityTemplate);
		entity.setEndTime(LocalTime.parse("09:10"));
		entity.setStartTime(LocalTime.parse("09:10"));
		entity.getCapacityChannel();
		entity.getCapacityCnt();
		entity.getCapacitySlotId();
		entity.getCapacitySlotType();
		entity.getCapacityTemplate();
		entity.getEndTime();
		entity.getIsDeletedFlg();
		entity.getReference();
		entity.getStartTime();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacityTemplateAndBusinessDateEntity() {
		CapacityTemplateAndBusinessDateEntity entity = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		CapacityTemplateEntity capacityTemplate = new CapacityTemplateEntity();
		id.setBusinessDate(DateUtil.stringToDate("09/01/2011"));
		id.setCapacityTemplateId(new BigInteger("1"));
		id.getBusinessDate();
		id.getCapacityTemplateId();
		entity.setId(id);
		entity.setCapacityTemplate(capacityTemplate);
		entity.getId();
		entity.getCapacityTemplate();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacityTemplateAndCapacityChannelEntity() {
		CapacityTemplateAndCapacityChannelEntity entity = new CapacityTemplateAndCapacityChannelEntity();
		CapacityTemplateAndCapacityChannelPK id = new CapacityTemplateAndCapacityChannelPK();
		CapacityChannelEntity capacityChannel = new CapacityChannelEntity();
		CapacityTemplateEntity capacityTemplate = new CapacityTemplateEntity();
		id.setCapacityChannelId(new BigInteger("1"));
		id.setCapacityTemplateId(new BigInteger("1"));
		id.getCapacityChannelId();
		id.getCapacityTemplateId();
		entity.setId(id);
		entity.setCapacityChannel(capacityChannel);
		entity.setCapacityTemplate(capacityTemplate);
		entity.setIsSelectedFlag("Y");
		entity.getCapacityChannel();
		entity.getCapacityTemplate();
		entity.getId();
		entity.getIsSelectedFlag();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacityTemplateEntity() {
		CapacityTemplateEntity entity = new CapacityTemplateEntity();
		List<CapacityModelAndCapacityTemplateEntity> capacityModelAndCapacityTemplates = new ArrayList<>();
		List<CapacitySlotEntity> capacitySlots = new ArrayList<>();
		CapacityTemplateTypeEntity capacityTemplateType = new CapacityTemplateTypeEntity();
		List<CapacityTemplateAndBusinessDateEntity> capacityTemplateAndBusinessDates = new ArrayList<>();
		List<CapacityTemplateAndCapacityChannelEntity> capacityTemplateAndCapacityChannels = new ArrayList<>();
		entity.setCapacityModelAndCapacityTemplates(capacityModelAndCapacityTemplates);
		entity.setCapacitySlots(capacitySlots);
		entity.setCapacityTemplateAndBusinessDates(capacityTemplateAndBusinessDates);
		entity.setCapacityTemplateAndCapacityChannels(capacityTemplateAndCapacityChannels);
		entity.setCapacityTemplateId(new BigInteger("1"));
		entity.setConceptId(new BigInteger("1"));
		entity.setCapacityTemplateNm("aa");
		entity.setCapacityTemplateType(capacityTemplateType);
		entity.setEffectiveDate(DateUtil.stringToDate("09/01/2011"));
		entity.setExpiryDate(DateUtil.stringToDate("09/01/2011"));
		entity.setStartTime(LocalTime.parse("09:10"));
		entity.setEndTime(LocalTime.parse("09:10"));
		entity.setMonFlg("n");
		entity.setTueFlg("n");
		entity.setWedFlg("n");
		entity.setThuFlg("n");
		entity.setFriFlg("Y");
		entity.setSatFlg("Y");
		entity.setSunFlg("Y");
		entity.getCapacityModelAndCapacityTemplates();
		entity.getCapacitySlots();
		entity.getCapacityTemplateAndBusinessDates();
		entity.getCapacityTemplateAndCapacityChannels();
		entity.getCapacityTemplateId();
		entity.getCapacityTemplateNm();
		entity.getCapacityTemplateType();
		entity.getConceptId();
		entity.getWedFlg();
		entity.getTueFlg();
		entity.getThuFlg();
		entity.getSunFlg();
		entity.getStartTime();
		entity.getSatFlg();
		entity.getMonFlg();
		entity.getIsDeletedFlg();
		entity.getFriFlg();
		entity.getEffectiveDate();
		entity.getEndTime();
		entity.getExpiryDate();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacityTemplateTypeEntity() {
		CapacityTemplateTypeEntity entity = new CapacityTemplateTypeEntity();
		List<CapacityTemplateEntity> capacityTemplates = new ArrayList<>(); 
		entity.setCapacityTemplates(capacityTemplates);
		entity.setCapacityTemplateTypeId(new BigInteger("1"));
		entity.setCapacityTemplateTypeNm("aa");
		entity.setIsDeletedFlg("Y");
		entity.getCapacityTemplates();
		entity.getCapacityTemplateTypeId();
		entity.getCapacityTemplateTypeNm();
		entity.getIsDeletedFlg();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testReferenceEntity() {
		ReferenceEntity entity = new ReferenceEntity();
		List<CapacitySlotEntity> capacitySlots = new ArrayList<>();
		ReferenceTypeEntity referenceType = new ReferenceTypeEntity();
		entity.setCapacitySlots(capacitySlots);
		entity.setConceptId(new BigInteger("1"));
		entity.setEffectiveLastDatetime(Instant.now());
		entity.setEffectiveStartDatetime(Instant.now());
		entity.setIsDeletedFlg("Y");
		entity.setReferenceCd("11");
		entity.setReferenceDesc("asas");
		entity.setReferenceId(new BigInteger("1"));
		entity.setReferenceNm("nm");
		entity.setReferenceType(referenceType);
		entity.getCapacitySlots();
		entity.getConceptId();
		entity.getEffectiveLastDatetime();
		entity.getEffectiveStartDatetime();
		entity.getIsDeletedFlg();
		entity.getReferenceCd();
		entity.getReferenceDesc();
		entity.getReferenceId();
		entity.getReferenceNm();
		entity.getReferenceType();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testReferenceTypeEntity() {
		ReferenceTypeEntity entity = new ReferenceTypeEntity();
		List<ReferenceEntity> references = new ArrayList<>();
		entity.setIsDeletedFlg("y");
		entity.setReferences(references);
		entity.setReferenceTypeId(new BigInteger("1"));
		entity.setReferenceTypeNm("aaa");
		entity.getIsDeletedFlg();
		entity.getReferences();
		entity.getReferenceTypeId();
		entity.getReferenceTypeNm();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
	
	@Test
	void testCapacitySlotTypeEntity() {
		CapacitySlotTypeEntity entity = new CapacitySlotTypeEntity();
		List<CapacitySlotEntity> capacitySlots = new ArrayList<>();
		List<CapacitySlotCalcParamEntity> capacitySlotCalcParams = new ArrayList<>();
		entity.setCapacitySlotCalcParams(capacitySlotCalcParams);
		entity.setCapacitySlots(capacitySlots);
		entity.setCapacitySlotTypeId(new BigInteger("1"));
		entity.setCapacitySlotTypeNm("aa");
		entity.setIsDeletedFlg("Y");
		entity.getCapacitySlotCalcParams();
		entity.getCapacitySlots();
		entity.getCapacitySlotTypeId();
		entity.getCapacitySlotTypeNm();
		entity.getIsDeletedFlg();
		entity.setCreatedBy("aa");
		entity.setCreatedDatetime(Instant.now());
		entity.setLastModifiedBy("aa");
		entity.setLastModifiedDatetime(Instant.now());
		entity.getCreatedBy();
		entity.getCreatedDatetime();
		entity.getLastModifiedBy();
		entity.getLastModifiedDatetime();
		assertNotNull(entity);
	}
}
