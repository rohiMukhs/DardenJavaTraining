package com.darden.dash.capacity.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({ MockitoExtension.class })
class ModelTest {
	
	@Test
	void testCapacityChannel() {
		List<Channel> channelList = new ArrayList<>();
		Channel channel = new Channel();
		channel.setCapacityChannelId(new BigInteger("1"));
		channel.setCapacityChannelName("Y");
		channel.setIsSelectedFlag("Y");
		channel.getCapacityChannelId();
		channel.getCapacityChannelName();
		channel.getIsSelectedFlag();
		channelList.add(channel);
		CapacityChannel model = new CapacityChannel();
		model.setCapacityChannelId(new BigInteger("1"));
	    model.setCapacityChannelName("name");
	    model.setPosName("fname");
	    model.setInterval("1");
	    model.setIsCombinedFlg("N");
	    model.setOperationalHoursEndTime("01:02:03");
	    model.setOperationalHoursStartTime("01:02:03");
	    model.setCombinedChannels(channelList);
	    model.getCapacityChannelId();
	    model.getCapacityChannelName();
	    model.getCombinedChannels();
	    model.getPosName();
	    model.getInterval();
	    model.getIsCombinedFlg();
	    model.getOperationalHoursEndTime();
	    model.getOperationalHoursStartTime();
	    assertNotNull(model);
	}
	
	@Test
	void testSlotChannel() {
		List<SlotDetail> slotList = new ArrayList<>();
		SlotChannel model = new SlotChannel();
		model.setChannelId(new BigInteger("1"));
		model.setChannelName("name");
		model.setSlotDetails(slotList);
		model.getChannelId();
		model.getChannelName();
		model.getSlotDetails();
		assertNotNull(model);
	}
	
	@Test
	void testCreateCapacityResponse() {
		CreateTemplateResponse response = new CreateTemplateResponse();		
		CreateCapacityTemplateResponse model = new CreateCapacityTemplateResponse(response);
		CreateCapacityTemplateResponse model1 = new CreateCapacityTemplateResponse(response);
		model.setCapacityTemplate(response);
		model1.setCapacityTemplate(model.getCapacityTemplate());
		model.setCorrelationId("212121");
		model1.setCorrelationId(model.getCorrelationId());
		model.setStatus(200);
		model1.setStatus(model.getStatus());
		model.setTitle("aaa");
		model1.setTitle(model.getTitle());
		model.build("Aaa", 200);
		model1.build("Aaa", 200);
		model.canEqual(model);
		model.equals(model);
		model1.getCapacityTemplate();
		model1.getCorrelationId();
		model1.getStatus();
		model1.getTitle();
		assertNotNull(model);
		assertEquals(model, model1);
		assertEquals(model.hashCode(), model1.hashCode());
	}
	
	@Test
	void testEditChannelResponse() {
		List<CapacityChannel> response = new ArrayList<>();
		EditChannelResponse model = new EditChannelResponse(response);
		EditChannelResponse model1 = new EditChannelResponse(response);
		model.setChannels(model.getChannels());
		model1.setChannels(response);
		model.setCorrelationId("aaa");
		model1.setCorrelationId(model.getCorrelationId());
		model.setStatus(200);
		model1.setStatus(model.getStatus());
		model.setTitle("aa");
		model1.setTitle(model.getTitle());
		model.build("aa", 200);
		model1.build("aa", 200);
		model.canEqual(model);
		model.equals(model);
		assertNotEquals(null, model);
		assertNotNull(model);
		assertEquals(model, model1);
		assertEquals(model.hashCode(), model1.hashCode());
	}
	
	@Test
	void covreageForHashCodeAndEquals() {
		CapacityTemplate c1 = new CapacityTemplate();
		CapacityTemplate c2 = new CapacityTemplate();
		c1.equals(null);
		assertNotEquals(null, c1);
		assertEquals(c1, c2);
		assertEquals(c1.hashCode(), c2.hashCode());
		
		CapacityResponse r1 = new CapacityResponse();
		CapacityResponse r2 = new CapacityResponse();
		r1.canEqual(r2);
		assertNotEquals(null, r1);
		assertEquals(r1, r2);
		assertEquals(r1.hashCode(), r2.hashCode());
		
		GetCapacityModelResponse m1 = new GetCapacityModelResponse();
		GetCapacityModelResponse m2 = new GetCapacityModelResponse();
		m1.canEqual(m2);
		assertNotEquals(null, m1);
		assertEquals(m1, m2);
		assertEquals(m1.hashCode(), m2.hashCode());
		
		CombineChannel cha = new CombineChannel();
		CreateCombineChannelResponse cc1 = new CreateCombineChannelResponse(cha);
		CreateCombineChannelResponse cc2 = new CreateCombineChannelResponse(cha);
		cc1.canEqual(cc2);
		assertNotEquals(null, cc1);
		assertEquals(cc1, cc2);
		assertEquals(cc1.hashCode(), cc2.hashCode());
	}
	
