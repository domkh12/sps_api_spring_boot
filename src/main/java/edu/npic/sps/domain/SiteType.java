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
@Table(name = "site_types")
public class SiteType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String uuid;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private LocalDateTime createdAt;

//    relationship
    @OneToMany(mappedBy = "siteType")
    private List<Site> sites;
}
