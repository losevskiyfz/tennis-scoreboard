package com.github.losevskiyfz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MatchesRequest {
    private String page;
    private String filterByPlayer;
}