package com.github.losevskiyfz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CurrentMatchDto {
    private PlayerDto p1;
    private PlayerDto p2;
    private Integer p1Points;
    private Integer p2Points;
    private Integer p1Sets;
    private Integer p2Sets;
    private Integer p1Games;
    private Integer p2Games;
}
