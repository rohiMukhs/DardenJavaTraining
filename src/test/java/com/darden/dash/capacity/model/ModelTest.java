package com.darden.dash.capacity.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
}
