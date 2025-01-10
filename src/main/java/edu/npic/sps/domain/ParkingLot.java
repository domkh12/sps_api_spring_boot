package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "parking_lots")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false, length = 50)
    private String lotName;
    @Column(nullable = false)
    private Boolean isAvailable;
    @Column(nullable = false)
    private Boolean isDeleted;
    private String createdBy;

    @OneToMany(mappedBy = "parkingLot")
    private List<ParkingLotDetail> parkingLotDetail;

    @ManyToOne
    private ParkingSpace parkingSpace;
}
