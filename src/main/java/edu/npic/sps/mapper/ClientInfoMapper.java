package edu.npic.sps.mapper;

import edu.npic.sps.domain.ClientInfo;
import edu.npic.sps.features.clientInfo.dto.ClientInfoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientInfoMapper {

    ClientInfoResponse toClientInfoResponse(ClientInfo clientInfo);

}