	@Test
	void OrderTemplateTest() {
		List<OrderList> orderList1 = new ArrayList<>();
		OrderList orderList = new OrderList();
		orderList.setListId(BigInteger.ONE);
		orderList.setListNm("name");
		orderList.setListType("user");
		orderList1.add(orderList);
		orderList.getListId();
		orderList.getListNm();
		orderList.getListType();
		OrderTemplate OrderTemplate = new OrderTemplate();
		OrderTemplate.setConceptId(BigInteger.ONE);
		OrderTemplate.setId(BigInteger.ONE);
		OrderTemplate.setOrderTemplateName("name");
		OrderTemplate.setOrderLists(orderList1);
		OrderTemplate.getConceptId();
		OrderTemplate.getId();
		OrderTemplate.getOrderLists();
		OrderTemplate.getOrderTemplateName();
		assertNotNull(OrderTemplate);
		
	}

    @Test
	void OrderTemplateResponseTest() {
		List<OrderList> orderList1 = new ArrayList<>();
		OrderList orderList = new OrderList();
		orderList.setListId(BigInteger.ONE);
		orderList.setListNm("name");
		orderList.setListType("user");
		orderList1.add(orderList);
		List<OrderTemplate> orderTemplateList= new ArrayList<>();
		OrderTemplate OrderTemplate = new OrderTemplate();
		OrderTemplate.setConceptId(BigInteger.ONE);
		OrderTemplate.setId(BigInteger.ONE);
		OrderTemplate.setOrderTemplateName("name");
		OrderTemplate.setOrderLists(orderList1);
		OrderTemplateResponse orderTemplateResponse = new OrderTemplateResponse();
		orderTemplateResponse.setCorrelationId("abc");
		orderTemplateResponse.setOrderTemplate(orderTemplateList);
		orderTemplateResponse.setStatus(new BigInteger("200"));
		orderTemplateResponse.setTitle("name");
		orderTemplateResponse.getCorrelationId();
		orderTemplateResponse.getOrderTemplate();
		orderTemplateResponse.getStatus();
		orderTemplateResponse.getTitle();
		assertNotNull(orderTemplateResponse);
		
	}
    
    @Test
	void editLocationDetailResponseTest() {
    	EditLocationDetailResponse editLocDetResponse = new EditLocationDetailResponse();
    	editLocDetResponse.setLocationId(1);
    	editLocDetResponse.setRestaurantNumber(1);
    	editLocDetResponse.setConceptId(1);
    	editLocDetResponse.setDivisionId(1);
    	editLocDetResponse.setRegionId(1);
    	editLocDetResponse.setLocationDesc("desc");
    	editLocDetResponse.setAddressStreet("street");
    	editLocDetResponse.setAddressState("state");
    	editLocDetResponse.setAddressCity("city");
    	editLocDetResponse.setPhone("9793748473");
    	editLocDetResponse.setFax("23");
    	editLocDetResponse.setZipCode("232");
    	editLocDetResponse.setEmail("jack@.com");
    	editLocDetResponse.setCreatedBy("ak");
    	editLocDetResponse.setCreatedDateTime(Instant.now());
    	editLocDetResponse.setLastModifiedBy("ak");
    	editLocDetResponse.setLastModifiedDatetime(Instant.now());
    	editLocDetResponse.getAddressCity();
    	editLocDetResponse.getAddressState();
    	editLocDetResponse.getAddressStreet();
    	editLocDetResponse.getConceptId();
    	editLocDetResponse.getCreatedBy();
    	editLocDetResponse.getCreatedDateTime();
    	editLocDetResponse.getDivisionId();
    	editLocDetResponse.getEmail();
    	editLocDetResponse.getFax();
    	editLocDetResponse.getLastModifiedBy();
    	editLocDetResponse.getLastModifiedDatetime();
    	editLocDetResponse.getLocationDesc();
    	editLocDetResponse.getLocationId();
    	editLocDetResponse.getPhone();
    	editLocDetResponse.getRegionId();
    	editLocDetResponse.getRestaurantNumber();
    	editLocDetResponse.getZipCode();
    	assertNotNull(editLocDetResponse);
	}
    
    @Test
    void CapacityTemplateNamesTest() {
    	CapacityTemplateNames capacityTemplateNames = new CapacityTemplateNames();
    	capacityTemplateNames.setTemplateId("1");
    	capacityTemplateNames.setTemplateName("name");
    	capacityTemplateNames.getTemplateId();
    	capacityTemplateNames.getTemplateName();
    	assertNotNull(capacityTemplateNames);
    }
    
    @Test
    void ListOfCapacityTemplateIdsTest() {
    	ListOfCapacityTemplateIds listOfCapacityTemplateIds = new ListOfCapacityTemplateIds();
    	Set<BigInteger> ids = new HashSet<>();
    	ids.add(BigInteger.ONE);
    	ids.add(BigInteger.TEN);
    	listOfCapacityTemplateIds.setTemplateIdList(ids);
    	listOfCapacityTemplateIds.getTemplateIdList();
    	assertNotNull(listOfCapacityTemplateIds);
    }
    
