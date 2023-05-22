package com.darden.dash.capacity.boh.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darden.dash.capacity.boh.entity.RestaurantTemplateEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateSlotEntity;
import com.darden.dash.capacity.boh.mapper.RestaurantCapacityTemplateMapper;
import com.darden.dash.capacity.boh.model.CreateRestaurantCapacityTemplateRequest;
import com.darden.dash.capacity.boh.model.RestaurantCapacityTemplate;
import com.darden.dash.capacity.boh.model.RestaurantChannel;
import com.darden.dash.capacity.boh.model.RestaurantSlotChannel;
import com.darden.dash.capacity.boh.model.RestaurantSlotDetail;
import com.darden.dash.capacity.boh.repository.RestaurantTemplateRepository;
import com.darden.dash.capacity.boh.service.RestaurantCapacityTemplateService;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityResponse;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;
import com.darden.dash.common.constant.ErrorCodeConstants;
import com.darden.dash.common.error.ApplicationErrors;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * @author gunsaik
 * 
 *
 *       Service Implementation class which holds method definitions which deals
 *       with Capacity Management for BOH or any business logic related to
 *       Capacity Management
 */
@Service
public class RestaurantCapacityTemplateServiceImpl implements RestaurantCapacityTemplateService{
	
	
	private RestaurantTemplateRepository restaurantTemplateRepository;
	
	private final RestaurantCapacityTemplateMapper restaurantTemplateMapper = Mappers.getMapper(RestaurantCapacityTemplateMapper.class);

	@Autowired
	public RestaurantCapacityTemplateServiceImpl(RestaurantTemplateRepository restaurantTemplateRepository) {
		super();
		this.restaurantTemplateRepository = restaurantTemplateRepository;
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

	@Override
	public RestaurantCapacityTemplate getRestaurantCapacityTempalteById(BigInteger bigTemplateId) {

		ApplicationErrors applicationErrors = new ApplicationErrors();
		Optional<RestaurantTemplateEntity> findByRestaurantTemplateIdAndConceptId = restaurantTemplateRepository
				.findByRestaurantTemplateIdAndConceptId(bigTemplateId, new BigInteger(RequestContext.getConcept()));
		if (findByRestaurantTemplateIdAndConceptId.isEmpty()) {
			applicationErrors.addErrorMessage(Integer.parseInt(ErrorCodeConstants.EC_4012), "capacityTemplateId");
			applicationErrors.raiseExceptionIfHasErrors();
		}
		RestaurantTemplateEntity template = new RestaurantTemplateEntity();
		if (findByRestaurantTemplateIdAndConceptId.isPresent())
			template = findByRestaurantTemplateIdAndConceptId.get();
		List<RestaurantChannel> capacityTemplateChannels = restaurantTemplateMapper.getRestaurantTemplateChannels(template);

		List<RestaurantTemplateSlotEntity> capacitySlots = template.getRestaurantSlots();
		MultiValuedMap<String, RestaurantSlotDetail> channelSlotDetails = new ArrayListValuedHashMap<>();
		Set<String> channelIds = new HashSet<>();
		Map<String, String> channelNames = new HashMap<>();

		restaurantTemplateMapper.mapCapacitySlots(capacitySlots, channelSlotDetails, channelIds, channelNames);

		List<RestaurantSlotChannel> slotChannels = restaurantTemplateMapper.mapSlotChannels(channelSlotDetails, channelIds,
				channelNames);

		return mapTemplateModel(template, capacityTemplateChannels, slotChannels);

	}
	
	private RestaurantCapacityTemplate mapTemplateModel(RestaurantTemplateEntity capacityTemplateEntity,
			List<RestaurantChannel> channels, List<RestaurantSlotChannel> slotChannels) {

		RestaurantCapacityTemplate capacityTemplateModel = restaurantTemplateMapper
				.mapRestaurantTemplate(capacityTemplateEntity);
		capacityTemplateModel.setCapacityTemplateType(
				capacityTemplateEntity.getRestaurantTemplateType().getRestaurantTemplateTypeNm());

		if (capacityTemplateEntity.getRestaurantTemplateType().getRestaurantTemplateTypeNm().equals(CapacityConstants.DAYS)) {
			restaurantTemplateMapper.mapToCapacityRestaurantTemplateFromEntity(capacityTemplateEntity,
					capacityTemplateModel);
		}
		capacityTemplateModel.setEffectiveDate(capacityTemplateEntity.getEffectiveDate());
		capacityTemplateModel.setExpiryDate(capacityTemplateEntity.getExpiryDate());
		capacityTemplateModel.setBusinessDate(null);
		capacityTemplateModel.setRestaurantSlotChannels(slotChannels);
		capacityTemplateModel.setRestaurantChannels(channels);

		return capacityTemplateModel;
	}


}
