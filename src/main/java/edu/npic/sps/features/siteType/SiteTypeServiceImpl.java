package edu.npic.sps.features.siteType;

import edu.npic.sps.domain.SiteType;
import edu.npic.sps.features.siteType.dto.SiteTypeRequest;
import edu.npic.sps.features.siteType.dto.SiteTypeResponse;
import edu.npic.sps.mapper.SiteTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
        siteTypeRepository.deleteByUuid(siteType.getUuid());
    }

    @Override
    public SiteTypeResponse updateByUuid(String uuid, SiteTypeRequest siteTypeRequest) {

        if (siteTypeRepository.existsByNameIgnoreCaseAndUuidNot(siteTypeRequest.name(), uuid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Site Type already exists!");
        }

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
        siteType.setCreatedAt(LocalDateTime.now());
        SiteType savedSiteType = siteTypeRepository.save(siteType);
        return siteTypeMapper.toSiteTypeResponse(savedSiteType);
    }

    @Override
    public List<SiteTypeResponse> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<SiteType> siteTypes = siteTypeRepository.findAll(sort);
        return siteTypes.stream().map(siteTypeMapper::toSiteTypeResponse).toList();
    }
}
