package com.darden.dash.capacity.boh.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.boh.entity.RestaurantTemplateEntity;
/**
 * 
 * @author vlsowjan
 * The purpose of this RestaurantTemplateRepository
 *         interface is to extend the properties of JpaRepository to perform the
 *         CRUD operation on the Restaurant Template table in
 *         database using the entity class.
 */
@Repository
public interface RestaurantTemplateRepository extends JpaRepository<RestaurantTemplateEntity, BigInteger> {

	/**
	 * This method is used to get capacity template based on value of template Id 
	 * and concept Id provided in header.
	 * 
	 * @param templateId Template Id of the Restaurant Template Entity.
	 * 
	 * @param conceptId ConceptId of the Capacity Template Entity.
	 * 
	 * @return RestaurantTemplateEntity Restaurant Template entity class retrieved 
	 * 							based on the parameters passed.
	 */
	Optional<RestaurantTemplateEntity> findByRestaurantTemplateIdAndConceptId(BigInteger bigTemplateId,
			BigInteger bigInteger);
	
	/**
	 * This method is used to get Restaurant template using provided concept Id.
	 * 
	 * @param conceptId ConceptId of the Capacity Template Entity.
	 * 
	 * @return List<RestaurantCapacityTemplateEntity> List of Capacity Template entity class
	 *         retrieved based on the parameters passed.
	 */
	List<RestaurantTemplateEntity> findByConceptId(BigInteger conceptId);

}
