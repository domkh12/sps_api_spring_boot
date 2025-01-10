package edu.npic.sps.features.siteType;

import edu.npic.sps.domain.SiteType;
import edu.npic.sps.features.siteType.dto.SiteTypeResponse;
import edu.npic.sps.mapper.SiteTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteTypeServiceImpl implements SiteTypeService{

    private final SiteTypeRepository siteTypeRepository;
    private final SiteTypeMapper siteTypeMapper;

    @Override
    public List<SiteTypeResponse> findAll() {
        List<SiteType> siteTypes = siteTypeRepository.findAll();
        return siteTypes.stream().map(siteType -> siteTypeMapper.toSiteTypeResponse(siteType)).toList();
    }
}
