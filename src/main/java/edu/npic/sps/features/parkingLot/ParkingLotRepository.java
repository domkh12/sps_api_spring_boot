package edu.npic.sps.features.parkingLot;

import edu.npic.sps.domain.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {

    Optional<ParkingLot> findByUuid(String uuid);

//    @Query("select count(p) from ParkingLot p where p.parking.id = ?1")
//    int FindAllParkingById(Integer id);
//
//    Optional<ParkingLot> findBySlotName(String slotName);


}
