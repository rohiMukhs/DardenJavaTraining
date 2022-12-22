package com.darden.dash.capacity.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelEntity;
import com.darden.dash.capacity.entity.CapacityChannelAndCombinedChannelPK;
import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.mapper.CapacityChannelMapper;
import com.darden.dash.capacity.model.CapacityChannel;
import com.darden.dash.capacity.model.ChannelInformationRequest;
import com.darden.dash.capacity.model.CombineChannel;
import com.darden.dash.capacity.model.CreateCombineChannelRequest;
import com.darden.dash.capacity.model.ReferenceDatum;
import com.darden.dash.capacity.repository.CapacityChannelAndCombinedChannelRepository;
import com.darden.dash.capacity.repository.CapacityChannelRepo;
import com.darden.dash.capacity.service.impl.CapacityChannelServiceImpl;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.service.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith({ MockitoExtension.class })
class CapacityChannelServiceImplTest {
	
	@InjectMocks
	CapacityChannelServiceImpl capacityChannelServiceImpl;

	@Mock
	private CapacityChannelRepo  capacityChannelRepository;
	
	@Mock
	private CapacityChannelAndCombinedChannelRepository capacityChannelAndCombinedChannelRepository;
	
	@Mock
	private AuditService auditService;
	
	public static List<CapacityChannelEntity> channelList = new ArrayList<>();
	
	public static CapacityChannelEntity channelEntity = new CapacityChannelEntity();
	
	public static CapacityChannelAndCombinedChannelEntity combineChannel = new CapacityChannelAndCombinedChannelEntity();
	
	public static List<CapacityChannelAndCombinedChannelEntity> combinChannelList = new ArrayList<>();
	
	public static CapacityChannelMapper capacityChannelMapper = Mappers.getMapper(CapacityChannelMapper.class);
	
	@BeforeAll
	static void beforeAll() {
		
		RequestContext.setConcept("1");
		RequestContext.setCorrelationId("d64cf01b-ce65-4a57-ac3e-f7fa09e1a87f");
		
		channelEntity.setCapacityChannelId(new BigInteger("1"));
		channelEntity.setCapacityChannelNm("channelnm");
		channelEntity.setConceptId(new BigInteger("1"));
		channelEntity.setCreatedBy("aa");
		channelEntity.setCreatedDatetime(Instant.now());
		channelEntity.setPosName("fname");
		channelEntity.setInterval(4);
		channelEntity.setIsCombinedFlg("N");
		channelEntity.setLastModifiedBy("cc");
		channelEntity.setLastModifiedDatetime(Instant.now());
		channelEntity.setOperationalHoursEndTime(Time.valueOf("10:20:20"));
		channelEntity.setOperationalHoursStartTime(Time.valueOf("10:20:20"));
		
		channelList.add(channelEntity);
		
		CapacityChannelAndCombinedChannelPK id = new CapacityChannelAndCombinedChannelPK();
		id.setCapacityChannelId(new BigInteger("1"));
		id.setCombinedCapacityChannelId(new BigInteger("1"));
		combineChannel.setCapacityChannel1(channelEntity);
		combineChannel.setCapacityChannel2(channelEntity);
		combineChannel.setCreatedBy("a");
		combineChannel.setCreatedDatetime(Instant.now());
		combineChannel.setId(id);
		combineChannel.setLastModifiedBy("ddd");
		combineChannel.setLastModifiedDatetime(Instant.now());
		
		combinChannelList.add(combineChannel);
	}

	@Test
	void testEditChannelInformation() throws JsonProcessingException{
		
		List<ChannelInformationRequest> requestList = new ArrayList<>();
		ChannelInformationRequest request = new ChannelInformationRequest();
		request.setCapacityChannelId(new BigInteger("1"));
		request.setPosName("gname");
		request.setInterval(5);
		request.setOperationHourStartTime("10:10");
		request.setOperationHourEndTime("10:10");
		
		requestList.add(request);
		
		List<CapacityChannelEntity> editedList = new ArrayList<>();
		
		channelEntity.setPosName(request.getPosName());
		channelEntity.setInterval(request.getInterval());
		channelEntity.setOperationalHoursStartTime(Time.valueOf(LocalTime.parse(request.getOperationHourStartTime())));
		channelEntity.setOperationalHoursEndTime(Time.valueOf(LocalTime.parse(request.getOperationHourEndTime())));
		channelEntity.setLastModifiedBy("user");
		channelEntity.setLastModifiedDatetime(Instant.now());
		
		editedList.add(channelEntity);
		
		RequestContext.setConcept("1");
		
		Mockito.when(capacityChannelRepository
				.findAllByCapacityChannelIdInAndConceptId(Mockito.anyList(), Mockito.any()))
				.thenReturn(channelList);
		Mockito.when(capacityChannelRepository.saveAll(Mockito.anyList())).thenReturn(editedList);
		
		List<CapacityChannel> res = capacityChannelServiceImpl.editChannelInformation(requestList, "user");
		
		assertNotNull(res);
		
	}
	
