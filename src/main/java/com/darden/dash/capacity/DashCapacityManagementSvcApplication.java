package com.darden.dash.capacity;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.darden.dash.capacity.util.CapacityConstants;
import com.darden.dash.common.repository.AppParameterRepository;
import com.darden.dash.common.service.impl.AppParameterServiceImpl;
/**
 * 
 * @author skashala
 * @date 16-May-2022
 * 
 *  This application is a Capacity Management MicroService which governs the
 *  application for creating template, editing template, deleting template
 *  and creating capacity template
 * 
 *
 */

@SpringBootApplication
@ComponentScan(CapacityConstants.SCAN_PACKAGE)
@ServletComponentScan(CapacityConstants.SCAN_PACKAGE)
@EnableJpaRepositories(CapacityConstants.SCAN_PACKAGE)
@EntityScan(basePackages = { CapacityConstants.ENTITY_PACKAGE, CapacityConstants.CAPACITY_MANAGEMENT_PACKAGE })
public class DashCapacityManagementSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashCapacityManagementSvcApplication.class, args);
	}

	@Bean
	@Qualifier(CapacityConstants.APP_PARAMETER_SERVICE)
	public AppParameterServiceImpl getAppParameterServiceImpl(AppParameterRepository auditRepository) {
		return new AppParameterServiceImpl(auditRepository);
	}
	
}
