package com.darden.dash.capacity.boh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.boh.entity.RestaurantTemplateAndCapacityChannelEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateAndCapacityChannelPK;

/**
 * 
 * @author vraviran
 * @date 19-May-2022
 * 
 * 
 *       The purpose of this RestaurantTemplateAndCapacityChannelRepository
 *       interface is to extend the properties of JpaRepository to perform the
 *       CRUD operation on the Restaurant Template And Capacity Channel table in
 *       database using the entity class
 */
@Transactional
@Repository
public interface RestaurantTemplateAndCapacityChannelRepository
		extends JpaRepository<RestaurantTemplateAndCapacityChannelEntity, RestaurantTemplateAndCapacityChannelPK> {

}
