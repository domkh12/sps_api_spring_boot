package edu.npic.sps.features.licensePlateProvince;

import edu.npic.sps.domain.LicensePlateProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicensePlateProvinceRepository extends JpaRepository<LicensePlateProvince, Integer> {

    Optional<LicensePlateProvince> findByProvinceNameEnIgnoreCase(String provinceNameEn);

    Optional<LicensePlateProvince> findByUuid(String uuid);
}
