package com.darden.dash.capacity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.client.LocationClient;
import com.darden.dash.capacity.client.OrderClient;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
import com.darden.dash.capacity.entity.CapacityModelAndLocationEntity;
import com.darden.dash.capacity.entity.CapacityModelAndLocationPK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDatePK;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateTypeEntity;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityTemplateModel;
import com.darden.dash.capacity.model.ConceptForCache;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.model.OrderList;
import com.darden.dash.capacity.model.OrderTemplate;
import com.darden.dash.capacity.model.Region;
import com.darden.dash.capacity.model.RestaurantsAssigned;
import com.darden.dash.capacity.model.TemplatesAssigned;
import com.darden.dash.capacity.repository.CapacityModelAndCapacityTemplateRepository;
import com.darden.dash.capacity.repository.CapacityModelAndLocationRepository;
import com.darden.dash.capacity.repository.CapacityModelRepository;
import com.darden.dash.capacity.repository.CapacityTemplateAndBusinessDateRepository;
import com.darden.dash.capacity.repository.CapacityTemplateRepo;
import com.darden.dash.capacity.service.impl.CapacityTemplateModelServiceImpl;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.client.service.ConceptClient;
import com.darden.dash.common.model.Concept;
import com.darden.dash.common.service.AuditService;
import com.darden.dash.common.util.DateUtil;
import com.darden.dash.common.util.GlobalDataCall;
import com.darden.dash.common.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith({ MockitoExtension.class })
class CapacityModelServiceImplTest {
	
	@InjectMocks
	CapacityTemplateModelServiceImpl capacityTemplateModelServiceImpl;
	
	@Mock
	private CapacityModelRepository capacityModelRepository;
	
	@Mock
	private LocationClient locationClient;
	
	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private CapacityModelAndCapacityTemplateRepository capacityModelAndCapacityTemplateRepo;

	@Mock
	private CapacityTemplateAndBusinessDateRepository capacityTemplateAndBusinessDateRepository;

	@Mock
	private CapacityTemplateRepo capacityTemplateRepo;

	@Mock
	private CapacityModelAndLocationRepository capacityModelAndLocationRepo;
	
	@Mock
	private ConceptClient conceptClient;

	@Mock
	private OrderClient orderClient;
	
	@Mock
	private AuditService auditService;
	
	@Mock
	private GlobalDataCall globalDataCall;
	
	public static CapacityModelEntity capacityModelEntity = new CapacityModelEntity();
	
	public static List<CapacityModelEntity> modelEntityList = new ArrayList<>();
	
	public static CapacityModelAndCapacityTemplateEntity capacityModelAndCapacityTemplateEntity = new CapacityModelAndCapacityTemplateEntity();
	
	public static List<CapacityModelAndCapacityTemplateEntity> modelAndTemplateList = new ArrayList<>();
	
	public static CapacityModelAndLocationEntity capacityModelAndLocationEntity = new CapacityModelAndLocationEntity();
	
	public static List<CapacityModelAndLocationEntity> modelAndLocationList = new ArrayList<>();
	
	public static CapacityModelRequest capacityModelRequest = new CapacityModelRequest();
	
	@BeforeAll
	static void beforeAll() {
		capacityModelEntity.setCapacityModelId(new BigInteger("1"));
		capacityModelEntity.setCapacityModelNm("name");
		capacityModelEntity.setConceptId(new BigInteger("1"));
		capacityModelEntity.setCreatedBy("aaa");
		capacityModelEntity.setCreatedDatetime(Instant.now());
		capacityModelEntity.setLastModifiedBy("zzz");
		capacityModelEntity.setLastModifiedDatetime(Instant.now());
		
		modelEntityList.add(capacityModelEntity);
		
		CapacityTemplateEntity capacityTemplateEntity = new CapacityTemplateEntity();
		capacityTemplateEntity.setCapacityTemplateId(BigInteger.valueOf(1));
		capacityTemplateEntity.setCapacityTemplateNm("Lorum Ipsum");
		
		CapacityModelAndCapacityTemplatePK id = new CapacityModelAndCapacityTemplatePK();
		id.setCapacityModelId(new BigInteger("1"));
		id.setCapacityTemplateId(new BigInteger("1"));
		capacityModelAndCapacityTemplateEntity.setCapacityModel(capacityModelEntity);
		capacityModelAndCapacityTemplateEntity.setCreatedBy("aaa");
		capacityModelAndCapacityTemplateEntity.setCreatedDatetime(Instant.now());
		capacityModelAndCapacityTemplateEntity.setId(id);
		capacityModelAndCapacityTemplateEntity.setLastModifiedBy("ccc");
		capacityModelAndCapacityTemplateEntity.setLastModifiedDatetime(Instant.now());
		capacityModelAndCapacityTemplateEntity.setCapacityTemplate(capacityTemplateEntity);
		
		modelAndTemplateList.add(capacityModelAndCapacityTemplateEntity);
		
		CapacityModelAndLocationPK capacityModelAndLocationPK = new CapacityModelAndLocationPK();
		capacityModelAndLocationPK.setCapacityModelId(new BigInteger("1"));
		capacityModelAndLocationPK.setLocationId(new BigInteger("1"));
		capacityModelAndLocationEntity.setCapacityModel(capacityModelEntity);
		capacityModelAndLocationEntity.setCreatedBy("Aa");
		capacityModelAndLocationEntity.setCreatedDatetime(Instant.now());
		capacityModelAndLocationEntity.setId(capacityModelAndLocationPK);
		capacityModelAndLocationEntity.setLastModifiedBy("zz");
		capacityModelAndLocationEntity.setLastModifiedDatetime(Instant.now());
		
		capacityModelEntity.setCapacityModelAndCapacityTemplates(modelAndTemplateList);
		capacityModelEntity.setCapacityModelAndLocations(modelAndLocationList);
		
		modelAndLocationList.add(capacityModelAndLocationEntity);
		
		capacityModelRequest.setTemplateModelName("abc");
		List<TemplatesAssigned> templatesAssignedList = new ArrayList<>();
		TemplatesAssigned templatesAssigned = new TemplatesAssigned();
		templatesAssigned.setTemplateId("101");
		templatesAssigned.setTemplateName("Test101");
		templatesAssignedList.add(templatesAssigned);
		capacityModelRequest.setTemplatesAssigned(templatesAssignedList);
		List<RestaurantsAssigned> restaurantsAssignedList = new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned = new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1111");
		restaurantsAssignedList.add(restaurantsAssigned);
		capacityModelRequest.setRestaurantsAssigned(restaurantsAssignedList);
	}
	
