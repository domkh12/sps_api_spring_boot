package edu.npic.sps.domain;

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
@Table(name = "license_plate_provinces")
public class LicensePlateProvince {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String uuid;
    @Column(unique = true, nullable = false)
    private String provinceNameKh;
    @Column(unique = true, nullable = false)
    private String provinceNameEn;
    @Column(nullable = false)
    private LocalDateTime createdAt;

//  relationship
    @OneToMany(mappedBy = "licensePlateProvince")
    private List<Vehicle> vehicles;
}
