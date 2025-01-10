package edu.npic.sps.features.parkingSpace;

import edu.npic.sps.domain.ParkingSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Integer> {

    Optional<ParkingSpace> findByUuid(String uuid);

    boolean existsByLabelContainsIgnoreCase(String label);

    Optional<ParkingSpace> findTopByOrderByCreatedAtDesc();

    Optional<ParkingSpace> findByLabel(String label);

    List<ParkingSpace> findBySite_Uuid(String uuid);

    @Query("select p from ParkingSpace p where p.site.uuid = ?1")
    Page<ParkingSpace> findParkingSpacesBySiteUuid(String uuid, Pageable pageable);



}
