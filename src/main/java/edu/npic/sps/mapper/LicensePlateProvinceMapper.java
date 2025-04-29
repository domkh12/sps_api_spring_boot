package edu.npic.sps.mapper;

import edu.npic.sps.domain.LicensePlateProvince;
import edu.npic.sps.features.licensePlateProvince.dto.LicensePlateProvinceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LicensePlateProvinceMapper {

    LicensePlateProvinceResponse toLicensePlateProvinceResponse(LicensePlateProvince licensePlateProvince);

}
