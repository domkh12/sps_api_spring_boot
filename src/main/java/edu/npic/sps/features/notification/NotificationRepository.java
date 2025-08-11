package edu.npic.sps.features.notification;

import edu.npic.sps.domain.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
}
