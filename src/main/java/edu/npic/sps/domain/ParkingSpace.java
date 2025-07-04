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
@Table(name = "parking_spaces")
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false, length = 200)
    private String label;
    @Column(nullable = false)
    private Integer lotQty;

    //optional
    private String image;
    private Integer filled;
    private Integer empty;

    private LocalDateTime createdAt;
    private Boolean isDeleted;

    // relationship
    @OneToMany(mappedBy = "parkingSpace")
    private List<ParkingLot> parkingLots;

    @OneToMany(mappedBy = "parkingSpace")
    private List<ParkingLotDetail> parkingLotDetail;

    @ManyToOne
    private Site site;
}
