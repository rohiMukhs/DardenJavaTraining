package com.darden.dash.capacity.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.entity.CapacitySlotTransactionEntity;

@Repository
public interface CapacitySlotTransactionRepository extends JpaRepository<CapacitySlotTransactionEntity, String> {
	
	List<CapacitySlotTransactionEntity> findByCapacityChannelInAndBusinessDate(List<CapacityChannelEntity> capacityChannel, LocalDate businessDate);
	
	List<CapacitySlotTransactionEntity> findByCapacityChannelAndBusinessDate(CapacityChannelEntity capacityChannel, LocalDate businessDate);
	
}