package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "vehicle_types")
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    String uuid;
    @Column(nullable = false, length = 250)
    private String name;
    @Column(unique = true, nullable = false,length = 250)
    private String alias;
    private String image;

    // relationship
    @OneToMany(mappedBy = "vehicleType")
    private List<Vehicle> vehicle;
}
