package edu.npic.sps.features.licensePlateType;

import edu.npic.sps.domain.LicensePlateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicensePlateTypeRepository extends JpaRepository<LicensePlateType, Integer> {
    Optional<LicensePlateType> findByUuid(String uuid);
}
