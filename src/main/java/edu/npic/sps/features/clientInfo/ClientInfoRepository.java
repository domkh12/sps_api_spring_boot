package edu.npic.sps.features.clientInfo;

import edu.npic.sps.domain.ClientInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientInfoRepository extends JpaRepository<ClientInfo, Integer> {
    @Query("select c from ClientInfo c where c.userId = ?1")
    Page<ClientInfo> findByUserId(Integer userId, Pageable pageable);

}
