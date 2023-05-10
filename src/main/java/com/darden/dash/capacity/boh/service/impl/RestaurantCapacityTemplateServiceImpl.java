package com.darden.dash.capacity.boh.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.boh.model.CreateRestaurantCapacityTemplateRequest;
import com.darden.dash.capacity.boh.service.RestaurantCapacityTemplateService;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.repository.CapacitySlotRepository;
import com.darden.dash.capacity.repository.CapacitySlotTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * @author vlsowjan
 * 
 *
 *       Service Implementation class which holds method definitions which deals
 *       with Capacity Management for BOH or any business logic related to
 *       Capacity Management
 */
@Service
public class RestaurantCapacityTemplateServiceImpl  implements RestaurantCapacityTemplateService{
	CapacitySlotRepository capacitySlotRepository;

	CapacitySlotTypeRepository capacitySlotTypeRepository;

	@Autowired
	public RestaurantCapacityTemplateServiceImpl(CapacitySlotRepository capacitySlotRepository,
			CapacitySlotTypeRepository capacitySlotTypeRepository) {
		super();
		this.capacitySlotRepository = capacitySlotRepository;
		this.capacitySlotTypeRepository = capacitySlotTypeRepository;
	}

	@Override
	public CapacityTemplateEntity createTemplate(
			CreateRestaurantCapacityTemplateRequest createRestaurantCapacityTemplateRequest, String user)
			throws JsonProcessingException {
		
		return null;
	}

	@Override
	public CapacityResponse getAllCapacityTemplates(Boolean assignedTemplate, String conceptId) {
	
		return null;
	}

}
