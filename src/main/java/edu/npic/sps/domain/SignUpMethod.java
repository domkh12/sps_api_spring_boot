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
@Table(name = "sign_up_methods")
public class SignUpMethod {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @Column(unique = true, nullable = false)
    private String uuid;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "signUpMethod")
    private List<User> users;
}
