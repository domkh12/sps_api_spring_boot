package edu.npic.sps.features.site;

import edu.npic.sps.features.site.dto.CreateSite;
import edu.npic.sps.features.site.dto.SiteRequest;
import edu.npic.sps.features.site.dto.SiteResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SiteService {

    List<SiteResponse> findAllByUserRole();

    Page<SiteResponse> findAll(int pageNo, int pageSize);

    SiteResponse createSite(CreateSite createSite);

    SiteResponse update(String uuid, SiteRequest siteRequest);

    void delete(String uuid);

    Page<SiteResponse> filter(int pageNo, int pageSize, String keywords, String cityId, String siteTypeId, String companyId);
}
