package edu.npic.sps.features.siteType;

import edu.npic.sps.features.siteType.dto.SiteTypeRequest;
import edu.npic.sps.features.siteType.dto.SiteTypeResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface SiteTypeService {
    SiteTypeResponse findByUuid(String uuid);

    void delete(String uuid);

    SiteTypeResponse updateByUuid(String uuid, SiteTypeRequest siteTypeRequest);

    SiteTypeResponse create(@Valid SiteTypeRequest siteTypeRequest);

    List<SiteTypeResponse> findAll();
}
