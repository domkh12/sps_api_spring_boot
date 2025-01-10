package edu.npic.sps.features.role;

import edu.npic.sps.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    Optional<Role> findByUuid(String uuid);

    Optional<Role> findByNameIgnoreCase(String name);


}
