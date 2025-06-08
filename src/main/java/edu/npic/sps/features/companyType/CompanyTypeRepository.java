package edu.npic.sps.features.companyType;

import edu.npic.sps.domain.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Integer> {
    Optional<CompanyType> findByUuid(String uuid);
}