    @Test
    void deleteTemplateModelRequestTest() {
    	DeleteModelTemplateRequest deleteModelTemplateRequest = new DeleteModelTemplateRequest("1","Y");
    	deleteModelTemplateRequest.getModelId();
    	deleteModelTemplateRequest.getDeleteConfirmed();
    	assertNotNull(deleteModelTemplateRequest);
    }
    
    @Test
    void ListOfModelResponseTest() {
    	CapacityModel capacityModel = new CapacityModel();
    	Set<BigInteger> ids = new HashSet<>();
    	ids.add(BigInteger.ONE);
    	ids.add(BigInteger.TEN);
    	capacityModel.setCapacityModelId(BigInteger.ONE);
    	capacityModel.setCapacityModelName("name");
    	
    	BusinessDate businessDate = new BusinessDate();
    	businessDate.setDate(null);
    	List<BusinessDate> businessDateList = new ArrayList<>();
    	businessDateList.add(businessDate);
    	CapacityTemplate capacityTemplate = new CapacityTemplate();
    	capacityTemplate.setBusinessDate(businessDateList);
    	capacityTemplate.setCapacityTemplateId(null);
    	capacityTemplate.setCapacityTemplateType(null);
    	capacityTemplate.setChannels(null);
    	capacityTemplate.setCreatedBy(null);
    	capacityTemplate.setCreatedDatetime(null);
    	capacityTemplate.setEffectiveDate(null);
    	capacityTemplate.setExpiryDate(null);
    	capacityTemplate.setFriDay(null);
    	capacityTemplate.setFriDay(null);
    	capacityTemplate.setLastModifiedBy(null);
    	capacityTemplate.setLastModifiedDatetime(null);
    	capacityTemplate.setMonDay(null);
    	capacityTemplate.setSatDay(null);
    	capacityTemplate.setSlotChannels(null);
    	capacityTemplate.setSlotEndTime(null);
    	capacityTemplate.setSlotStartTime(null);
    	capacityTemplate.setSunDay(null);
    	capacityTemplate.setTemplateName(null);
    	capacityTemplate.setThuDay(null);
    	capacityTemplate.setTueDay(null);
    	capacityTemplate.setWedDay(null);
    	
    	capacityTemplate.getBusinessDate();
    	capacityTemplate.getCapacityTemplateId();
    	capacityTemplate.getCapacityTemplateType();
    	capacityTemplate.getChannels();
    	capacityTemplate.getCreatedBy();
    	capacityTemplate.getCreatedDatetime();
    	capacityTemplate.getEffectiveDate();
    	capacityTemplate.getEffectiveDate();
    	capacityTemplate.getExpiryDate();
    	capacityTemplate.getFriDay();
    	capacityTemplate.getLastModifiedBy();
    	capacityTemplate.getLastModifiedDatetime();
    	capacityTemplate.getMonDay();
    	capacityTemplate.getSatDay();
    	capacityTemplate.getSlotChannels();
    	capacityTemplate.getSlotEndTime();
    	capacityTemplate.getSlotStartTime();
    	capacityTemplate.getSunDay();
    	capacityTemplate.getTemplateName();
    	capacityTemplate.getThuDay();
    	capacityTemplate.getTueDay();
    	capacityTemplate.getWedDay();
    	
    	List<CapacityTemplate> capacityTemplateList = new ArrayList<>();
    	capacityModel.setCapacityTemplateList(capacityTemplateList);
    	capacityModel.setCreatedBy("Ak");
    	capacityModel.setCreatedDateTime(null);
    	capacityModel.setIsDeletedFlg("y");
    	capacityModel.setLastModifiedBy("Ak");
    	capacityModel.setLastModifiedDateTime(null);
    	capacityModel.setRestaurants(ids);
    	
    	capacityModel.getCapacityModelId();
    	capacityModel.getCapacityModelName();
    	capacityModel.getCapacityTemplateList();
    	capacityModel.getCreatedBy();
    	capacityModel.getCreatedDateTime();
    	capacityModel.getIsDeletedFlg();
    	capacityModel.getLastModifiedBy();
    	capacityModel.getLastModifiedDateTime();
    	capacityModel.getRestaurants();
    	
    	List<CapacityModel> capacityModelList = new ArrayList<>();
    	capacityModelList.add(capacityModel);
    	ListOfModelResponse listOfModelResponse = new ListOfModelResponse(capacityModelList);
    	listOfModelResponse.getCorrelationId();
    	listOfModelResponse.getModelList();
    	listOfModelResponse.getStatus();
    	listOfModelResponse.getTitle();
    	assertNotNull(listOfModelResponse);
    }
}