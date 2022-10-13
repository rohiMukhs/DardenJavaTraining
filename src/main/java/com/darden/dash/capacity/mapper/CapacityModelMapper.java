package com.darden.dash.capacity.mapper;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplateEntity;
import com.darden.dash.capacity.entity.CapacityModelAndCapacityTemplatePK;
import com.darden.dash.capacity.entity.CapacityModelAndLocationEntity;
import com.darden.dash.capacity.entity.CapacityModelAndLocationPK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityModelRequest;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.model.CapacityTemplateModel;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.model.RestaurantDetail;
import com.darden.dash.capacity.model.RestaurantsAssigned;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.RequestContext;

/**
 * 
 * @author vraviran
 * 
 * This Mapper class is used to map the capacity Model entity to
 *         capacity channel model class
 *
 */
@Mapper
public interface CapacityModelMapper {
	
	/**
	 * This mapper method is to map data of capacity 
	 * model along with the data of capacity template
	 * assigned to the model.Along with the location 
	 * detail assigned to the model to the capacity
	 * model class .this mapper method is used for get
	 * all capacity model api.
	 * 
	 * @param mel Entity class containing the values of 
	 * 					capacity template.
	 * 
	 * @param restaurantList list of model class containing 
	 * 					the value of restaurants.
	 * 
	 * @return CapacityModel to be mapped model class to display
	 * 				the values retrieved.
	 */
	@Named(CapacityConstants.MAP_TO_CAPACITY_MODEL)
	default CapacityModel mapToCapacityModel(CapacityModelEntity mel, List<Locations> restaurantList,
			List<BigInteger> restaurantNumberViaOrderTemplate) {
		CapacityModel model = new CapacityModel();
		model.setCapacityModelId(mel.getCapacityModelId());
		model.setCapacityModelName(mel.getCapacityModelNm());
		List<CapacityTemplate> templateNames = new ArrayList<>();
		mel.getCapacityModelAndCapacityTemplates().stream().forEach(cmct -> {
			if(cmct.getCapacityTemplate().getCapacityTemplateNm() != null){
				CapacityTemplate tempName = new CapacityTemplate();
				tempName.setCapacityTemplateId(cmct.getCapacityTemplate().getCapacityTemplateId().toString());
				tempName.setTemplateName(cmct.getCapacityTemplate().getCapacityTemplateNm());
				templateNames.add(tempName);
			}
		});
		model.setCapacityTemplateList(templateNames);
		List<BigInteger> restaurantNumberList = new ArrayList<>();
		restaurantList.stream()
			.forEach(rl -> mel.getCapacityModelAndLocations().stream()
					.forEach(mll -> {
						if(rl.getLocationId().equals(mll.getId().getLocationId()) && !restaurantNumberList.contains(rl.getRestaurantNumber()))
							restaurantNumberList.add(rl.getRestaurantNumber());
					})
		);
		RestaurantDetail restaurants = new RestaurantDetail();
		restaurants.setViaTemplate(restaurantNumberViaOrderTemplate);
		restaurants.setViaOverride(restaurantNumberList);
		restaurantNumberList.addAll(restaurantNumberViaOrderTemplate);
		restaurants.setRestaurantCount(restaurantNumberList.size());
		model.setRestaurants(restaurants);
		model.setIsDeletedFlg(mel.getIsDeletedFlg());
		model.setCreatedBy(mel.getCreatedBy());
		model.setCreatedDateTime(mel.getCreatedDatetime());
		model.setLastModifiedBy(mel.getLastModifiedBy());
		model.setLastModifiedDateTime(mel.getLastModifiedDatetime());
		return model;
	}
	
	/**
	 * This mapper method is used to map data to list of Capacity Model And
	 * Location Entity.
	 * 
	 * @param user String contains the user detail from access token.
	 * 
	 * @param dateTime Instant containing the value of date and time.
	 * 
	 * @param savedEntity entity class containing the value of capacity
	 * 					model.
	 * 
	 * @param notAssignedLocation list of bigInteger containing the 
	 * 					value of not assigned location Id.
	 * 
	 * @return List<CapacityModelAndLocationEntity> list entity class containing
	 *   data of mapped Capacity Model And Location.
	 */
	@Named(CapacityConstants.MAPTOCAPACITYMODELANDLOCATIONENTITYLIST)
	default List<CapacityModelAndLocationEntity> mapToCapacityModelAndLocationEntityList(String user, Instant dateTime,
			CapacityModelEntity savedEntity, ArrayList<BigInteger> notAssignedLocation) {
		List<CapacityModelAndLocationEntity> capacityModelAndLocationEntites = new ArrayList<>();
		notAssignedLocation.stream().filter(Objects::nonNull)
				.forEach(restaurantsAssigned -> {
					CapacityModelAndLocationEntity capacityModelAndLocationEntity = new CapacityModelAndLocationEntity();
					CapacityModelAndLocationPK capacityModelAndLocationPK = new CapacityModelAndLocationPK();
					capacityModelAndLocationPK.setLocationId(restaurantsAssigned);
					capacityModelAndLocationPK.setCapacityModelId(savedEntity.getCapacityModelId());
					capacityModelAndLocationEntity.setId(capacityModelAndLocationPK);
					capacityModelAndLocationEntity.setCreatedBy(user);
					capacityModelAndLocationEntity.setCreatedDatetime(dateTime);
					capacityModelAndLocationEntity.setLastModifiedBy(user);
					capacityModelAndLocationEntity.setLastModifiedDatetime(dateTime);
					capacityModelAndLocationEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
					capacityModelAndLocationEntites.add(capacityModelAndLocationEntity);
				});
		return capacityModelAndLocationEntites;
	}
	
