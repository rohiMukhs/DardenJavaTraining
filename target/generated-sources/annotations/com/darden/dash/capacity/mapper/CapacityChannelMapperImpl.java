package com.darden.dash.capacity.mapper;

import com.darden.dash.capacity.entity.CapacityChannelEntity;
import com.darden.dash.capacity.model.CapacityChannel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-26T13:12:12+0530",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.13 (Amazon.com Inc.)"
)
public class CapacityChannelMapperImpl implements CapacityChannelMapper {

    @Override
    public List<CapacityChannel> mapChannels(List<CapacityChannelEntity> capacityChannelEntites) {
        if ( capacityChannelEntites == null ) {
            return null;
        }

        List<CapacityChannel> list = new ArrayList<CapacityChannel>( capacityChannelEntites.size() );
        for ( CapacityChannelEntity capacityChannelEntity : capacityChannelEntites ) {
            list.add( map( capacityChannelEntity ) );
        }

        return list;
    }

    @Override
    public CapacityChannel map(CapacityChannelEntity capacityChannelEntity) {
        if ( capacityChannelEntity == null ) {
            return null;
        }

        CapacityChannel capacityChannel = new CapacityChannel();

        capacityChannel.setCapacityChannelName( capacityChannelEntity.getCapacityChannelNm() );
        capacityChannel.setFirendlyName( capacityChannelEntity.getFirendlyNm() );
        capacityChannel.setCapacityChannelId( capacityChannelEntity.getCapacityChannelId() );
        capacityChannel.setInterval( String.valueOf( capacityChannelEntity.getInterval() ) );
        capacityChannel.setIsCombinedFlg( capacityChannelEntity.getIsCombinedFlg() );

        map( capacityChannelEntity, capacityChannel );

        return capacityChannel;
    }
}
