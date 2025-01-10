package edu.npic.sps.features.signUpMethod;

import edu.npic.sps.domain.SignUpMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignUpMethodRepository extends JpaRepository<SignUpMethod, Integer> {
    Optional<SignUpMethod> findByName(String name);
    Boolean existsByName(String name);
}
