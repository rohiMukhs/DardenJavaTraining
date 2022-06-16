package com.darden.dash.capacity.service;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.darden.dash.capacity.client.LocationClient;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
import com.darden.dash.capacity.entity.CapacityModelAndLocationEntity;
import com.darden.dash.capacity.entity.CapacityModelAndLocationPK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.model.Region;
import com.darden.dash.capacity.repository.CapacityModelRepository;
import com.darden.dash.capacity.service.impl.CapacityTemplateModelServiceImpl;
import com.darden.dash.common.RequestContext;

@ExtendWith({ MockitoExtension.class })
class CapacityModelServiceImplTest {
	
	@InjectMocks
	CapacityTemplateModelServiceImpl capacityTemplateModelServiceImpl;
	
	@Mock
	private CapacityModelRepository capacityModelRepository;
	
	@Mock
	private LocationClient locationClient;
	
	public static CapacityModelEntity capacityModelEntity = new CapacityModelEntity();
	
	public static List<CapacityModelEntity> modelEntityList = new ArrayList<>();
	
	public static CapacityModelAndCapacityTemplateEntity capacityModelAndCapacityTemplateEntity = new CapacityModelAndCapacityTemplateEntity();
	
	public static List<CapacityModelAndCapacityTemplateEntity> modelAndTemplateList = new ArrayList<>();
	
	public static CapacityModelAndLocationEntity capacityModelAndLocationEntity = new CapacityModelAndLocationEntity();
	
	public static List<CapacityModelAndLocationEntity> modelAndLocationList = new ArrayList<>();
	
	@BeforeAll
	static void beforeAll() {
		capacityModelEntity.setCapacityModelId(new BigInteger("1"));
		capacityModelEntity.setCapacityModelNm("name");
		capacityModelEntity.setConceptId(new BigInteger("1"));
		capacityModelEntity.setCreatedBy("aaa");
		capacityModelEntity.setCreatedDatetime(Instant.now());
		capacityModelEntity.setIsDeletedFlg("N");
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
		Mockito.when(capacityModelRepository.findByConceptId(Mockito.any())).thenReturn(modelEntityList);
		Mockito.when(locationClient.getAllRestaurants()).thenReturn(locations);
		List<CapacityModel> res = capacityTemplateModelServiceImpl.getAllCapacityModels();
		assertNotNull(res);
	}
}
