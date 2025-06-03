package edu.npic.sps.features.signUpMethod;

import edu.npic.sps.domain.SignUpMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SignUpMethodRepository extends JpaRepository<SignUpMethod, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET sign_up_method_id = null WHERE sign_up_method_id IN (SELECT id FROM sign_up_methods WHERE uuid = ?1); DELETE FROM sign_up_methods WHERE uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);


    @Query("select (count(s) > 0) from SignUpMethod s where upper(s.name) = upper(?1)")
    boolean existsByNameIgnoreCase(String name);

    Optional<SignUpMethod> findByName(String name);

    Boolean existsByName(String name);

    Optional<SignUpMethod> findByUuid(String uuid);
}
