package edu.npic.sps.features.city;

import edu.npic.sps.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE sites SET city_id = NULL WHERE city_id IN (SELECT id FROM cities WHERE uuid = ?1); DELETE FROM cities WHERE uuid = ?1", nativeQuery = true)
    void deleteBySitesNullAndUuid(String uuid);

    Optional<City> findByUuid(String uuid);
}
