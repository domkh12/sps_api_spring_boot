package edu.npic.sps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String uuid;
    @Column(nullable = false, unique = true)
    private String companyName;
    @Column(nullable = false)
    private Integer siteQty;
    private String companyAddress;
    private LocalDate establishedDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private String image;

//    relationship
    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    private List<Site> sites;

    @ManyToOne
    private CompanyType companyType;

    @ManyToOne
    private City city;
}
