package edu.npic.sps.features.companyType;

import edu.npic.sps.domain.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE companies SET company_type_id = null WHERE company_type_id IN (SELECT id FROM company_types c WHERE c.uuid = ?1); delete from company_types c where c.uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);

    Optional<CompanyType> findByUuid(String uuid);

    @Query("select (count(c) > 0) from CompanyType c where upper(c.name) = upper(?1)")
    boolean existsByNameIgnoreCase(String name);

}
