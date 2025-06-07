package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatch;
import com.github.losevskiyfz.entity.Match;

import java.util.List;

public interface MatchesPersistenceService {
    CurrentMatch newMatch(String player1, String player2);

    Match save(Match match);

    List<Match> findByNamePaged(int pageNumber, int pageSize, String nameFilter);
}
