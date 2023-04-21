package com.darden.dash.capacity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelPK;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;

/**
 * 
 * @author vraviran
 * @date 19-May-2022
 * 
 * 
 * 		 The purpose of this CapacityTemplateAndCapacityChannelRepository interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       Capacity Template And Capacity Channel table in database using the entity class
 */
@Transactional
@Repository
public interface CapacityTemplateAndCapacityChannelRepository extends JpaRepository<CapacityTemplateAndCapacityChannelEntity, CapacityTemplateAndCapacityChannelPK>{
	
	/**
	 * This method is used to delete a CapacityTemplateAndCapacityChannelEntity value of 
	 * based on the value of CapacityTemplateEntity.
	 * 
	 * @param capacityTemplate Capacity Template Entity of the Capacity 
	 * 							Template And Capacity Channel Entity.
	 */
	void deleteAllBycapacityTemplate(CapacityTemplateEntity capacityTemplate);
	
	
	/**
	 * This method is used to return a list of CapacityTemplateAndCapacityChannelEntity values, 
	 * based on the value of capacityChannelId.
	 * 
	 * @param capacityChannelId BigInteger Id of Capacity Channel.
	 */
	List<CapacityTemplateAndCapacityChannelEntity> findByCapacityChannel(CapacityChannelEntity capacityChannel);
	
}