	@Test
	void getModel() {
		RequestContext.setConcept("1");
		List<Locations> locations = new ArrayList<>();
		Locations location = new Locations();
		location.setAddressState("address");
		location.setLastModifiedDateTime(Instant.now());
		location.setLocationDescription("desc");
		location.setLocationId(new BigInteger("1"));
		location.setRestaurantNumber(new BigInteger("1111"));
		Region region  = new Region();
		region.setRegionId(1);
		region.setRegionName("region");
		location.setRegion(region);
		locations.add(location);
		List<OrderList> orderList1 = new ArrayList<>();
		OrderList orderList = new OrderList();
		orderList.setListId(BigInteger.ONE);
		orderList.setListNm("name");
		orderList.setListType("CAPACITY MODEL LIST");
		orderList1.add(orderList);
		List<OrderTemplate> OrderTemplateList = new ArrayList<>();
		OrderTemplate OrderTemplate = new OrderTemplate();
		OrderTemplate.setConceptId(BigInteger.ONE);
		OrderTemplate.setId(BigInteger.ONE);
		OrderTemplate.setOrderTemplateName("name");
		OrderTemplate.setOrderLists(orderList1);
		OrderTemplate.setLocations(locations);
		OrderTemplateList.add(OrderTemplate);
		Mockito.when(orderClient.getAllOrderTemplates()).thenReturn(OrderTemplateList);
		Mockito.when(capacityModelRepository.findByConceptId(Mockito.any())).thenReturn(modelEntityList);
		Mockito.when(locationClient.getAllRestaurants()).thenReturn(locations);
		List<CapacityModel> res = capacityTemplateModelServiceImpl.getAllCapacityModels("1");
		assertNotNull(res);
	}
	
	@Test
	void createModelTemplateTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		
		List<Locations> locations = new ArrayList<>();
		Locations location = new Locations();
		location.setAddressState("address");
		location.setLastModifiedDateTime(Instant.now());
		location.setLocationDescription("desc");
		location.setLocationId(new BigInteger("1"));
		location.setRestaurantNumber(new BigInteger("1111"));
		Region region  = new Region();
		region.setRegionId(1);
		region.setRegionName("region");
		location.setRegion(region);
		locations.add(location);
		
		Mockito.when(jwtUtils.findUserDetail(Mockito.anyString())).thenReturn("TestUser");
		CapacityModelRequest capacityModelRequest = new CapacityModelRequest();
		CapacityModelRequest request = new CapacityModelRequest();
		request.setTemplateModelName("abc");
		List<TemplatesAssigned> TemplatesAssignedList = new ArrayList<>();
		TemplatesAssigned templatesAssigned = new TemplatesAssigned();
		templatesAssigned.setTemplateId("101");
		templatesAssigned.setTemplateName("Test101");
		TemplatesAssignedList.add(templatesAssigned);
		capacityModelRequest.setTemplatesAssigned(TemplatesAssignedList);
		List<RestaurantsAssigned> restaurantsAssignedList = new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned = new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1");
		restaurantsAssignedList.add(restaurantsAssigned);
		capacityModelRequest.setRestaurantsAssigned(restaurantsAssignedList);

