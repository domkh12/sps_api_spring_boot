package edu.npic.sps.features.company;

import edu.npic.sps.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query("select (count(c) > 0) from Company c where upper(c.companyName) = upper(?1)")
    boolean existsByCompanyNameIgnoreCase(String companyName);


    Optional<Company> findByUuid(String uuid);
}
