package com.darden.dash.capacity.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.client.LocationClient;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.mapper.CapacityModelMapper;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.repository.CapacityModelRepository;
import com.darden.dash.capacity.service.CapacityTemplateModelService;
import com.darden.dash.common.RequestContext;

/**
 * 
 * @author vraviran
 * @date 16-jun-2022
 * 
 * 		 Service Implementation class which holds method definitions which deals
 *       with Capacity Model or any business logic related to Capacity
 *       Model.
 *
 */
@Service
public class CapacityTemplateModelServiceImpl implements CapacityTemplateModelService{

	private CapacityModelRepository capacityModelRepository;
	
	private LocationClient locationClient;
	
	private CapacityModelMapper capacityModelMapper = Mappers.getMapper(CapacityModelMapper.class);
	
	/**
	 * Autowiring required properties
	 * 
	 * @param capacityModelRepository
	 * @param locationClient
	 */
	@Autowired
	public CapacityTemplateModelServiceImpl(CapacityModelRepository capacityModelRepository, LocationClient locationClient) {
		super();
		this.capacityModelRepository = capacityModelRepository;
		this.locationClient = locationClient;
	}


	/**
	 * This service method is written for the purpose of retrieving the 
	 * list of capacity model based on concept Id along with the valye of
	 * capacity template and restaurant assigned to the model.
	 * 
	 * @return List<CapacityModel> list of model class containing the value 
	 * of capacity models.
	 */
	@Override
	public List<CapacityModel> getAllCapacityModels() {
		List<CapacityModelEntity> modelEntityList =  capacityModelRepository.findByConceptId(new BigInteger(RequestContext.getConcept()));
		List<Locations> restaurantList = locationClient.getAllRestaurants();
		List<CapacityModel> modelResponseList = new ArrayList<>();
		modelEntityList.stream().forEach(mel -> {
			CapacityModel model = capacityModelMapper.mapToCapacityModel(mel, restaurantList);
			modelResponseList.add(model);
		});
		return modelResponseList;
	}

}
