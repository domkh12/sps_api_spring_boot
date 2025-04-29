package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.domain.ParkingLotDetail;
import edu.npic.sps.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingLotDetailRepository extends JpaRepository<ParkingLotDetail, Integer> {

    @Query("select (count(p) > 0) from ParkingLotDetail p where p.vehicle.uuid = ?1 and p.isParking = ?2")
    boolean existsByVehicle_UuidAndIsParking(String uuid, Boolean isParking);


    Optional<ParkingLotDetail> findByUuid(String uuid);

    @Query("select p from ParkingLotDetail p where p.parkingLot.uuid = ?1 and p.isParking = ?2")
    Optional<ParkingLotDetail> findByParkingLot_UuidAndIsParking(String uuid, Boolean isParking);


    @Query("select p from ParkingLotDetail p where p.isParking = ?1 and p.vehicle.uuid = ?2")
    Optional<ParkingLotDetail> findByVehicle(Boolean isParking, String uuid);


}
