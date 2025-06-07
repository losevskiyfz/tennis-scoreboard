package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatch;
import com.github.losevskiyfz.dto.PlayerNumber;

public interface MatchScoreCalculationService {
    CurrentMatch addScore(CurrentMatch match, PlayerNumber scoreWinner);
}
