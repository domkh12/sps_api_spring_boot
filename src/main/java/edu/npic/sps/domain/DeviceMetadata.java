package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity(name = "device_meta_data")
public class DeviceMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Long userId;
    private String deviceDetails;
    private String location;
    private Date lastLoggedIn;
    private String ipAddress;
}
