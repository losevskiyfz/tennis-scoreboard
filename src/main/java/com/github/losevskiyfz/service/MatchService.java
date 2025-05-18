package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatchDto;

public interface MatchService {
    CurrentMatchDto newMatch(String player1, String player2);
}
