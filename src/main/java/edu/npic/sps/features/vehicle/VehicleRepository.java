package edu.npic.sps.features.vehicle;

import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.domain.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    Optional<Vehicle> findByUuid(String uuid);

    void deleteByVehicleType(VehicleType vehicleType);

    Boolean existsByNumberPlate(String numberPlate);

    Optional<Vehicle> findByNumberPlate(String numberPlate);

    Page<Vehicle> findBySites_Uuid(String uuid, Pageable pageable);


}

