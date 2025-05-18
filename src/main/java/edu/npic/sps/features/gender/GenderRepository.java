package edu.npic.sps.features.gender;

import edu.npic.sps.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {
   Optional<Gender> findByUuid(String uuid);
}
