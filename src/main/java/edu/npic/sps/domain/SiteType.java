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
@Table(name = "site_types")
public class SiteType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false, unique = true)
    private String name;

//    relationship
    @OneToMany(mappedBy = "siteType")
    private List<Site> sites;
}
