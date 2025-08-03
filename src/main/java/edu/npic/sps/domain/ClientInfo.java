package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client_info")
public class ClientInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String anonymizedIp;
    private String ipAddress;
    private String salt;
    private String loginType;
    private String userAgent;
    private Integer userId;
    private String action;
    private String browser;
    private String browserVersion;
    private String operatingSystem;
    private String operatingSystemVersion;
    private String deviceTye;
    private String rawUserAgent;
    private LocalDateTime createdAt;
}
