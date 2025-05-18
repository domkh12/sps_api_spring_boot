package edu.npic.sps.features.vehicletype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.npic.sps.domain.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer> {

    Optional<VehicleType> findByUuid(String uuid);
    Boolean existsByAlias(String alias);
    Optional<VehicleType> findByAlias(String alias);

    @Modifying
    @Query("UPDATE Vehicle AS v SET v.vehicleType = null WHERE v.vehicleType = ?1")
    void unsetVehicleType(VehicleType vehicleType);
}
