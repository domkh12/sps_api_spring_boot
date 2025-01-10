package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.domain.ParkingLotDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingLotDetailRepository extends JpaRepository<ParkingLotDetail, Integer> {
    Optional<ParkingLotDetail> findByUuid(String uuid);
}
