package edu.npic.sps.features.role;

import edu.npic.sps.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("select r from Role r where upper(r.name) <> upper(?1)")
    List<Role> findByNameNotIgnoreCase(String name);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users_roles ur WHERE ur.role_id IN (SELECT id FROM roles r WHERE r.uuid = ?1); DELETE FROM roles r WHERE r.uuid = ?1", nativeQuery = true)
    void deleteByUuid(String uuid);

    Optional<Role> findByUuid(String uuid);

    Optional<Role> findByName(String name);

    Optional<Role> findByNameIgnoreCase(String name);


}
