package edu.npic.sps.features.vehicle;

import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.domain.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {



    @Query("select (count(v) > 0) from Vehicle v inner join v.sites sites where sites.uuid in ?1 and v.uuid = ?2")
    boolean existsBySites_UuidInAndUuid(Collection<String> uuids, String uuid);

    @Query("select v from Vehicle v left join v.sites sites where sites.uuid in ?1")
    Optional<Vehicle> findBySites_UuidIn(Collection<String> uuids);

    @Query("select (count(v) > 0) from Vehicle v left join v.sites sites where sites.uuid in ?1")
    boolean existsBySites_UuidIn(Collection<String> uuids);

    long countBySites_Uuid(String uuid);

    Optional<Vehicle> findByUuid(String uuid);

    Boolean existsByNumberPlate(String numberPlate);

    Optional<Vehicle> findByNumberPlate(String numberPlate);

    Page<Vehicle> findBySites_Uuid(String uuid, Pageable pageable);

    @Query("""
            select v from Vehicle v left join v.sites sites
            where (
             upper(v.numberPlate) like upper(concat('%', ?1, '%'))
             or upper(v.vehicleModel) like upper(concat('%', ?1, '%'))
             or upper(v.vehicleMake) like upper(concat('%', ?1, '%'))
             )
             and (v.vehicleType.uuid in ?2 or ?2 is null)
             and (sites.uuid in ?3 or ?3 is null)
            """)
    Page<Vehicle> filterVehicles(String keyword, Collection<String> vehicleTypeUuid, Collection<String> siteUuid, Pageable pageable);

}

