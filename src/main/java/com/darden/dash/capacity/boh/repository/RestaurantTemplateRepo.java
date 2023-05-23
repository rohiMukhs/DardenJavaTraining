package com.darden.dash.capacity.boh.repository;

import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.boh.entity.RestaurantTemplateEntity;

/**
 * 
 * @author vlsowjan
 * 
 * 
 *         The purpose of this RestaurantTemplateRepo interface is to extend the
 *         properties of JpaRepository to perform the CRUD operation on the
 *         restauranttemplate table in database using the entity class
 */

@Transactional
@Repository
public interface RestaurantTemplateRepo extends JpaRepository<RestaurantTemplateEntity, BigInteger> {

}
