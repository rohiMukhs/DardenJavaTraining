package com.darden.dash.capacity.validator.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.model.ChannelListRequest;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.service.CapacityChannelService;
import com.darden.dash.capacity.validation.ChannelValidator;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.enums.OperationConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith({ MockitoExtension.class })
class ChannelValidatorTest {
	
	@InjectMocks
	private ChannelValidator channelValidator;
	
	@Mock
	private CapacityChannelService service;
	
	@Mock
	private JwtUtils jwtUtils;
	
	@Mock
	private ApplicationErrors applicationErrors;
	
	@Test
	void testValidateInDBforUpdate() throws JsonProcessingException{
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		ChannelListRequest requestList = new ChannelListRequest();
		List<ChannelInformationRequest> list = new ArrayList<>();
		ChannelInformationRequest r1 = new ChannelInformationRequest();
		r1.setCapacityChannelId(new BigInteger("1"));
		r1.setFriendlyName("aa");
		r1.setInterval(5);
		r1.setOperationHourEndTime("00:05");
		r1.setOperationHourStartTime("00:10");
		list.add(r1);
		ChannelInformationRequest r2 = new ChannelInformationRequest();
		r2.setCapacityChannelId(new BigInteger("2"));
		r2.setFriendlyName("bb");
		r2.setInterval(10);
		r2.setOperationHourEndTime("00:10");
		r2.setOperationHourStartTime("00:20");
		list.add(r2);
		requestList.setChannels(list);
		Mockito.when(service.friendlyNmValidation(Mockito.any())).thenReturn(false);
		channelValidator.validate(requestList, OperationConstants.OPERATION_UPDATE.getCode());
	}
	
	@Test
	void testValidateInDBforCreate() throws JsonProcessingException {
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		CreateCombineChannelRequest request = new CreateCombineChannelRequest();
		Set<String> s = new HashSet<>();
		s.add("a");
		s.add("b");
		request.setChannels(s);
		request.setCombinedChannelName("aaa");
		request.setFriendlyName("a");
		request.setEndTime("00:11");
		request.setStartTime("00:00");
		request.setInterval(9);
		Mockito.when(service.validateChannelFriendlyNmValidation(Mockito.anyString())).thenReturn(false);
		Mockito.when(service.validateChannelNmValidation(Mockito.anyString())).thenReturn(false);
		channelValidator.validate(request, OperationConstants.OPERATION_CREATE.getCode());
	}

}