	/**
	 * This mapper method is used to map data to Capacity Model Entity.
	 * 
	 * @param capacityModelRequest model class containing the value of create capacity model class.
	 * 
	 * @param createdBy String contains the user detail from access token.
	 * 
	 * @param dateTime Instant containing the value of date and time.
	 * 
	 * @return CapacityModelEntity entity class containing the value of
	 * 					Capacity Model Entity.
	 */
	@Named(CapacityConstants.MAPTOCAPACITYMODELENTITY)
	default CapacityModelEntity mapToCapacityModelEntity(CapacityModelRequest capacityModelRequest, String createdBy,
			Instant dateTime) {
		CapacityModelEntity capacityModelEntity = new CapacityModelEntity();
		capacityModelEntity.setCapacityModelNm(capacityModelRequest.getTemplateModelName());
		capacityModelEntity.setCreatedBy(createdBy);
		capacityModelEntity.setCreatedDatetime(dateTime);
		capacityModelEntity.setLastModifiedBy(createdBy);
		capacityModelEntity.setLastModifiedDatetime(dateTime);
		capacityModelEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
		capacityModelEntity.setIsDeletedFlg(CapacityConstants.N);
		return capacityModelEntity;
	}
	
	/**
	 * This mapper method is used to map data to list of RestaurantsAssigned model class.
	 * 
	 * @param modelLocationsList list of entity class containing the values of
	 * 					Capacity Model And Location.
	 * 
	 * @return List<RestaurantsAssigned>  list of model class containing the values of
	 * 					Restaurants Assigned.
	 */
	@Named(CapacityConstants.MAPTORESTAURANTASSIGNEDLIST)
	default List<RestaurantsAssigned> mapToRestaurantAssignedList(
			List<CapacityModelAndLocationEntity> modelLocationsList) {
		List<RestaurantsAssigned> restaurantsAssignedList = new ArrayList<>();
		modelLocationsList.stream().filter(Objects::nonNull).forEach(restaurantsAssignedEntity -> {
			RestaurantsAssigned restaurantsAssigned = new RestaurantsAssigned();
			restaurantsAssigned.setLocationId(String.valueOf(restaurantsAssignedEntity.getId().getLocationId()));
			restaurantsAssignedList.add(restaurantsAssigned);
		});
		return restaurantsAssignedList;
	}

	/**
	 * 
	 * 
	 * @param capacityTemplateModel model class containing the values of capacity
	 * 								template model.
	 * 
	 * @param responseEntity
	 */
	@Named(CapacityConstants.MAPTOCAPACITYTEMPLATEMODEL)
	default void mapToCapacityTemplateModel(CapacityTemplateModel capacityTemplateModel,
			CapacityModelEntity responseEntity) {
		capacityTemplateModel.setTemplateModelName(responseEntity.getCapacityModelNm());
		capacityTemplateModel.setCreatedBy(responseEntity.getCreatedBy());
		capacityTemplateModel.setCreatedDateTime(responseEntity.getCreatedDatetime());
		capacityTemplateModel.setLastModifiedBy(responseEntity.getLastModifiedBy());
		capacityTemplateModel.setLastModifiedDateTime(responseEntity.getLastModifiedDatetime());
		capacityTemplateModel.setIsDeletedFlg(responseEntity.getIsDeletedFlg());
	}
	
