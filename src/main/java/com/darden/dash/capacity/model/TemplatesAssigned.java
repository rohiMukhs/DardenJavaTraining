package com.darden.dash.capacity.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * @author skashala
 * This Model class is written for the purpose of showing the
 *  values of TemplatesAssigned for Capacity Model
 *
 */
@Getter
@Setter

public class TemplatesAssigned implements Serializable {

	private String templateId;
	private String templateName;


}
