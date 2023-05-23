package com.darden.dash.capacity.boh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.boh.entity.RestaurantTemplateAndBusinessDateEntity;
import com.darden.dash.capacity.boh.entity.RestaurantTemplateAndBusinessDatePK;

/**
 * 
 * @author vlsowjan
 * 
 * 
 * 
 *         The purpose of this RestaurantTemplateAndBusinessDateRepository
 *         interface is to extend the properties of JpaRepository to perform the
 *         CRUD operation on the Restaurant Template And Business Date table in
 *         database using the entity class
 */
@Transactional
@Repository
public interface RestaurantTemplateAndBusinessDateRepository
		extends JpaRepository<RestaurantTemplateAndBusinessDateEntity, RestaurantTemplateAndBusinessDatePK> {

}
