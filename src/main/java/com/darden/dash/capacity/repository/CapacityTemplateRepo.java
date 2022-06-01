package com.darden.dash.capacity.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.entity.CapacityTemplateEntity;

/**
 * 
 * @author skashala
 * @date 16-May-2022
 * 
 * The purpose of this CapacityTemplateRepo interface is to extend the
 *       properties of JpaRepository to perform the CRUD operation on the
 *       capacitytemplate table in database using the entity class
 */

@Transactional
@Repository
public interface CapacityTemplateRepo extends JpaRepository<CapacityTemplateEntity, BigInteger> {
	
	/**
	 * This method is used to get capacity template using provided concept Id.
	 * 
	 * @param conceptId
	 * @return List<CapacityTemplateEntity>
	 */
	List<CapacityTemplateEntity> findByConceptId(BigInteger conceptId);
	
	/**
	 * This method is used to get capacity template using capacity Template name.
	 * 
	 * @param capacityTemplateNm
	 * @return
	 */
	CapacityTemplateEntity findByCapacityTemplateNm(String capacityTemplateNm);
	
	/**
	 * This method is used to get capacity template based on value of template Id 
	 * and concept Id provided in header.
	 * 
	 * @param templateId
	 * @param conceptId
	 * @return CapacityTemplateEntity
	 */
	Optional<CapacityTemplateEntity> findByCapacityTemplateIdAndConceptId(BigInteger templateId, BigInteger conceptId);

}
