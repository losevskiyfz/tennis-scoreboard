package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatch;
import com.github.losevskiyfz.entity.Match;

public interface MatchesPersistenceService {
    CurrentMatch newMatch(String player1, String player2);

    Match save(Match match);
}
