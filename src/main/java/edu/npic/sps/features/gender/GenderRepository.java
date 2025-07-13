package edu.npic.sps.features.gender;

import edu.npic.sps.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

   @Transactional
   @Modifying
   @Query(value = "UPDATE users SET gender_id = NULL WHERE gender_id IN (SELECT id FROM genders WHERE uuid = ?1); DELETE FROM genders WHERE uuid = ?1", nativeQuery = true)
   void deleteByUuid(String uuid);

   @Query("select (count(g) > 0) from Gender g where upper(g.gender) = upper(?1) and g.uuid <> ?2")
   boolean existsByGenderIgnoreCaseAndUuidNot(String gender, String uuid);

   @Query("select (count(g) > 0) from Gender g where upper(g.gender) = upper(?1)")
   boolean existsByGenderIgnoreCase(String gender);

   Optional<Gender> findByUuid(String uuid);
}
