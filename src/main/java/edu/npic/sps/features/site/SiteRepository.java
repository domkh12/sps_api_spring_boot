package edu.npic.sps.features.site;

import edu.npic.sps.domain.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {

    @Query("select count(s) from Site s where s.uuid in ?1")
    long countByUuidIn(Collection<String> uuids);

    @Query("select s from Site s where s.uuid = ?1")
    List<Site> findBranchByUuid(String uuid);

    @Query("select count(s) from Site s where s.company.id = ?1")
    Integer countByCompany_Id(Integer id);

    @Query("select count(s) from Site s")
    Integer totalBranches();

    @Query("select s from Site s where s.uuid in ?1")
    List<Site> findListByUuid(Collection<String> uuids);

    @Transactional
    @Modifying
    @Query(value = "UPDATE parking_spaces SET site_id = null WHERE site_id IN (SELECT id FROM branches b WHERE b.uuid = ?1); DELETE FROM users_sites WHERE site_id IN (SELECT id FROM branches b WHERE b.uuid = ?1); DELETE FROM vehicles_sites WHERE site_id IN (SELECT id FROM branches b WHERE b.uuid = ?1); DELETE FROM branches b WHERE b.uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);


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
