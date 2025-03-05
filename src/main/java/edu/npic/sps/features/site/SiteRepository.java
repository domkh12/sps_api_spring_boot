package edu.npic.sps.features.site;

import edu.npic.sps.domain.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
    Page<Site> findByUsers_Uuid(String uuid, Pageable pageable);


    Optional<Site> findBySiteNameIgnoreCase(String siteName);

    Boolean existsBySiteName(String siteName);

    @Query("""
            select s from Site s
            where upper(s.siteName) like upper(?1) or upper(s.city.name) like upper(?1) or upper(s.siteType.name) like upper(?1) or upper(s.company.companyName) like upper(?1) or upper(s.siteAddress) like upper(?1)""")
    Page<Site> searchSites(String searchTerm, Pageable pageable);

    Optional<Site> findByUuid(String uuid);

    Boolean existsBySiteNameIgnoreCase(String siteName);

    @Query("""
            select s from Site s
            where (
            upper(s.siteName) like upper(concat('%', ?1, '%'))
            or upper(s.siteAddress) like upper(concat('%', ?1, '%')))
            and (s.city.uuid in ?2 or ?2 is null)
            and (s.siteType.uuid in ?3 or ?3 is null)
            and (s.company.uuid in ?4 or ?4 is null)
            """)
    Page<Site> filterSite(String keyword, Collection<String> cityUuid, Collection<String> siteTypeUuid, Collection<String> companyIds, Pageable pageable);


}
