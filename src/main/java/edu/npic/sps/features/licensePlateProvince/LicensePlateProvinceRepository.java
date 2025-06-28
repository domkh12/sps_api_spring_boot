package edu.npic.sps.features.licensePlateProvince;

import edu.npic.sps.domain.LicensePlateProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LicensePlateProvinceRepository extends JpaRepository<LicensePlateProvince, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE vehicles SET license_plate_province_id = NULL WHERE license_plate_province_id IN (SELECT id FROM license_plate_provinces WHERE uuid = ?1); DELETE FROM license_plate_provinces WHERE uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);

    @Query("select (count(l) > 0) from LicensePlateProvince l where l.provinceNameKh = ?1")
    boolean existsByProvinceNameKh(String provinceNameKh);

    @Query("select (count(l) > 0) from LicensePlateProvince l where upper(l.provinceNameEn) = upper(?1)")
    boolean existsByProvinceNameEnIgnoreCase(String provinceNameEn);

    Optional<LicensePlateProvince> findByProvinceNameEnIgnoreCase(String provinceNameEn);

    Optional<LicensePlateProvince> findByUuid(String uuid);
}
