package edu.npic.sps.features.company;

import edu.npic.sps.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE branches SET company_id = null WHERE company_id IN (SELECT id FROM companies c WHERE c.uuid = ?1); DELETE FROM companies c WHERE c.uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);

    @Query("select (count(c) > 0) from Company c where upper(c.companyName) = upper(?1)")
    boolean existsByCompanyNameIgnoreCase(String companyName);

    Optional<Company> findByUuid(String uuid);
}
