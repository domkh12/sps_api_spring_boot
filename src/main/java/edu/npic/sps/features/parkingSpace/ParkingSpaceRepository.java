package edu.npic.sps.features.parkingSpace;

import edu.npic.sps.domain.ParkingSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE parking_lots SET parking_space_id = null WHERE parking_space_id IN (SELECT id FROM parking_spaces p WHERE p.uuid =?1 ); DELETE FROM parking_spaces p where p.uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);

    Optional<ParkingSpace> findByLabelIgnoreCase(String label);

    long countBySite_Uuid(String uuid);

    @Query("select p from ParkingSpace p where upper(p.label) like upper(concat('%', ?1, '%')) and (p.site.uuid in ?2 or ?2 is null)")
    Page<ParkingSpace> filterParkingSpace(String label, Collection<String> branchUuids, Pageable pageable);

    Optional<ParkingSpace> findByUuid(String uuid);

    @Query("select p from ParkingSpace p where p.site.uuid = ?1 order by p.id DESC")
    List<ParkingSpace> findBySiteUuid(String uuid);

    @Query("select p from ParkingSpace p where p.site.uuid = ?1")
    Page<ParkingSpace> findParkingSpacesBySiteUuid(String uuid, Pageable pageable);





}
