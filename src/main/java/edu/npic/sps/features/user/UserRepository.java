package edu.npic.sps.features.user;

import edu.npic.sps.base.Status;
import edu.npic.sps.domain.Role;
import edu.npic.sps.domain.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("""
            select u from User u inner join u.sites sites inner join u.roles roles
            where sites.uuid in ?1 and roles.name = ?2
            order by u.id DESC""")
    List<User> findBySites_UuidInAndRoles_NameOrderByIdDesc(Collection<String> uuids, String name, Sort sort);

    @Query("""
            select u from User u inner join u.sites sites inner join u.roles roles
            where u.createdAt between ?1 and ?2 and sites.uuid in ?3 and roles.name = ?4
            order by u.id DESC""")
    List<User> findByCreatedAtBetweenAndSites_UuidInAndRoles_NameOrderByIdDesc(LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Collection<String> uuids, String name, Sort sort);

    @Query("""
            select u from User u inner join u.sites sites inner join u.roles roles
            where sites.uuid in ?1 and roles.name = ?2""")
    Page<User> findBySites_UuidInAndRoles_Name(Collection<String> uuids, String name, Pageable pageable);

    @Query("""
            select u from User u inner join u.sites sites inner join u.roles roles
            where u.createdAt between ?1 and ?2 and sites.uuid in ?3 and roles.name = ?4""")
    Page<User> findByCreatedAtBetweenAndSites_UuidInAndRoles_Name(LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Collection<String> uuids, String name, Pageable pageable);


    @Query("select u from User u inner join u.sites sites where u.createdAt between ?1 and ?2 and sites.uuid in ?3")
    Page<User> findByCreatedAtBetweenAndSites_UuidIn(LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Collection<String> uuids, Pageable pageable);

    @Query("select u from User u where u.createdAt between ?1 and ?2")
    Page<User> findByCreatedAtBetween(LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    @Query("select u from User u left join u.sites sites where sites.uuid in ?1")
    Page<User> findBySites_UuidIn(Collection<String> uuids, Pageable pageable);

    @Query("select u from User u left join u.sites sites where sites.uuid in ?1 order by u.id DESC")
    List<User> findBySites_UuidInOrderByIdDesc(Collection<String> uuids, Sort sort);

    @Query("""
            select u from User u inner join u.sites sites
            where u.createdAt between ?1 and ?2 and sites.uuid in ?3
            order by u.id DESC""")
    List<User> findByCreatedAtBetweenAndSites_UuidInOrderByIdDesc(LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Collection<String> uuids, Sort sort);


    @Query("select u from User u where u.createdAt between ?1 and ?2 order by u.id DESC")
    List<User> findByCreatedAtBetweenOrderByIdDesc(LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Sort sort);

    @Query("""
            select count(u) from User u inner join u.sites sites inner join u.roles roles
            where u.uuid <> ?1 and sites.uuid in ?2 and upper(roles.name) = 'USER'""")
    Integer countUserBySite(String uuid, Collection<String> uuids);

    @Query("select count(u) from User u where u.uuid <> ?1")
    Integer countByUuidNot(String uuid);


    @Transactional
    @Modifying
    @Query(value =
            """
            UPDATE vehicles SET user_id = NULL WHERE user_id IN (SELECT id FROM users WHERE uuid = ?1);
            DELETE FROM users_roles WHERE user_id IN (SELECT id FROM users WHERE uuid = ?1);
            DELETE FROM users WHERE uuid = ?1
            """,
            nativeQuery = true)
    void deleteByUuid(String uuid);

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
    Page<User> findUserByRoleManager(String uuid, String uuid1, Pageable pageable);

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
            and roles.name = 'USER' and size(roles) = 1
            """)
    Page<User> filterUserByManager(String uuid, String keyword, Collection<String> uuids, Collection<String> uuids1, String status, Collection<String> branchId, Pageable pageable);

    @Query("""
            select u from User u inner join u.sites sites inner join u.roles roles
            where u.uuid <> ?1 and sites.uuid = ?2 and roles.name = 'USER' and size(roles) = 1""")
    List<User> findAllFullNameBySite(String uuid, String uuid1);


}
