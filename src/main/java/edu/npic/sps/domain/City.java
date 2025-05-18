package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cities")
public class City implements Serializable {

    @Serial
    private static final long serialVersionUID = 6527855645691638321L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false, unique = true)
    private String name;

//    relationship
    @OneToMany(mappedBy = "city")
    List<Site> sites;
}
