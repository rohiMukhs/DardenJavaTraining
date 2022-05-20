package com.darden.dash.capacity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacityTemplateAndCapacityChannelPK;

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

}
