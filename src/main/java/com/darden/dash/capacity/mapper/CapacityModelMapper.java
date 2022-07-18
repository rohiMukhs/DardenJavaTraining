package com.darden.dash.capacity.mapper;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.darden.dash.capacity.entity.CapacityModelAndLocationEntity;
import com.darden.dash.capacity.entity.CapacityModelAndLocationPK;
import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.model.CapacityModel;
import com.darden.dash.capacity.model.CapacityTemplateNames;
import com.darden.dash.capacity.model.Locations;
import com.darden.dash.capacity.util.CapacityConstants;

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
	default CapacityModel mapToCapacityModel(CapacityModelEntity mel, List<Locations> restaurantList) {
		CapacityModel model = new CapacityModel();
		model.setCapacityModelId(mel.getCapacityModelId());
		model.setCapacityModelName(mel.getCapacityModelNm());
		List<CapacityTemplateNames> templateNames = new ArrayList<>();
		mel.getCapacityModelAndCapacityTemplates().stream().forEach(cmct -> {
			if(cmct.getCapacityTemplate().getCapacityTemplateNm() != null){
				CapacityTemplateNames tempName = new CapacityTemplateNames();
				tempName.setTemplateName(cmct.getCapacityTemplate().getCapacityTemplateNm());
				templateNames.add(tempName);
			}
		});
		model.setCapacityTemplateList(templateNames);
		Set<BigInteger> restaurantNumberList = new HashSet<>();
		restaurantList.stream()
			.forEach(rl -> mel.getCapacityModelAndLocations().stream()
					.forEach(mll -> {
						if(rl.getLocationId().equals(mll.getId().getLocationId()))
							restaurantNumberList.add(rl.getRestaurantNumber());
					})
		);
		model.setRestaurants(restaurantNumberList);
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
					capacityModelAndLocationEntites.add(capacityModelAndLocationEntity);
				});
		return capacityModelAndLocationEntites;
	}
	
}
