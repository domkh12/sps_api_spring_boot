package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String uuid;
    @Column(unique = true, length = 50)
    private String numberPlate;

    // optional
    @Column(length = 200)
    private String licensePlateKhName;
    @Column(length = 200)
    private String licensePlateEngName;
    private String color;
    private String vehicleModel;
    private String vehicleMake;
    private String vehicleDescription;
    private String image;

    //system generated
    private LocalDateTime createdAt;
    private Boolean isDeleted;

    // relationship
    @OneToMany(mappedBy = "vehicle")
    private List<ParkingLotDetail> parkingLotDetail;

    @ManyToOne
    private VehicleType vehicleType;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "vehicle_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "site_id", referencedColumnName = "id")
    )
    private List<Site> sites;

}
