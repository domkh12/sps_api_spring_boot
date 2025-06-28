package edu.npic.sps.features.siteType;

import edu.npic.sps.domain.SiteType;
import edu.npic.sps.features.siteType.dto.SiteTypeRequest;
import edu.npic.sps.features.siteType.dto.SiteTypeResponse;
import edu.npic.sps.mapper.SiteTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiteTypeServiceImpl implements SiteTypeService{

    private final SiteTypeRepository siteTypeRepository;
    private final SiteTypeMapper siteTypeMapper;

    @Override
    public SiteTypeResponse findByUuid(String uuid) {
        SiteType siteType = siteTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site Type not found!")
        );

        return siteTypeMapper.toSiteTypeResponse(siteType);
    }

    @Override
    public void delete(String uuid) {
        SiteType siteType = siteTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site Type not found!")
        );
        siteTypeRepository.delete(siteType);
    }

    @Override
    public SiteTypeResponse updateByUuid(String uuid, SiteTypeRequest siteTypeRequest) {
        SiteType siteType = siteTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site Type not found!")
        );
        siteTypeMapper.updateFromSiteTypeRequest(siteTypeRequest, siteType);
        return siteTypeMapper.toSiteTypeResponse(siteTypeRepository.save(siteType));
    }

    @Override
    public SiteTypeResponse create(SiteTypeRequest siteTypeRequest) {
        SiteType siteType = siteTypeMapper.fromSiteTypeRequest(siteTypeRequest);
        siteType.setUuid(UUID.randomUUID().toString());
        SiteType savedSiteType = siteTypeRepository.save(siteType);
        return siteTypeMapper.toSiteTypeResponse(savedSiteType);
    }

    @Override
    public List<SiteTypeResponse> findAll() {
        List<SiteType> siteTypes = siteTypeRepository.findAll();
        return siteTypes.stream().map(siteType -> siteTypeMapper.toSiteTypeResponse(siteType)).toList();
    }
}
