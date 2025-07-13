package edu.npic.sps.features.licensePlateType;

import edu.npic.sps.domain.LicensePlateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LicensePlateTypeRepository extends JpaRepository<LicensePlateType, Integer> {

    @Query("select (count(l) > 0) from LicensePlateType l where upper(l.name) = upper(?1) and l.uuid <> ?2")
    boolean existsByNameIgnoreCaseAndUuidNot(String name, String uuid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE vehicles SET license_plate_type_id = null WHERE license_plate_type_id IN (SELECT id FROM license_plate_types WHERE uuid = ?1); DELETE FROM license_plate_types WHERE uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);


    @Query("select (count(l) > 0) from LicensePlateType l where upper(l.name) = upper(?1)")
    boolean existsByNameIgnoreCase(String name);

    Optional<LicensePlateType> findByUuid(String uuid);
}
