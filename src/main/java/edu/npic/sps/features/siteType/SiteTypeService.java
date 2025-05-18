package edu.npic.sps.features.siteType;

import edu.npic.sps.features.siteType.dto.SiteTypeResponse;

import java.util.List;

public interface SiteTypeService {
    List<SiteTypeResponse> findAll();
}