	@Test
	void testFriendlyNmValidation() {
		ChannelInformationRequest request = new ChannelInformationRequest();
		request.setCapacityChannelId(new BigInteger("2"));
		request.setPosName("fname");
		request.setInterval(5);
		request.setOperationHourStartTime("30:30:30");
		request.setOperationHourEndTime("30:30:30");
		RequestContext.setConcept("1");
		Mockito.when(capacityChannelRepository
				.findByPosNameAndConceptId(Mockito.anyString(), Mockito.any()))
				.thenReturn(channelEntity);
		boolean res = capacityChannelServiceImpl.friendlyNmValidation(request);
		assertEquals(true, res);
	}
	
	@Test
	void testAddCombineChannel() throws JsonProcessingException {
		channelEntity.setIsCombinedFlg("Y");
		CapacityChannelEntity channel = new CapacityChannelEntity();
		channel.setCapacityChannelNm("s");
		channel.setCapacityChannelId(new BigInteger("1"));
		channel.setConceptId(new BigInteger("1"));
		channel.setPosName("A");
		channel.setOperationalHoursEndTime(Time.valueOf(LocalTime.parse("00:00")));
		channel.setOperationalHoursStartTime(Time.valueOf(LocalTime.parse("00:00")));
		List<CapacityChannelAndCombinedChannelEntity> list = new ArrayList<>();
		CapacityChannelAndCombinedChannelEntity ch = new CapacityChannelAndCombinedChannelEntity();
		CapacityChannelAndCombinedChannelPK id = new CapacityChannelAndCombinedChannelPK();
		id.setCapacityChannelId(new BigInteger("1"));
		id.setCombinedCapacityChannelId(new BigInteger("1"));
		ch.setLastModifiedBy("aa");
		ch.setId(id);
		ch.setCreatedBy("aaa");
		Set<String> s = new HashSet<>();
		s.add("a");
		s.add("b");
		CreateCombineChannelRequest request = new CreateCombineChannelRequest();
		request.setChannels(s);
		request.setCombinedChannelName("aaa");
		request.setPosName("a");
		request.setEndTime("00:11");
		request.setStartTime("00:00");
		request.setInterval(1);
		Mockito.when(capacityChannelRepository.save(Mockito.any())).thenReturn(channelEntity);
		Mockito.when(capacityChannelRepository
				.findByCapacityChannelNmAndConceptId(Mockito.anyString(), Mockito.any()))
				.thenReturn(Optional.of(channel));
		Mockito.when(capacityChannelAndCombinedChannelRepository.saveAll(Mockito.anyIterable())).thenReturn(list);
		CombineChannel res = capacityChannelServiceImpl.addCombinedChannel(request, "aaa");
		assertNotNull(res);
	}
	
	@Test
	void testValidateChannelNmValidation() {
		Mockito.when(capacityChannelRepository
				.findByCapacityChannelNmAndConceptId(Mockito.anyString(), Mockito.any()))
				.thenReturn(Optional.of(channelEntity));
		boolean res = capacityChannelServiceImpl.validateChannelNmValidation("ch");
		assertEquals(true, res);
	}
	
	@Test
	void testValidateChannelFriendlyNmValidation() {
		Mockito.when(capacityChannelRepository
				.findByPosNameAndConceptId(Mockito.anyString(), Mockito.any()))
				.thenReturn(channelEntity);
		boolean res = capacityChannelServiceImpl.validateChannelFriendlyNmValidation("a");
		assertEquals(true, res);
	}
	
	@Test
	void testGetReferenceData() {
		Mockito.when(capacityChannelRepository.findByConceptId(Mockito.any())).thenReturn(channelList);
		ReferenceDatum res =  capacityChannelServiceImpl.getReferenceData();
		assertNotNull(res);
	}
	
	@Test
	void testValidationOfCombinationOfChannel() {
		Mockito.when(capacityChannelRepository.findByConceptId(Mockito.any())).thenReturn(channelList);
		Mockito.when(capacityChannelAndCombinedChannelRepository.findByCapacityChannel2(Mockito.any())).thenReturn(combinChannelList);
		Set<String> name = new HashSet<>();
		name.add("channelnm");
		Set<Integer> res = capacityChannelServiceImpl.validateBaseChannelCombindation(name);
		assertNotNull(res);
	}
}