	/**
	 *  This mapper method is used to map data to list of Capacity Model And Location entity class.
	 * 
	 * @param createdBy String contains the user detail from access token.
	 * 
	 * @param dateTime Instant containing the value of date and time.
	 * 
	 * @param capacityModelEntity entity class containing the value of
	 * 					 capacity model.
	 * 
	 * @param restaurantsAssigned model class containing the value of
	 * 						restaurant assigned.
	 * 
	 * @return CapacityModelAndLocationEntity entity class containing 
	 * 						the value of Capacity Model And Location.
	 */
	@Named(CapacityConstants.MAPTOCAPACITYMODELANDLOCATIONENTITY)
	default CapacityModelAndLocationEntity mapToCapacityModelAndLocationEntity(String createdBy, Instant dateTime,
			CapacityModelEntity capacityModelEntity, RestaurantsAssigned restaurantsAssigned) {
		CapacityModelAndLocationEntity capacityModelAndLocationEntity = new CapacityModelAndLocationEntity();
		CapacityModelAndLocationPK capacityModelAndLocationPK = new CapacityModelAndLocationPK();
		capacityModelAndLocationPK.setLocationId(new BigInteger(restaurantsAssigned.getLocationId()));
		capacityModelAndLocationPK.setCapacityModelId(capacityModelEntity.getCapacityModelId());
		capacityModelAndLocationEntity.setId(capacityModelAndLocationPK);
		capacityModelAndLocationEntity.setCreatedBy(createdBy);
		capacityModelAndLocationEntity.setCreatedDatetime(dateTime);
		capacityModelAndLocationEntity.setLastModifiedBy(createdBy);
		capacityModelAndLocationEntity.setLastModifiedDatetime(dateTime);
		capacityModelAndLocationEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
		return capacityModelAndLocationEntity;
	}
	
	/**
	 *  This mapper method is used to map data to list of Capacity Model And capacity template entity class.
	 * 
	 * @param createdByString contains the user detail from access token.
	 * 
	 * @param dateTime Instant containing the value of date and time.
	 * 
	 * @param capacityModelEntity entity class containing the value of
	 * 					 capacity model.
	 * 
	 * @param capacityTemplateEntites List entity class containing the 
	 * 					value of capacity template.
	 *  
	 * @return List<CapacityModelAndCapacityTemplateEntity> list of 
	 * 			entity class containing the value of capacity model 
	 * 			and capacity template.
	 */
	@Named(CapacityConstants.MAPTOCAPACITYMODELANDCAPACITYTEMPLATEENTITYLIST)
	default List<CapacityModelAndCapacityTemplateEntity> mapToCapacityModelAndCapacityTemplateEntityList(
			String createdBy, Instant dateTime, CapacityModelEntity capacityModelEntity,
			List<CapacityTemplateEntity> capacityTemplateEntites) {
		List<CapacityModelAndCapacityTemplateEntity> capacityModelAndCapacityTemplateEntites = new ArrayList<>();
		capacityTemplateEntites.stream().forEach(capactityTemplateEntity -> {
			CapacityModelAndCapacityTemplateEntity capacityModelAndCapacityTemplateEntity = mapToCapacityModelAndCapacityTemplateEntity(
					createdBy, dateTime, capacityModelEntity, capactityTemplateEntity);
			capacityModelAndCapacityTemplateEntites.add(capacityModelAndCapacityTemplateEntity);
		});
		return capacityModelAndCapacityTemplateEntites;
	}

	/**
	 * This mapper method is used to map data to list of Capacity Model And capacity template entity class.
	 * 
	 * @param createdBy contains the user detail from access token.
	 * 
	 * @param dateTime Instant containing the value of date and time.
	 * 
	 * @param capacityModelEntity entity class containing the value of
	 * 				capacity model.
	 * 
	 * @param capactityTemplateEntity entity class containing the value of
	 * 				capacity template.
	 * 
	 * @return CapacityModelAndCapacityTemplateEntity entity class containing the value of
	 * 				capacity model and capacity template.
	 */
	@Named(CapacityConstants.MAPTOCAPACITYMODELANDCAPACITYTEMPLATEENTITY)
	default CapacityModelAndCapacityTemplateEntity mapToCapacityModelAndCapacityTemplateEntity(String createdBy,
			Instant dateTime, CapacityModelEntity capacityModelEntity, CapacityTemplateEntity capactityTemplateEntity) {
		CapacityModelAndCapacityTemplateEntity capacityModelAndCapacityTemplateEntity = new CapacityModelAndCapacityTemplateEntity();
		CapacityModelAndCapacityTemplatePK capacityModelAndCapacityTemplatePK = new CapacityModelAndCapacityTemplatePK();
		capacityModelAndCapacityTemplatePK.setCapacityModelId(capacityModelEntity.getCapacityModelId());
		capacityModelAndCapacityTemplatePK.setCapacityTemplateId(capactityTemplateEntity.getCapacityTemplateId());
		capacityModelAndCapacityTemplateEntity.setId(capacityModelAndCapacityTemplatePK);
		capacityModelAndCapacityTemplateEntity.setCreatedBy(createdBy);
		capacityModelAndCapacityTemplateEntity.setCreatedDatetime(dateTime);
		capacityModelAndCapacityTemplateEntity.setLastModifiedBy(createdBy);
		capacityModelAndCapacityTemplateEntity.setLastModifiedDatetime(dateTime);
		capacityModelAndCapacityTemplateEntity.setConceptId(new BigInteger(RequestContext.getConcept()));
		return capacityModelAndCapacityTemplateEntity;
	}
}
