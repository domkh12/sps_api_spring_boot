package edu.npic.sps.features.parkingLot;

import edu.npic.sps.domain.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {

    long countByParkingSpace_Uuid(String uuid);

    @Query("select p from ParkingLot p where p.parkingSpace.uuid = ?1")
    List<ParkingLot> findAllLotByParkingSpaceUuid(String uuid);


    Optional<ParkingLot> findByUuid(String uuid);

    @Query("select p from ParkingLot p where p.parkingSpace.uuid = ?1")
    List<ParkingLot> findByParkingSpaceUuid(String uuid);


}
