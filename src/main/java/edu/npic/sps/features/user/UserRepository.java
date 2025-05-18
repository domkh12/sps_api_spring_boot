package edu.npic.sps.features.user;

import edu.npic.sps.base.Status;
import edu.npic.sps.domain.Role;
import edu.npic.sps.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    String countByCreatedAt(LocalDateTime createdAt);

    @Query("""
            select count(u) from User u inner join u.sites sites inner join u.roles roles
            where sites.uuid = ?1 and roles.name = 'USER' and size(roles) = 1 and upper(u.status) = upper('Banned')""")
    Integer countBannedUserBySite(String uuid);

    @Query("""
            select count(u) from User u inner join u.sites sites inner join u.roles roles
            where sites.uuid = ?1 and roles.name = 'USER' and size(roles) = 1 and upper(u.status) = upper('Pending')""")
    Integer countPendingUserBySite(String uuid);

    @Query("""
            select count(u) from User u inner join u.sites sites inner join u.roles roles
            where u.uuid <> ?1 and sites.uuid = ?2 and roles.name = 'USER' and size(roles) = 1 and upper(u.status) = upper('Active')""")
    Integer countActiveUserBySite(String uuid, String uuid1);

    long countBySites_Uuid(String uuid);

    Optional<User> findByUuid(String uuid);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Page<User> findByUuidNot(String uuid, Pageable pageable);

    @Query("select count(u) from User u where upper(u.status) = upper('Pending')")
    Integer countPendingUser();

    @Query("select count(u) from User u where upper(u.status) = upper('Banned')")
    Integer countBannedUser();

    @Query("select count(u) from User u where u.uuid <> ?1 and upper(u.status) = upper('Active')")
    Integer countActiveUser(String uuid);

    @Query("""
            select u from User u inner join u.sites sites inner join u.roles roles
            where u.uuid <> ?1 and sites.uuid = ?2 and roles.name = 'USER' and size(roles) = 1""")
    Page<User> findUserByRoleAdmin(String uuid, String uuid1, Pageable pageable);

    @Query("""
            select u from User u inner join u.sites sites inner join u.roles roles
            where u.uuid <> ?1
            and (
            upper(u.fullName) like upper(concat('%', ?2, '%'))
            or upper(u.email) like upper(concat('%', ?2, '%'))
            or upper(u.phoneNumber) like upper(concat('%', ?2, '%'))
            )
            and (roles.uuid in ?3 or ?3 is null)
            and (u.signUpMethod.uuid in ?4 or ?4 is null)
            and (upper(u.status) = upper(?5) or ?5 = '')
            and (sites.uuid in ?6 or ?6 is null)
            """)
    Page<User> findUserByFilter(String uuid, String keyword, Collection<String> uuids, Collection<String> uuids1, String status, Collection<String> branchId, Pageable pageable);

    @Query("""
            select u from User u inner join u.sites sites inner join u.roles roles
            where u.uuid <> ?1 and sites.uuid = ?2 and roles.name = 'USER' and size(roles) = 1""")
    List<User> findAllFullNameBySite(String uuid, String uuid1);


}
