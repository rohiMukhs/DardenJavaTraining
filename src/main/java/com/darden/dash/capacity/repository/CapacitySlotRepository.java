package com.darden.dash.capacity.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacitySlotEntity;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;

/**
 * 
 * @author vraviran
 * @date 19-May-2022
 * 
 * 
 * The purpose of this CapacitySlotRepository interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       capacity Slot table in database using the entity class
 */
@Transactional
@Repository
public interface CapacitySlotRepository extends JpaRepository<CapacitySlotEntity, BigInteger> {

	/**
	 * This method is used to delete a CapacitySlotEntity value of based on the value
	 * of CapacityTemplateEntity.
	 * 
	 * @param capacityTemplate Capacity Template Entity of the Capacity 
	 * 							SlotEntity.
	 */
	void deleteAllBycapacityTemplate(CapacityTemplateEntity capacityTemplate);
	
	/**
	* This method is used to retrieve list of capacityslot entity value based on
	* the value of capacityChannel entity.
	*
	* @param capacityChannel  Capacity Channel Entity of the Capacity 
	 * 							SlotEntity.
	 * 
	* @return List<CapacitySlotEntity> List of Capacity Slot Entity to retrieved based
	* 									upon the parameters
	*/
	
	List<CapacitySlotEntity> findByCapacityChannel(CapacityChannelEntity capacityChannel);
	
}
