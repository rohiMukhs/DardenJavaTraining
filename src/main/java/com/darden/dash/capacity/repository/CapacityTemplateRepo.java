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
	 * @param conceptId ConceptId of the Capacity Template Entity.
	 * 
	 * @return List<CapacityTemplateEntity> List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityTemplateEntity> findByConceptId(BigInteger conceptId);
	
	/**
	 * This method is used to get capacity template using capacity Template name.
	 * 
	 * @param capacityTemplateNm Capacity Template Name of the Capacity Template Entity.
	 * 
	 * @param conceptId ConceptId of the Capacity Template Entity.
	 * 
	 * @return CapacityTemplateEntity Capacity Template entity class retrieved 
	 * 							based on the parameters passed.
	 */
	CapacityTemplateEntity findByCapacityTemplateNmAndConceptId(String capacityTemplateNm, BigInteger conceptId);
	
	/**
	 * This method is used to get capacity template based on value of template Id 
	 * and concept Id provided in header.
	 * 
	 * @param templateId Template Id of the Capacity Template Entity.
	 * 
	 * @param conceptId ConceptId of the Capacity Template Entity.
	 * 
	 * @return CapacityTemplateEntity Capacity Template entity class retrieved 
	 * 							based on the parameters passed.
	 */
	Optional<CapacityTemplateEntity> findByCapacityTemplateIdAndConceptId(BigInteger templateId, BigInteger conceptId);
	
	/**
	 * This method is to fetch capacity templates by Monday flag.
	 * 
	 * @param monFlg contains value of flag
	 * 
	 * @return List<CapacityTemplateEntity> List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityTemplateEntity> findByMonFlgAndConceptId(String monFlg, BigInteger conceptId);
	
	/**
	 * This method is to fetch capacity templates by Tuesday flag.
	 * 
	 * @param tueFlg contains value of flag
	 * 
	 * @return List<CapacityTemplateEntity> List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityTemplateEntity> findByTueFlgAndConceptId(String tueFlg, BigInteger conceptId);
	
	/**
	 * This method is to fetch capacity templates by Wednesday flag.
	 * 
	 * @param wedFlg contains value of flag
	 * 
	 * @return List<CapacityTemplateEntity> List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityTemplateEntity> findByWedFlgAndConceptId(String wedFlg, BigInteger conceptId);
	
	/**
	 * This method is to fetch capacity templates by Thursday flag.
	 * 
	 * @param thuFlg contains value of flag
	 * 
	 * @return List<CapacityTemplateEntity> List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityTemplateEntity> findByThuFlgAndConceptId(String thuFlg, BigInteger conceptId);
	
	/**
	 * This method is to fetch capacity templates by Friday flag.
	 * 
	 * @param friFlg contains value of flag
	 * 
	 * @return List<CapacityTemplateEntity> List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityTemplateEntity> findByFriFlgAndConceptId(String friFlg, BigInteger conceptId);
	
	/**
	 * This method is to fetch capacity templates by Saturday flag.
	 * 
	 * @param satFlg contains value of flag
	 * 
	 * @return List<CapacityTemplateEntity> List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityTemplateEntity> findBySatFlgAndConceptId(String satFlg, BigInteger conceptId);
	
	/**
	 * This method is to fetch capacity templates by Sunday flag.
	 * 
	 * @param sunFlg contains value of flag
	 * 
	 * @return List<CapacityTemplateEntity> List of Capacity Template entity class retrieved 
	 * 									   based on the parameters passed.
	 */
	List<CapacityTemplateEntity> findBySunFlgAndConceptId(String sunFlg, BigInteger conceptId);
	
}
