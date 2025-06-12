package com.github.losevskiyfz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Matches")
@Getter
@Setter
@Builder
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "Player1", referencedColumnName = "ID")
    private Player player1;
    @ManyToOne
    @JoinColumn(name = "Player2", referencedColumnName = "ID")
    private Player player2;
    @ManyToOne
    @JoinColumn(name = "Winner", referencedColumnName = "ID")
    private Player winner;
}
