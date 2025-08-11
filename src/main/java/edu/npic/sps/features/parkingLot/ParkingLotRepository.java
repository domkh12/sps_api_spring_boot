package edu.npic.sps.features.parkingLot;

import edu.npic.sps.domain.ParkingLot;
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
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {

    @Query("select count(p) from ParkingLot p where p.isAvailable = false and p.parkingSpace.id = ?1")
    Integer OccupiedSlotsBySpace(Integer id);

    @Query("select count(p) from ParkingLot p where p.isAvailable = true and p.parkingSpace.id = ?1")
    Integer countByIsAvailableTrueAndParkingSpace_Id(Integer id);

    @Query("select count(p) from ParkingLot p where p.isAvailable = true and p.parkingSpace.site.uuid in ?1")
    Integer countByIsAvailableTrueAndParkingSpace_Site_UuidIn(Collection<String> uuids);

    @Query("select count(p) from ParkingLot p where p.parkingSpace.site.uuid in ?1")
    Integer countByParkingSpace_Site_UuidIn(Collection<String> uuids);

    @Query("select count(p) from ParkingLot p where p.isAvailable = true")
    Integer countByIsAvailableTrue();

    @Query("select count(p) from ParkingLot p")
    Integer countFirstBy();

    @Query("select count(p) from ParkingLot p where p.parkingSpace.id = ?1 and p.isAvailable = false")
    Integer countByParkingSpace_IdAndIsAvailableFalse(Integer id);

    @Query("select count(p) from ParkingLot p where p.parkingSpace.id = ?1")
    Integer countByParkingSpace_Id(Integer id);

    @Query("select count(p) from ParkingLot p where p.isAvailable = false and p.parkingSpace.site.uuid in ?1")
    Integer OccupiedSlotsBySites(Collection<String> uuids);


    @Query("select count(p) from ParkingLot p where p.isAvailable = false")
    Integer OccupiedSlots();

    @Query("select count(p) from ParkingLot p")
    Integer totalParkingSlotsCount();

    @Query("select (count(p) > 0) from ParkingLot p where p.parkingSpace.site.uuid in ?1 and p.uuid = ?2")
    boolean existsByParkingSpace_Site_UuidInAndUuid(Collection<String> uuids, String uuid);

    @Query("select p from ParkingLot p where p.parkingSpace.site.uuid in ?1")
    Optional<ParkingLot> findByParkingSpace_Site_UuidInObj(Collection<String> uuids);

    @Query("select (count(p) > 0) from ParkingLot p where p.parkingSpace.site.uuid in ?1")
    boolean existsByParkingSpace_Site_UuidIn(Collection<String> uuids);

    @Query("select p from ParkingLot p where p.parkingSpace.site.uuid in ?1")
    Page<ParkingLot> findByParkingSpace_Site_UuidIn(Collection<String> uuids, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE parking_lot_details SET parking_lot_id = null WHERE parking_lot_id IN (SELECT id FROM parking_lots pl WHERE pl.uuid = ?1); DELETE FROM parking_lots pl WHERE pl.uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);

    @Query("select (count(p) > 0) from ParkingLot p where upper(p.lotName) = upper(?1) and p.parkingSpace.uuid = ?2")
    boolean existsByLotNameIgnoreCaseAndParkingSpace_Uuid(String lotName, String uuid);

    @Query("""
            select p from ParkingLot p
            where (upper(p.lotName) like upper(concat('%', ?1, '%'))
                  or upper(p.parkingSpace.label) like upper(concat('%', ?1, '%')))
                  and (p.parkingSpace.site.uuid in ?2 or ?2 is null)""")
    Page<ParkingLot> filterParkingLot(String keywords, Collection<String> siteUuid, Pageable pageable);


    long countByParkingSpace_Uuid(String uuid);

    @Query("select p from ParkingLot p where p.parkingSpace.uuid = ?1")
    List<ParkingLot> findAllLotByParkingSpaceUuid(String uuid);


    Optional<ParkingLot> findByUuid(String uuid);

    @Query("select p from ParkingLot p where p.parkingSpace.uuid = ?1")
    List<ParkingLot> findByParkingSpaceUuid(String uuid);


}
