package com.darden.dash.capacity.mapper;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.darden.dash.capacity.entity.CapacityModelEntity;
import com.darden.dash.capacity.model.CapacityModel;
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
		Set<String> templateNames = new HashSet<>();
		mel.getCapacityModelAndCapacityTemplates().stream().forEach(cmct -> {
			if(cmct.getCapacityTemplate().getCapacityTemplateNm() != null)
				templateNames.add(cmct.getCapacityTemplate().getCapacityTemplateNm());
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
	
}
