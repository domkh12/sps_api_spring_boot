package edu.npic.sps.features.company;

import edu.npic.sps.domain.Company;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("select count(c) from Company c")
    Integer totalCompanies();

    @Query("""
            select c from Company c
            where (upper(c.companyName) like upper(concat('%', ?1, '%'))
                   or upper(c.companyAddress) like upper(concat('%', ?1, '%'))
                  )
                   and (c.city.uuid in ?2 or ?2 is null)
                   and (c.companyType.uuid in ?3 or ?3 is null)
            """)
    Page<Company> filterCompany(String keywords, Collection<String> cityUuid, Collection<String> companyTypeUuid, Pageable pageable);


    @Query("select (count(c) > 0) from Company c where upper(c.companyName) = upper(?1) and c.uuid <> ?2")
    boolean existsByCompanyNameIgnoreCaseAndUuidNot(String companyName, String uuid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE branches SET company_id = null WHERE company_id IN (SELECT id FROM companies c WHERE c.uuid = ?1); DELETE FROM companies c WHERE c.uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);

    @Query("select (count(c) > 0) from Company c where upper(c.companyName) = upper(?1)")
    boolean existsByCompanyNameIgnoreCase(String companyName);

    Optional<Company> findByUuid(String uuid);

}
