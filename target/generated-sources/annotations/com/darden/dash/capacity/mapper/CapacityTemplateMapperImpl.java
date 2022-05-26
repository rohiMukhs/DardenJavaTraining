package com.darden.dash.capacity.mapper;

import com.darden.dash.capacity.entity.CapacityTemplateEntity;
import com.darden.dash.capacity.model.CapacityTemplate;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-26T13:12:12+0530",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.13 (Amazon.com Inc.)"
)
public class CapacityTemplateMapperImpl implements CapacityTemplateMapper {

    @Override
    public CapacityTemplate map(CapacityTemplateEntity capacityTemplate) {
        if ( capacityTemplate == null ) {
            return null;
        }

        CapacityTemplate capacityTemplate1 = new CapacityTemplate();

        capacityTemplate1.setWedDay( capacityTemplate.getWedFlg() );
        capacityTemplate1.setTueDay( capacityTemplate.getTueFlg() );
        capacityTemplate1.setSunDay( capacityTemplate.getSunFlg() );
        capacityTemplate1.setTemplateName( capacityTemplate.getCapacityTemplateNm() );
        capacityTemplate1.setThuDay( capacityTemplate.getThuFlg() );
        capacityTemplate1.setFriDay( capacityTemplate.getFriFlg() );
        capacityTemplate1.setMonDay( capacityTemplate.getMonFlg() );
        capacityTemplate1.setSatDay( capacityTemplate.getSatFlg() );
        if ( capacityTemplate.getCapacityTemplateId() != null ) {
            capacityTemplate1.setCapacityTemplateId( capacityTemplate.getCapacityTemplateId().toString() );
        }
        capacityTemplate1.setCreatedBy( capacityTemplate.getCreatedBy() );
        capacityTemplate1.setCreatedDatetime( capacityTemplate.getCreatedDatetime() );
        capacityTemplate1.setLastModifiedBy( capacityTemplate.getLastModifiedBy() );
        capacityTemplate1.setLastModifiedDatetime( capacityTemplate.getLastModifiedDatetime() );

        map( capacityTemplate, capacityTemplate1 );

        return capacityTemplate1;
    }
}
