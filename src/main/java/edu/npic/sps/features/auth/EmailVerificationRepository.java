package edu.npic.sps.features.auth;

import edu.npic.sps.domain.EmailVerification;
import edu.npic.sps.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findByUser(User user);
}
