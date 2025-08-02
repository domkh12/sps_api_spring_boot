package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "parking_lot_details")
public class ParkingLotDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String uuid;
    private LocalDateTime createdAt;
    @Column(nullable = false, length = 100)
    private LocalDateTime timeIn;
    @Column(length = 100)
    private LocalDateTime timeOut;
    private Boolean isCheckIn;
    private Boolean isCheckOut;
    @Column(nullable = false, length = 100)
    private Boolean isParking;
    private Long durations;
    private String image;
    private String imageCheckIn;
    private String imageCheckOut;

    // relationship
    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private ParkingSpace parkingSpace;

    @ManyToOne
    private ParkingLot parkingLot;

}
