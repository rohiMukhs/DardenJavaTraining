package com.darden.dash.capacity.model;

import static org.junit.Assert.assertNotNull;

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
	    model.setFirendlyName("fname");
	    model.setInterval("1");
	    model.setIsCombinedFlg("N");
	    model.setOperationalHoursEndTime("01:02:03");
	    model.setOperationalHoursStartTime("01:02:03");
	    model.setCombinedChannels(channelList);
	    model.getCapacityChannelId();
	    model.getCapacityChannelName();
	    model.getCombinedChannels();
	    model.getFirendlyName();
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
		model.setChannelId("1");
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
		model.setCapacityTemplate(response);
		model.setCorrelationId("212121");
		model.setStatus(200);
		model.setTitle("aaa");
		model.getCapacityTemplate();
		model.getCorrelationId();
		model.getStatus();
		model.getTitle();
		model.build("Aaa", 200);
		model.canEqual(model);
		model.equals(model);
		assertNotNull(model);
	}
	
	@Test
	void testEditChannelResponse() {
		List<CapacityChannel> response = new ArrayList<>();
		EditChannelResponse model = new EditChannelResponse(response);
		model.setChannels(response);
		model.setCorrelationId("aaa");
		model.setStatus(200);
		model.setTitle("aa");
		model.getChannels();
		model.getCorrelationId();
		model.getStatus();
		model.getTitle();
		model.build("aa", 200);
		model.canEqual(model);
		model.equals(model);
		assertNotNull(model);
	}
	
}
