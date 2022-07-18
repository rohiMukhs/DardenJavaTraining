package com.darden.dash.capacity.validator.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.model.BusinessDate;
import com.darden.dash.capacity.model.CreateCapacityTemplateRequest;
import com.darden.dash.capacity.model.DeleteCapacityTemplateRequest;
import com.darden.dash.capacity.model.SlotChannel;
import com.darden.dash.capacity.model.SlotDetail;
import com.darden.dash.capacity.service.CapacityManagementService;
import com.darden.dash.capacity.util.CapacityManagementUtils;
import com.darden.dash.capacity.validation.CapacityValidator;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith({ MockitoExtension.class })
class CapacityValidatorTest {
	
	@InjectMocks
	private CapacityValidator capacityValidator;
	
	@Mock
	private CapacityManagementService capacityManagementService;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@Mock
	private ApplicationErrors applicationErrors;
	
	@Mock
	private CapacityManagementUtils capacityManagementUtils;
	
	@BeforeEach
	private void setup() {
		when(capacityManagementUtils.validateConceptId(Mockito.any(),Mockito.any())).thenReturn(false);
	}
	
	@Test
    void validateInDbForDeleteTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		DeleteCapacityTemplateRequest deleteRq = new DeleteCapacityTemplateRequest("99", "Y");
		Mockito.when(capacityManagementService.validateCapacityTemplateId(Mockito.anyString(),Mockito.any())).thenReturn(false);
		capacityValidator.validate(deleteRq, OperationConstants.OPERATION_DELETE.getCode());
		assertNotNull(deleteRq);
    }
	
	@Test
	void validateInDbForCreateTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");

		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<BusinessDate> date = new ArrayList<>();
		BusinessDate bdate = new BusinessDate();
		bdate.setDate("01/01/2011");
		date.add(bdate);
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("Days");
		request.setBusinessDates(date);
		request.setEffectiveDate("01/01/2011");
		request.setExpiryDate("01/01/2011");
		request.setMonDay("Y");
		request.setTueDay("Y");
		request.setWedDay("Y");
		request.setThuDay("N");
		request.setFriDay("N");
		request.setSatDay("Y");
		request.setSunDay("Y");
		request.setSlotStartTime("01:02");
		request.setSlotEndTime("02:09");
		request.setIsDeletedFlag("N");
		request.setSlotChannels(slotList);
		
		Mockito.when(capacityManagementService.validateCapacityTemplateNm(request.getCapacityTemplateName())).thenReturn(false);
		capacityValidator.validate(request, OperationConstants.OPERATION_CREATE.getCode());
		assertNotNull(request);
	}
	
	@Test
	void validateInDbForUpdateTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
			
		CreateCapacityTemplateRequest request = new CreateCapacityTemplateRequest();
		List<BusinessDate> date = new ArrayList<>();
		BusinessDate bdate = new BusinessDate();
		bdate.setDate("01/01/2011");
		date.add(bdate);
		List<SlotDetail> detailList = new ArrayList<>();
		SlotDetail detail = new SlotDetail();
		detail.setCapacityCount(1);
		detail.setEndTime("01:01");
		detail.setIsDeletedFlg("N");
		detail.setSlotId(new BigInteger("1"));
		detail.setSlotTypeId("1");
		detail.setStartTime("01:01");
		detailList.add(detail);
		List<SlotChannel> slotList = new ArrayList<>();
		SlotChannel slot = new SlotChannel();
		slot.setChannelId(new BigInteger("1"));
		slot.setIsSelectedFlag("Y");
		slot.setSlotDetails(detailList);
		slotList.add(slot);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateName("name");
		request.setTemplateTypeId(new BigInteger("1"));
		request.setTemplateTypeName("Days");
		request.setBusinessDates(date);
		request.setEffectiveDate("01/01/2011");
		request.setExpiryDate("01/01/2011");
		request.setMonDay("Y");
		request.setTueDay("Y");
		request.setWedDay("Y");
		request.setThuDay("N");
		request.setFriDay("N");
		request.setSatDay("Y");
		request.setSunDay("Y");
		request.setSlotStartTime("01:02");
		request.setSlotEndTime("02:09");
		request.setIsDeletedFlag("N");
		request.setSlotChannels(slotList);
		Mockito.when(capacityManagementService.validateCapacityTemplateNmForCreate(Mockito.anyString(),Mockito.anyString())).thenReturn(false);
		Mockito.when(capacityManagementService.validateCapacityModelBusinessDates(Mockito.any(), Mockito.any())).thenReturn(false);
		capacityValidator.validate(request, OperationConstants.OPERATION_UPDATE.getCode(),"1");
		assertNotNull(request);
	}
	

}
