package com.github.losevskiyfz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CurrentMatch {
    private PlayerDto player1;
    private PlayerDto player2;
    List<Set> sets;
    List<Game> games;
    List<Score> scores;
    boolean isTieBreak;
    boolean isOver;
    boolean isOverflow;
}
