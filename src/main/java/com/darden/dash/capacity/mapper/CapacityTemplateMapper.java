package com.darden.dash.capacity.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityTemplate;
import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.capacity.util.DateUtil;

/**
 * @author skashala
 * @date 16-May-2022
 * 
 *         This class is used to map capacity template entity to capacity
 *         template model
 */
@Mapper
public interface CapacityTemplateMapper {

	/**
	 * This method is used to mapping the objects from CapacityTemplateEntity to
	 * CapacityTemplate
	 * 
	 * @param capacityTemplate
	 * @return CapacityTemplate
	 */

	@Mapping(target = CapacityConstants.CHANNELS, ignore = true)
	@Mapping(target = CapacityConstants.SLOT_CHANNELS, ignore = true)
	@Mapping(target = CapacityConstants.SLOT_END_TIME, ignore = true)
	@Mapping(target = CapacityConstants.SLOT_START_TIME, ignore = true)
	@Mapping(target = CapacityConstants.MAP_EFFECTIVE_DATE, ignore = true)
	@Mapping(target = CapacityConstants.MAP_EXPIRY_DATE, ignore = true)
	@Mapping(source = CapacityConstants.MAP_SUN_FLG, target = CapacityConstants.SUN_DAY)
	@Mapping(source = CapacityConstants.MAP_MON_FLG, target = CapacityConstants.MON_DAY)
	@Mapping(source = CapacityConstants.MAP_TUE_FLG, target = CapacityConstants.TUE_DAY)
	@Mapping(source = CapacityConstants.MAP_WED_FLG, target = CapacityConstants.WED_DAY)
	@Mapping(source = CapacityConstants.MAP_THU_FLG, target = CapacityConstants.THU_DAY)
	@Mapping(source = CapacityConstants.MAP_FRI_FLG, target = CapacityConstants.FRI_DAY)
	@Mapping(source = CapacityConstants.MAP_SAT_FLG, target = CapacityConstants.SAT_DAY)
	@Mapping(source = CapacityConstants.MAP_CAPACITY_TEMPLATE_NM, target = CapacityConstants.TEMPLATE_NAME)
	CapacityTemplate map(CapacityTemplateEntity capacityTemplate);

	/**
	 * This method is used to mapping the CapacityTemplateEntity to CapacityTemplate
	 * and converting the date formatter to String format for the specific fields
	 * 
	 * @param capacityEntity
	 * @param capacityModel
	 */
	@AfterMapping
	default void map(CapacityTemplateEntity capacityEntity, @MappingTarget CapacityTemplate capacityModel) {
		capacityModel.setEffectiveDate(DateUtil.instantToDate(capacityEntity.getEffectiveDate()));
		capacityModel.setExpiryDate(DateUtil.instantToDate(capacityEntity.getExpiryDate()));
	}

}
