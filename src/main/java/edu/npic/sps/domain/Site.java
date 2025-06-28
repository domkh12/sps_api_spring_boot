package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@Entity
@Table(name = "branches")
@AllArgsConstructor
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String uuid;
    @Column(unique = true, nullable = false)
    private String siteName;
    @Column(nullable = false)
    private String siteAddress;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private String image;
    @Column(nullable = false)
    private Integer parkingSpacesQty;

//    relationship

    @ManyToOne
    private City city;

    @ManyToOne
    private SiteType siteType;

    @ManyToOne
    private Company company;

    @ManyToMany(mappedBy = "sites")
    private List<User> users;

    @OneToMany(mappedBy = "site", cascade = CascadeType.REMOVE)
    private List<ParkingSpace> parkingSpaces;

    @ManyToMany(mappedBy = "sites")
    private List<Vehicle> vehicles;
}
