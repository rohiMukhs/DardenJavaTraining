package com.darden.dash.capacity.boh.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darden.dash.capacity.boh.entity.RestaurantTemplateSlotEntity;

/**
 * 
 * @author vlsowjan
 * 
 * 
 * 
 *         The purpose of this RestaurantTemplateSlot Repository interface is to
 *         extend the properties of JpaRepository to perform the CRUD operation
 *         on the Restaurant Template Slot table in database using the entity
 *         class
 */
@Transactional
@Repository
public interface RestaurantTemplateSlotRepository extends JpaRepository<RestaurantTemplateSlotEntity, BigInteger> {

}
