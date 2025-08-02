package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.domain.ParkingLotDetail;
import edu.npic.sps.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingLotDetailRepository extends JpaRepository<ParkingLotDetail, Integer> {
    @Query("select count(p) from ParkingLotDetail p where p.parkingSpace.id = ?1 and p.isCheckOut = true")
    Long countByParkingSpace_IdAndIsCheckOutTrue(Integer id);

    @Query("select SUM(p.durations) from ParkingLotDetail p where p.parkingSpace.id = ?1 and p.isCheckOut = true")
    Long sumByParkingSpace_IdAndIsCheckOutTrue(Integer id);

    @Query("select count(p) from ParkingLotDetail p where p.isCheckOut = true")
    Long countByIsCheckOutTrue();

    @Query("select SUM(p.durations) from ParkingLotDetail p where p.isCheckOut = true")
    Long sumByIsCheckOutTrue();

    @Query("select p from ParkingLotDetail p where upper(p.vehicle.numberPlate) like upper(concat('%', ?1, '%'))")
    Page<ParkingLotDetail> filterParkingLotDetailWithKeywords(String numberPlate, Pageable pageable);

    @Query("""
            select p from ParkingLotDetail p
            where upper(p.vehicle.numberPlate) like upper(concat('%', ?1, '%')) and p.createdAt between ?2 and ?3""")
    Page<ParkingLotDetail> filterParkingLotDetailWithDateRange(String numberPlate, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    @Query("""
            select p from ParkingLotDetail p
            where p.isCheckOut = true
            and (upper(p.vehicle.numberPlate) like upper(concat('%', ?1, '%')))
            """)
    Page<ParkingLotDetail> filterCheckOutByKeywords(String numberPlate, Pageable pageable);

    @Query("""
            select p from ParkingLotDetail p
            where p.isCheckOut = true
            and (upper(p.vehicle.numberPlate) like upper(concat('%', ?1, '%')))
            and p.createdAt between ?2 and ?3""")
    Page<ParkingLotDetail> filterCheckOutWithDateRange(String numberPlate, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    @Query("""
            select p from ParkingLotDetail p
            where p.isCheckOut = true
            and (upper(p.vehicle.numberPlate) like upper(concat('%', ?1, '%')))
            and p.createdAt between ?2 and ?3""")
    Page<ParkingLotDetail> filterCheckOut(String keywords, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    @Query("select p from ParkingLotDetail p where p.isCheckOut = true")
    Page<ParkingLotDetail> findByIsCheckOutTrue(Pageable pageable);

    @Query("""
            select p from ParkingLotDetail p
            where p.isCheckIn = true
            and (upper(p.vehicle.numberPlate) like upper(concat('%', ?1, '%')))
            """)
    Page<ParkingLotDetail> filterCheckInByKeywords(String keywords, Pageable pageable);

    @Query("""
           select p from ParkingLotDetail p
           where p.isCheckIn = true
           and (upper(p.vehicle.numberPlate) like upper(concat('%', ?1, '%')))
           and p.createdAt between ?2 and ?3
           """)
    Page<ParkingLotDetail> filterCheckInWithDateRange(String keywords, LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    @Query("select p from ParkingLotDetail p where p.isCheckIn = true")
    Page<ParkingLotDetail> findByIsCheckInTrue(Pageable pageable);

    @Query("select (count(p) > 0) from ParkingLotDetail p where p.vehicle.uuid = ?1 and p.isParking = ?2")
    boolean existsByVehicle_UuidAndIsParking(String uuid, Boolean isParking);

    Optional<ParkingLotDetail> findByUuid(String uuid);

    @Query("select p from ParkingLotDetail p where p.parkingLot.uuid = ?1 and p.isParking = ?2")
    Optional<ParkingLotDetail> findByParkingLot_UuidAndIsParking(String uuid, Boolean isParking);

    @Query("select p from ParkingLotDetail p where p.isParking = ?1 and p.vehicle.uuid = ?2")
    Optional<ParkingLotDetail> findByVehicle(Boolean isParking, String uuid);


}