		CapacityTemplateModel capacityTemplateModelResponse = new CapacityTemplateModel();
		capacityTemplateModelResponse.setCreatedBy("user");
		capacityTemplateModelResponse.setCreatedDateTime(Instant.now());
		capacityTemplateModelResponse.setLastModifiedBy("user");
		capacityTemplateModelResponse.setLastModifiedDateTime(Instant.now());
		List<TemplatesAssigned> TemplatesAssignedList1 = new ArrayList<>();
		TemplatesAssigned templatesAssigned1 = new TemplatesAssigned();
		templatesAssigned1.setTemplateId("101");
		templatesAssigned1.setTemplateName("Test101");
		TemplatesAssignedList1.add(templatesAssigned1);
		List<RestaurantsAssigned> restaurantsAssignedList1 = new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned1 = new RestaurantsAssigned();
		restaurantsAssigned1.setLocationId("1");
		restaurantsAssignedList1.add(restaurantsAssigned1);
		CapacityModelEntity capacityModelEntityRes = new CapacityModelEntity();
		capacityModelEntityRes.setCapacityModelId(BigInteger.ONE);
		CapacityTemplateEntity capacityTemplateEntityRes = new CapacityTemplateEntity();
		capacityTemplateEntityRes.setCapacityTemplateId(BigInteger.ONE);
		capacityTemplateEntityRes.setCapacityTemplateNm("TestTemplate");
		capacityTemplateEntityRes.setCapacityTemplateId(BigInteger.ONE);
		capacityTemplateEntityRes.setCapacityTemplateNm("Test2");
		capacityModelEntityRes.setCapacityModelId(BigInteger.ONE);
		CapacityModelEntity capacityModelEntityResponse = new CapacityModelEntity();
		capacityModelEntityResponse.setCapacityModelNm("Test123");
		capacityModelEntityResponse.setCreatedBy("user");
		capacityModelEntityResponse.setCreatedDatetime(Instant.now());
		capacityModelEntityResponse.setLastModifiedBy("user");
		capacityModelEntityResponse.setLastModifiedDatetime(Instant.now());
		List<CapacityModelAndCapacityTemplateEntity> list = new ArrayList<>();
		CapacityModelAndCapacityTemplateEntity capacityModelAndCapacityTemplateEntity = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK capacityModelAndCapacityTemplatePK = new CapacityModelAndCapacityTemplatePK();
		capacityModelAndCapacityTemplatePK.setCapacityModelId(BigInteger.ONE);
		capacityModelAndCapacityTemplatePK.setCapacityTemplateId(BigInteger.ONE);
		capacityModelAndCapacityTemplateEntity.setId(capacityModelAndCapacityTemplatePK);
		list.add(capacityModelAndCapacityTemplateEntity);
		capacityModelEntityResponse.setCapacityModelAndCapacityTemplates(list);
		List<CapacityModelAndLocationEntity> listNew = new ArrayList<>();
		CapacityModelAndLocationEntity capacityModelAndLocationEntity = new CapacityModelAndLocationEntity();
		CapacityModelAndLocationPK CapacityModelAndLocationPK = new CapacityModelAndLocationPK();
		CapacityModelAndLocationPK.setCapacityModelId(BigInteger.ONE);
		CapacityModelAndLocationPK.setLocationId(BigInteger.ONE);
		capacityModelAndLocationEntity.setId(CapacityModelAndLocationPK);
		listNew.add(capacityModelAndLocationEntity);
		capacityModelEntityResponse.setCapacityModelAndLocations(listNew);
		Mockito.lenient().when(capacityTemplateRepo.findById(Mockito.any()))
				.thenReturn(Optional.of(capacityTemplateEntityRes));
		Mockito.lenient().when(capacityModelRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(capacityModelEntityResponse));
		Mockito.lenient().when(capacityModelAndLocationRepo.saveAll(Mockito.any())).thenReturn(Collections.emptyList());
		Mockito.lenient().when(capacityModelAndCapacityTemplateRepo.saveAll(Mockito.any()))
				.thenReturn(Collections.emptyList());
		Mockito.lenient().when(capacityTemplateRepo.findAll())
				.thenReturn(Collections.singletonList(capacityTemplateEntityRes));
		Mockito.lenient().when(capacityTemplateRepo.findAllById(Mockito.anyList()))
				.thenReturn(Collections.singletonList(capacityTemplateEntityRes));
		Mockito.lenient().when(capacityModelRepository.save(Mockito.any())).thenReturn(capacityModelEntityRes);
		Mockito.when(locationClient.getAllRestaurants()).thenReturn(locations);
		CapacityTemplateModel capacityTemplateModel=capacityTemplateModelServiceImpl.createCapacityModel(capacityModelRequest, CapacityServiceImplTest.ACCESS_TOKEN);
		assertNotNull(capacityTemplateModel);

	}
	
	@Test
	void updateModelTemplateTest() throws JsonProcessingException {
		RequestContext.setConcept("1");
		Mockito.lenient().when(jwtUtils.findUserDetail(Mockito.anyString())).thenReturn("TestUser");
		CapacityModelRequest capacityModelRequest = new CapacityModelRequest();
		CapacityModelRequest request = new CapacityModelRequest();
		request.setTemplateModelName("abc");
		List<TemplatesAssigned> TemplatesAssignedList = new ArrayList<>();
		TemplatesAssigned templatesAssigned = new TemplatesAssigned();
		templatesAssigned.setTemplateId("101");
		templatesAssigned.setTemplateName("Test101");
		TemplatesAssignedList.add(templatesAssigned);
		capacityModelRequest.setTemplatesAssigned(TemplatesAssignedList);
		List<RestaurantsAssigned> restaurantsAssignedList = new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned = new RestaurantsAssigned();
		restaurantsAssigned.setLocationId("1");
		restaurantsAssignedList.add(restaurantsAssigned);
		capacityModelRequest.setRestaurantsAssigned(restaurantsAssignedList);
		
		List<Locations> locations = new ArrayList<>();
		Locations location = new Locations();
		location.setAddressState("address");
		location.setLastModifiedDateTime(Instant.now());
		location.setLocationDescription("desc");
		location.setLocationId(new BigInteger("1"));
		location.setRestaurantNumber(new BigInteger("1111"));
		Region region  = new Region();
		region.setRegionId(1);
		region.setRegionName("region");
		location.setRegion(region);
		locations.add(location);

		CapacityTemplateModel capacityTemplateModelResponse = new CapacityTemplateModel();
		capacityTemplateModelResponse.setCreatedBy("user");
		capacityTemplateModelResponse.setCreatedDateTime(Instant.now());
		capacityTemplateModelResponse.setLastModifiedBy("user");
		capacityTemplateModelResponse.setLastModifiedDateTime(Instant.now());
		List<TemplatesAssigned> TemplatesAssignedList1 = new ArrayList<>();
		TemplatesAssigned templatesAssigned1 = new TemplatesAssigned();
		templatesAssigned1.setTemplateId("101");
		templatesAssigned1.setTemplateName("Test101");
		TemplatesAssignedList1.add(templatesAssigned1);
		List<RestaurantsAssigned> restaurantsAssignedList1 = new ArrayList<>();
		RestaurantsAssigned restaurantsAssigned1 = new RestaurantsAssigned();
		restaurantsAssigned1.setLocationId("1");
		restaurantsAssignedList1.add(restaurantsAssigned1);
		CapacityModelEntity capacityModelEntityRes = new CapacityModelEntity();
		capacityModelEntityRes.setCapacityModelId(BigInteger.ONE);
		CapacityTemplateEntity capacityTemplateEntityRes = new CapacityTemplateEntity();
		capacityTemplateEntityRes.setCapacityTemplateId(BigInteger.ONE);
		capacityTemplateEntityRes.setCapacityTemplateNm("TestTemplate");
		capacityTemplateEntityRes.setCapacityTemplateId(BigInteger.ONE);
		capacityTemplateEntityRes.setCapacityTemplateNm("Test2");
		capacityModelEntityRes.setCapacityModelId(BigInteger.ONE);
		CapacityModelEntity capacityModelEntityResponse = new CapacityModelEntity();
		capacityModelEntityResponse.setCapacityModelNm("Test123");
		capacityModelEntityResponse.setCreatedBy("user");
		capacityModelEntityResponse.setCreatedDatetime(Instant.now());
		capacityModelEntityResponse.setLastModifiedBy("user");
		capacityModelEntityResponse.setLastModifiedDatetime(Instant.now());
		List<CapacityModelAndCapacityTemplateEntity> list = new ArrayList<>();
		CapacityModelAndCapacityTemplateEntity capacityModelAndCapacityTemplateEntity = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK capacityModelAndCapacityTemplatePK = new CapacityModelAndCapacityTemplatePK();
		capacityModelAndCapacityTemplatePK.setCapacityModelId(BigInteger.ONE);
		capacityModelAndCapacityTemplatePK.setCapacityTemplateId(BigInteger.ONE);
		capacityModelAndCapacityTemplateEntity.setId(capacityModelAndCapacityTemplatePK);
		list.add(capacityModelAndCapacityTemplateEntity);
		capacityModelEntityResponse.setCapacityModelAndCapacityTemplates(list);
		List<CapacityModelAndLocationEntity> listNew = new ArrayList<>();
		CapacityModelAndLocationEntity capacityModelAndLocationEntity = new CapacityModelAndLocationEntity();
		CapacityModelAndLocationPK CapacityModelAndLocationPK = new CapacityModelAndLocationPK();
		CapacityModelAndLocationPK.setCapacityModelId(BigInteger.ONE);
		CapacityModelAndLocationPK.setLocationId(BigInteger.ONE);
		capacityModelAndLocationEntity.setId(CapacityModelAndLocationPK);
		listNew.add(capacityModelAndLocationEntity);
		capacityModelEntityResponse.setCapacityModelAndLocations(listNew);
		Mockito.lenient().when(capacityTemplateRepo.findById(Mockito.any()))
				.thenReturn(Optional.of(capacityTemplateEntityRes));
		Mockito.lenient().when(capacityModelRepository.findByCapacityModelIdAndConceptId(Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(capacityModelEntityResponse));
		Mockito.doNothing().when(capacityModelAndLocationRepo).deleteAllByCapacityModel(Mockito.any());	
		Mockito.lenient().when(capacityModelAndLocationRepo.saveAll(Mockito.any())).thenReturn(Collections.emptyList());
		Mockito.when(locationClient.getAllRestaurants()).thenReturn(locations);
		Mockito.lenient().when(capacityModelAndCapacityTemplateRepo.saveAll(Mockito.any()))
				.thenReturn(Collections.emptyList());	
		Mockito.lenient().when(capacityTemplateRepo.findAll())
				.thenReturn(Collections.singletonList(capacityTemplateEntityRes));
		Mockito.lenient().when(capacityTemplateRepo.findAllById(Mockito.anyList()))
				.thenReturn(Collections.singletonList(capacityTemplateEntityRes));
		Mockito.lenient().when(capacityModelRepository.save(Mockito.any())).thenReturn(capacityModelEntityRes);
		CapacityTemplateModel capacityTemplateModel=capacityTemplateModelServiceImpl.updateCapacityModel("1", capacityModelRequest, "user");
		assertNotNull(capacityTemplateModel);

	}
	
	
	@Test
	void testValidateCapacityModelBusinessDates () {	
		CapacityTemplateTypeEntity days = new CapacityTemplateTypeEntity();
		days.setCapacityTemplateTypeId(BigInteger.valueOf(1));
		days.setCapacityTemplateTypeNm("Days");
		CapacityTemplateEntity c1 = new CapacityTemplateEntity();
		c1.setCapacityTemplateId(BigInteger.valueOf(1));
		c1.setCapacityTemplateType(days);
		c1.setCapacityTemplateNm("Lorum Ipsum");
		c1.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c1.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c1.setMonFlg("Y");
		c1.setTueFlg("Y");
		c1.setWedFlg("Y");
		c1.setThuFlg("N");
		c1.setFriFlg("N");
		c1.setSatFlg("Y");
		c1.setSunFlg("Y");
		c1.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c1.setEndTime(java.time.LocalTime.parse("12:46:55"));
		
		CapacityTemplateEntity c2 = new CapacityTemplateEntity();
		c2.setCapacityTemplateId(BigInteger.valueOf(2));
		c2.setCapacityTemplateNm("Lm");
		c2.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c2.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c2.setCapacityTemplateType(days);
		c2.setMonFlg("N");
		c2.setTueFlg("N");
		c2.setWedFlg("N");
		c2.setThuFlg("N");
		c2.setFriFlg("N");
		c2.setSatFlg("N");
		c2.setSunFlg("N");
		c2.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c2.setEndTime(java.time.LocalTime.parse("12:46:55"));
		
		CapacityTemplateTypeEntity dates = new CapacityTemplateTypeEntity();
		dates.setCapacityTemplateTypeId(BigInteger.valueOf(2));
		dates.setCapacityTemplateTypeNm("Dates");
		
		List<CapacityTemplateAndBusinessDateEntity> d1 = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity done = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(BigInteger.valueOf(3));
		id.setBusinessDate(Date.from(Instant.now()));
		done.setId(id);
		d1.add(done);
		
		CapacityTemplateEntity c3 = new CapacityTemplateEntity();
		c3.setCapacityTemplateId(BigInteger.valueOf(3));
		c3.setCapacityTemplateNm("Lm");
		c3.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c3.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c3.setCapacityTemplateType(dates);
		c3.setCapacityTemplateAndBusinessDates(d1);
		c3.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c3.setEndTime(java.time.LocalTime.parse("12:46:55"));
		
		List<CapacityTemplateAndBusinessDateEntity> d2 = new ArrayList<>();
		
		CapacityTemplateEntity c4 = new CapacityTemplateEntity();
		c4.setCapacityTemplateId(BigInteger.valueOf(4));
		c4.setCapacityTemplateNm("Lmg");
		c4.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c4.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c4.setCapacityTemplateType(dates);
		c4.setCapacityTemplateAndBusinessDates(d2);
		c4.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c4.setEndTime(java.time.LocalTime.parse("12:46:55"));
		
		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("model");
		
		List<CapacityTemplateEntity> list = new ArrayList<>();
		
		list.add(c1);
		
		list.add(c2);
		
		list.add(c3);
		
		list.add(c4);

		CapacityTemplateEntity request = new CapacityTemplateEntity();
		CapacityTemplateTypeEntity capacityTemplateTypeEntity=new CapacityTemplateTypeEntity();
		capacityTemplateTypeEntity.setCapacityTemplateTypeId(BigInteger.ONE);
		capacityTemplateTypeEntity.setCapacityTemplateTypeNm("Days");
		request.setCapacityTemplateType(capacityTemplateTypeEntity);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateNm("name");
		request.setCapacityTemplateId(new BigInteger("1"));
		request.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		request.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		request.setMonFlg("Y");
		request.setTueFlg("Y");
		request.setWedFlg("Y");
		request.setThuFlg("N");
		request.setFriFlg("N");
		request.setSatFlg("Y");
		request.setSunFlg("Y");
		ArrayList<BigInteger> otherTemplateId = new ArrayList<>();
		List<String> matchingTemplate = new ArrayList<>();
		otherTemplateId.add(new BigInteger("2"));
		otherTemplateId.add(new BigInteger("3"));
		Mockito.when(capacityTemplateRepo.findAllById(Mockito.anyIterable())).thenReturn(list);
		boolean res = capacityTemplateModelServiceImpl.validateCapacityModelTemplateBusinessDates(request, otherTemplateId,matchingTemplate);
		assertEquals(false, res);
	}
	
	@Test
	void testValidateDate(){
		
		CapacityTemplateTypeEntity dates = new CapacityTemplateTypeEntity();
		dates.setCapacityTemplateTypeId(BigInteger.valueOf(2));
		dates.setCapacityTemplateTypeNm("Dates");
		
		List<CapacityTemplateAndBusinessDateEntity> d1 = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity done = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(BigInteger.valueOf(3));
		id.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		done.setId(id);
		d1.add(done);
		
		CapacityTemplateEntity c3 = new CapacityTemplateEntity();
		c3.setCapacityTemplateId(BigInteger.valueOf(3));
		c3.setCapacityTemplateNm("Lm");
		c3.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c3.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c3.setCapacityTemplateType(dates);
		c3.setCapacityTemplateAndBusinessDates(d1);
		c3.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c3.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c3.setCapacityTemplateAndBusinessDates(d1);
		
		List<CapacityTemplateAndBusinessDateEntity> d2 = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity done1 = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id1 = new CapacityTemplateAndBusinessDatePK();
		id1.setCapacityTemplateId(BigInteger.valueOf(3));
		id1.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		done1.setId(id);
		d2.add(done);
		
		CapacityTemplateEntity c4 = new CapacityTemplateEntity();
		c4.setCapacityTemplateId(BigInteger.valueOf(4));
		c4.setCapacityTemplateNm("Lmg");
		c4.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		c4.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		c4.setCapacityTemplateType(dates);
		c4.setCapacityTemplateAndBusinessDates(d2);
		c4.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c4.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c4.setCapacityTemplateAndBusinessDates(d2);
		
		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("model");
		
		List<CapacityTemplateEntity> list = new ArrayList<>();
		
		list.add(c3);
		
		list.add(c4);
		
		CapacityTemplateEntity request = new CapacityTemplateEntity();
		CapacityTemplateTypeEntity capacityTemplateTypeEntity=new CapacityTemplateTypeEntity();
		capacityTemplateTypeEntity.setCapacityTemplateTypeId(BigInteger.ONE);
		capacityTemplateTypeEntity.setCapacityTemplateTypeNm("Dates");
		request.setCapacityTemplateNm("name");
		request.setCapacityTemplateType(capacityTemplateTypeEntity);
		List<CapacityTemplateAndBusinessDateEntity> capacityTemplateAndBusinessDateEntityList = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity capacityTemplateAndBusinessDateEntity = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK capacityTemplateAndBusinessDatePK = new CapacityTemplateAndBusinessDatePK();
		capacityTemplateAndBusinessDatePK.setCapacityTemplateId(BigInteger.valueOf(3));
		capacityTemplateAndBusinessDatePK.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		capacityTemplateAndBusinessDateEntity.setId(capacityTemplateAndBusinessDatePK);
		capacityTemplateAndBusinessDateEntity.setCapacityTemplate(request);
		capacityTemplateAndBusinessDateEntityList.add(capacityTemplateAndBusinessDateEntity);
		request.setCapacityTemplateAndBusinessDates(capacityTemplateAndBusinessDateEntityList);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateId(new BigInteger("1"));
		request.setEffectiveDate(DateUtil.stringToDate("01/01/2011"));
		request.setExpiryDate(DateUtil.stringToDate("01/10/2011"));
		request.setCapacityTemplateAndBusinessDates(capacityTemplateAndBusinessDateEntityList);
		ArrayList<BigInteger> otherTemplateId = new ArrayList<>();
		List<String> matchingTemplate = new ArrayList<>();
		otherTemplateId.add(new BigInteger("4"));
		Mockito.when(capacityTemplateRepo.findAllById(Mockito.anyIterable())).thenReturn(list);
		assertEquals(true, capacityTemplateModelServiceImpl.validateCapacityModelTemplateBusinessDates(request, otherTemplateId, matchingTemplate));
	}
	
	@Test
	void testValidateModelTemplateNm() {
		CapacityModelEntity capacityModelEntity=new CapacityModelEntity();
		capacityModelEntity.setCapacityModelNm("test");
		RequestContext.setConcept("1");
		Mockito.when(capacityModelRepository
				.findByCapacityModelNmAndConceptId(Mockito.any(), Mockito.any()))
				.thenReturn(Collections.singletonList(capacityModelEntity));
		assertEquals(true, capacityTemplateModelServiceImpl.validateModelTemplateNm("test"));
	}
	
	@Test
	void testValidateDays(){
		

		CapacityTemplateTypeEntity templateType = new CapacityTemplateTypeEntity();
		templateType.setCapacityTemplateTypeId(BigInteger.valueOf(2));
		templateType.setCapacityTemplateTypeNm("Days");
		
		List<CapacityTemplateAndBusinessDateEntity> businessDates = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity businessDateEntity = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(BigInteger.valueOf(3));
		id.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		businessDateEntity.setId(id);
		businessDates.add(businessDateEntity);
		
		CapacityTemplateEntity c3 = new CapacityTemplateEntity();
		c3.setCapacityTemplateId(BigInteger.valueOf(3));
		c3.setCapacityTemplateNm("Lm");
		c3.setEffectiveDate(DateUtil.stringToDate("06/01/2011"));
		c3.setExpiryDate(DateUtil.stringToDate("06/10/2011"));
		c3.setCapacityTemplateType(templateType);
		c3.setCapacityTemplateAndBusinessDates(businessDates);
		c3.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c3.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c3.setMonFlg("N");
		c3.setTueFlg("N");
		c3.setWedFlg("N");
		c3.setThuFlg("N");
		c3.setFriFlg("N");
		c3.setSatFlg("Y");
		c3.setSunFlg("N");

		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("model");
		List<CapacityTemplateEntity> list = new ArrayList<>();
		list.add(c3);
				
		CapacityTemplateEntity request = new CapacityTemplateEntity();
		CapacityTemplateTypeEntity capacityTemplateTypeEntity=new CapacityTemplateTypeEntity();
		capacityTemplateTypeEntity.setCapacityTemplateTypeId(BigInteger.ONE);
		capacityTemplateTypeEntity.setCapacityTemplateTypeNm("Days");
		request.setCapacityTemplateType(capacityTemplateTypeEntity);
		List<CapacityTemplateAndBusinessDateEntity> capacityTemplateAndBusinessDateEntityList = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity capacityTemplateAndBusinessDateEntity = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK capacityTemplateAndBusinessDatePK = new CapacityTemplateAndBusinessDatePK();
		capacityTemplateAndBusinessDatePK.setCapacityTemplateId(BigInteger.valueOf(3));
		capacityTemplateAndBusinessDatePK.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		capacityTemplateAndBusinessDateEntity.setId(capacityTemplateAndBusinessDatePK);
		capacityTemplateAndBusinessDateEntityList.add(capacityTemplateAndBusinessDateEntity);
		request.setCapacityTemplateAndBusinessDates(capacityTemplateAndBusinessDateEntityList);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateNm("name");
		request.setCapacityTemplateId(new BigInteger("1"));
		request.setEffectiveDate(DateUtil.stringToDate("06/03/2011"));
		request.setExpiryDate(DateUtil.stringToDate("06/04/2011"));
		request.setMonFlg("Y");
		request.setTueFlg("Y");
		request.setWedFlg("Y");
		request.setThuFlg("Y");
		request.setFriFlg("Y");
		request.setSatFlg("Y");
		request.setSunFlg("Y");
		ArrayList<BigInteger> otherTemplateId = new ArrayList<>();
		List<String> matchingTemplate = new ArrayList<>();
		otherTemplateId.add(new BigInteger("3"));
		Mockito.when(capacityTemplateRepo.findAllById(Mockito.anyIterable())).thenReturn(list);
		boolean res2 = capacityTemplateModelServiceImpl.validateCapacityModelTemplateBusinessDates(request, otherTemplateId, matchingTemplate);
		assertEquals(false, res2);

	}
	@Test
	void testValidateNegativeDays(){
		
		CapacityTemplateTypeEntity templateType = new CapacityTemplateTypeEntity();
		templateType.setCapacityTemplateTypeId(BigInteger.valueOf(2));
		templateType.setCapacityTemplateTypeNm("Days");
		
		List<CapacityTemplateAndBusinessDateEntity> businessDates = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity businessDateEntity = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(BigInteger.valueOf(3));
		id.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		businessDateEntity.setId(id);
		businessDates.add(businessDateEntity);
		
		CapacityTemplateEntity c3 = new CapacityTemplateEntity();
		c3.setCapacityTemplateId(BigInteger.valueOf(3));
		c3.setCapacityTemplateNm("Lm");
		c3.setEffectiveDate(DateUtil.stringToDate("06/03/2011"));
		c3.setExpiryDate(DateUtil.stringToDate("06/04/2011"));
		c3.setCapacityTemplateType(templateType);
		c3.setCapacityTemplateAndBusinessDates(businessDates);
		c3.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c3.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c3.setMonFlg("N");
		c3.setTueFlg("N");
		c3.setWedFlg("N");
		c3.setThuFlg("N");
		c3.setFriFlg("N");
		c3.setSatFlg("N");
		c3.setSunFlg("N");

		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("model");
		List<CapacityTemplateEntity> list = new ArrayList<>();
		list.add(c3);
				
		CapacityTemplateEntity request = new CapacityTemplateEntity();
		CapacityTemplateTypeEntity capacityTemplateTypeEntity=new CapacityTemplateTypeEntity();
		capacityTemplateTypeEntity.setCapacityTemplateTypeId(BigInteger.ONE);
		capacityTemplateTypeEntity.setCapacityTemplateTypeNm("Days");
		request.setCapacityTemplateType(capacityTemplateTypeEntity);
		List<CapacityTemplateAndBusinessDateEntity> capacityTemplateAndBusinessDateEntityList = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity capacityTemplateAndBusinessDateEntity = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK capacityTemplateAndBusinessDatePK = new CapacityTemplateAndBusinessDatePK();
		capacityTemplateAndBusinessDatePK.setCapacityTemplateId(BigInteger.valueOf(3));
		capacityTemplateAndBusinessDatePK.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		capacityTemplateAndBusinessDateEntity.setId(capacityTemplateAndBusinessDatePK);
		capacityTemplateAndBusinessDateEntityList.add(capacityTemplateAndBusinessDateEntity);
		request.setCapacityTemplateAndBusinessDates(capacityTemplateAndBusinessDateEntityList);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateNm("name");
		request.setCapacityTemplateId(new BigInteger("1"));
		request.setEffectiveDate(DateUtil.stringToDate("06/05/2011"));
		request.setExpiryDate(DateUtil.stringToDate("06/10/2011"));
		request.setMonFlg("Y");
		request.setTueFlg("Y");
		request.setWedFlg("Y");
		request.setThuFlg("Y");
		request.setFriFlg("Y");
		request.setSatFlg("Y");
		request.setSunFlg("Y");
		ArrayList<BigInteger> otherTemplateId = new ArrayList<>();
		otherTemplateId.add(new BigInteger("3"));
		Mockito.when(capacityTemplateRepo.findAllById(Mockito.anyIterable())).thenReturn(list);
		List<String> matchingTemplate = new ArrayList<>();
		boolean res2 = capacityTemplateModelServiceImpl.validateCapacityModelTemplateBusinessDates(request, otherTemplateId, matchingTemplate);
		assertEquals(false, res2);
	}


	@Test
	void testValidateNegativeDays1(){

		CapacityTemplateTypeEntity templateType = new CapacityTemplateTypeEntity();
		templateType.setCapacityTemplateTypeId(BigInteger.valueOf(2));
		templateType.setCapacityTemplateTypeNm("Dates");
		
		List<CapacityTemplateAndBusinessDateEntity> businessDates = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity businessDateEntity = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK id = new CapacityTemplateAndBusinessDatePK();
		id.setCapacityTemplateId(BigInteger.valueOf(3));
		id.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		businessDateEntity.setId(id);
		businessDates.add(businessDateEntity);
		
		CapacityTemplateEntity c3 = new CapacityTemplateEntity();
		c3.setCapacityTemplateId(BigInteger.valueOf(3));
		c3.setCapacityTemplateNm("Lm");
		c3.setEffectiveDate(DateUtil.stringToDate("06/01/2011"));
		c3.setExpiryDate(DateUtil.stringToDate("06/10/2011"));
		c3.setCapacityTemplateType(templateType);
		c3.setCapacityTemplateAndBusinessDates(businessDates);
		c3.setStartTime(java.time.LocalTime.parse("11:46:55"));
		c3.setEndTime(java.time.LocalTime.parse("12:46:55"));
		c3.setMonFlg("Y");
		c3.setTueFlg("Y");
		c3.setWedFlg("Y");
		c3.setThuFlg("Y");
		c3.setFriFlg("Y");
		c3.setSatFlg("Y");
		c3.setSunFlg("Y");

		CapacityModelEntity model = new CapacityModelEntity();
		model.setCapacityModelId(new BigInteger("1"));
		model.setCapacityModelNm("model");
		List<CapacityTemplateEntity> list = new ArrayList<>();
		list.add(c3);
				
		CapacityTemplateEntity request = new CapacityTemplateEntity();
		CapacityTemplateTypeEntity capacityTemplateTypeEntity=new CapacityTemplateTypeEntity();
		capacityTemplateTypeEntity.setCapacityTemplateTypeId(BigInteger.ONE);
		capacityTemplateTypeEntity.setCapacityTemplateTypeNm("Days");
		request.setCapacityTemplateType(capacityTemplateTypeEntity);
		List<CapacityTemplateAndBusinessDateEntity> capacityTemplateAndBusinessDateEntityList = new ArrayList<>();
		CapacityTemplateAndBusinessDateEntity capacityTemplateAndBusinessDateEntity = new CapacityTemplateAndBusinessDateEntity();
		CapacityTemplateAndBusinessDatePK capacityTemplateAndBusinessDatePK = new CapacityTemplateAndBusinessDatePK();
		capacityTemplateAndBusinessDatePK.setCapacityTemplateId(BigInteger.valueOf(3));
		capacityTemplateAndBusinessDatePK.setBusinessDate(DateUtil.stringToDate("01/01/2011"));
		capacityTemplateAndBusinessDateEntity.setId(capacityTemplateAndBusinessDatePK);
		capacityTemplateAndBusinessDateEntityList.add(capacityTemplateAndBusinessDateEntity);
		request.setCapacityTemplateAndBusinessDates(capacityTemplateAndBusinessDateEntityList);
		request.setConceptId(new BigInteger("1"));
		request.setCapacityTemplateNm("name");
		request.setCapacityTemplateId(new BigInteger("1"));
		request.setEffectiveDate(DateUtil.stringToDate("06/03/2011"));
		request.setExpiryDate(DateUtil.stringToDate("06/04/2011"));
		request.setMonFlg("Y");
		request.setTueFlg("Y");
		request.setWedFlg("Y");
		request.setThuFlg("Y");
		request.setFriFlg("Y");
		request.setSatFlg("Y");
		request.setSunFlg("Y");
		ArrayList<BigInteger> otherTemplateId = new ArrayList<>();
		otherTemplateId.add(new BigInteger("3"));
		Mockito.when(capacityTemplateRepo.findAllById(Mockito.anyIterable())).thenReturn(list);
		List<String> matchingTemplate = new ArrayList<>();
		boolean res2 = capacityTemplateModelServiceImpl.validateCapacityModelTemplateBusinessDates(request, otherTemplateId, matchingTemplate);
		assertEquals(false, res2);
	}
	
	@Test
	void testValidateModelTemplateNmForUpdate() {
		CapacityModelEntity capacityModelEntity=new CapacityModelEntity();
		capacityModelEntity.setConceptId(BigInteger.ONE);
		capacityModelEntity.setCapacityModelNm("test");
		capacityModelEntity.setCapacityModelId(new BigInteger("1"));
		when(capacityModelRepository.findByCapacityModelNmAndConceptId(Mockito.anyString(), Mockito.any())).thenReturn(Collections.singletonList(capacityModelEntity));
		assertEquals(true, capacityTemplateModelServiceImpl.validateModelTemplateNmForUpdate("test", "2"));
	}
	
	@Test
	void testCacheConcept() {
		Concept concept = new Concept();
		concept.setConceptName("name");
		concept.setConceptId(1);
		List<Concept> list = new ArrayList<>();
		list.add(concept);
		Mockito.when(conceptClient.getAllConcepts()).thenReturn(list);
		List<ConceptForCache> res = capacityTemplateModelServiceImpl.getCacheConceptData();
		assertNotNull(res);
	}
	
	@Test
	void DeleteCapacityModel()throws JsonProcessingException {
		CapacityModelEntity capacityModelEntity = new CapacityModelEntity();
		capacityModelEntity.setCapacityModelId(BigInteger.ONE);
		capacityModelEntity.setCapacityModelNm("name");
		capacityModelEntity.setConceptId(BigInteger.ONE);
		List<OrderList> orderList1 = new ArrayList<>();
		OrderList orderList = new OrderList();
		orderList.setListId(BigInteger.ONE);
		orderList.setListNm("name");
		orderList.setListType("user");
		orderList1.add(orderList);
		List<OrderTemplate> OrderTemplateList = new ArrayList<>();
		OrderTemplate OrderTemplate = new OrderTemplate();
		OrderTemplate.setConceptId(BigInteger.ONE);
		OrderTemplate.setId(BigInteger.ONE);
		OrderTemplate.setOrderTemplateName("name");
		OrderTemplate.setOrderLists(orderList1);
		OrderTemplateList.add(OrderTemplate);
				Mockito.when(capacityModelRepository
				.findByCapacityModelIdAndConceptId(new BigInteger("1"),new BigInteger("1")))
		        .thenReturn(Optional.of(capacityModelEntity));
		Mockito.when(orderClient.getAllOrderTemplates())
		        .thenReturn(OrderTemplateList);
		Mockito.lenient().doNothing().when(capacityModelRepository).deleteById(Mockito.any());
		Mockito.lenient().doNothing().when(capacityModelAndLocationRepo).deleteAllByCapacityModel(Mockito.any());
		Mockito.lenient().doNothing().when(capacityModelAndCapacityTemplateRepo).deleteAllByCapacityModel(Mockito.any());
		Mockito.lenient().doNothing().when(auditService).addAuditData(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		capacityTemplateModelServiceImpl.deleteTemplateModel("1","USER","Y");
	}
	
	@Test
	void DeleteCapacityModel2()throws JsonProcessingException {
		CapacityModelEntity capacityModelEntity = new CapacityModelEntity();
		capacityModelEntity.setCapacityModelId(BigInteger.ONE);
		capacityModelEntity.setCapacityModelNm("name");
		capacityModelEntity.setConceptId(BigInteger.ONE);
		capacityModelEntity.setCapacityModelAndLocations(modelAndLocationList);
		List<OrderList> orderList1 = new ArrayList<>();
		OrderList orderList = new OrderList();
		orderList.setListId(BigInteger.ONE);
		orderList.setListNm("name");
		orderList.setListType("CAPACITY MODEL LIST");
		orderList1.add(orderList);
		List<OrderTemplate> OrderTemplateList = new ArrayList<>();
		OrderTemplate OrderTemplate = new OrderTemplate();
		OrderTemplate.setConceptId(BigInteger.ONE);
		OrderTemplate.setId(BigInteger.ONE);
		OrderTemplate.setOrderTemplateName("name");
		OrderTemplate.setOrderLists(orderList1);
		OrderTemplateList.add(OrderTemplate);
		
		List<Locations> locations = new ArrayList<>();
		Locations location = new Locations();
		location.setAddressState("address");
		location.setLastModifiedDateTime(Instant.now());
		location.setLocationDescription("desc");
		location.setLocationId(new BigInteger("1"));
		location.setRestaurantNumber(new BigInteger("1111"));
		Region region  = new Region();
		region.setRegionId(1);
		region.setRegionName("region");
		location.setRegion(region);
		locations.add(location);
		
				Mockito.when(capacityModelRepository
				.findByCapacityModelIdAndConceptId(new BigInteger("1"),new BigInteger("1")))
		        .thenReturn(Optional.of(capacityModelEntity));
		Mockito.when(orderClient.getAllOrderTemplates())
		        .thenReturn(OrderTemplateList);
		Mockito.when(locationClient.getAllRestaurants()).thenReturn(locations);
		Mockito.lenient().doNothing().when(capacityModelRepository).deleteById(Mockito.any());
		Mockito.lenient().doNothing().when(capacityModelAndLocationRepo).deleteAllByCapacityModel(Mockito.any());
		Mockito.lenient().doNothing().when(capacityModelAndCapacityTemplateRepo).deleteAllByCapacityModel(Mockito.any());
		Mockito.lenient().doNothing().when(auditService).addAuditData(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		capacityTemplateModelServiceImpl.deleteTemplateModel("1","USER","Y");
	}
	
}
