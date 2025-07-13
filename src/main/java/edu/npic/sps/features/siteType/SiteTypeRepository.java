package edu.npic.sps.features.siteType;

import edu.npic.sps.domain.SiteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SiteTypeRepository extends JpaRepository<SiteType, Integer> {

    @Query("select (count(s) > 0) from SiteType s where upper(s.name) = upper(?1) and s.uuid <> ?2")
    boolean existsByNameIgnoreCaseAndUuidNot(String name, String uuid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE branches SET site_type_id = NULL WHERE site_type_id IN (SELECT id FROM branch_types WHERE uuid = ?1); DELETE FROM branch_types WHERE uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);


    Optional<SiteType> findByUuid(String uuid);
}
