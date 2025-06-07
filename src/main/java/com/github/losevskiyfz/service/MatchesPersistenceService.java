package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatch;

public interface MatchesPersistenceService {
    CurrentMatch newMatch(String player1, String player2);
}
