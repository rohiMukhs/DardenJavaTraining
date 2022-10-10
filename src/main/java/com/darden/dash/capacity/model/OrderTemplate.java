package com.darden.dash.capacity.model;

import java.math.BigInteger;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author asdattat
 *
 */
@Getter
@Setter
public class OrderTemplate {
	private BigInteger id;
    private String orderTemplateName;
    private BigInteger conceptId;
    private List<OrderList> orderLists;
    private List<Locations> locations;
}
