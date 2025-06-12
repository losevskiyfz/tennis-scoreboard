package com.github.losevskiyfz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Players")
@Getter
@Setter
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "Name", unique = true)
    private String name;
}
