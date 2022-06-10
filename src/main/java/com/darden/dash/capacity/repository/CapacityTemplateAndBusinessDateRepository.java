package com.darden.dash.capacity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndBusinessDatePK;
import com.darden.dash.capacity.entity.CapacityTemplateEntity;

/**
 * 
 * @author vraviran
 * @date 19-May-2022
 * 
 * 
 * The purpose of this CapacityTemplateAndBusinessDateRepository interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       Capacity Template And Business Date table in database using the entity class
 */
@Transactional
@Repository
public interface CapacityTemplateAndBusinessDateRepository extends JpaRepository<CapacityTemplateAndBusinessDateEntity, CapacityTemplateAndBusinessDatePK>{
	
	/**
	 * This method is used to delete a CapacityTemplateAndBusinessDateEntity value of 
	 * based on the value of CapacityTemplateEntity.
	 * 
	 * @param capacityTemplate Capacity Template Entity of the Capacity 
	 * 							Template And Business Date Entity.
	 */
	void deleteAllBycapacityTemplate(CapacityTemplateEntity capacityTemplate);
	
}
