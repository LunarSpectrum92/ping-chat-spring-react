package com.Konopka.AuthService.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;

    @Column(unique = true, nullable = false)
    private String username;


}
