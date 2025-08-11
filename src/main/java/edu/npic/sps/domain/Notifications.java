package edu.npic.sps.domain;

import edu.npic.sps.base.NotificationStatus;
import edu.npic.sps.base.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private NotificationType type;
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.UNREAD;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    private List<User> users;
}
