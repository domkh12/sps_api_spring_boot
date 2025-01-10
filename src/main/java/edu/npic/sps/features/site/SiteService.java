package edu.npic.sps.features.site;

import edu.npic.sps.features.site.dto.CreateSite;
import edu.npic.sps.features.site.dto.SiteResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SiteService {

    List<SiteResponse> findAllByUserRole();

    Page<SiteResponse> findAll(int pageNo, int pageSize, String searchTerm);

    SiteResponse createSite(CreateSite createSite);
}
