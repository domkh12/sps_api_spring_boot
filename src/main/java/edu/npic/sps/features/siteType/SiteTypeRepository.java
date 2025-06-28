package edu.npic.sps.features.siteType;

import edu.npic.sps.domain.SiteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteTypeRepository extends JpaRepository<SiteType, Integer> {
    Optional<SiteType> findByUuid(String uuid);
}
