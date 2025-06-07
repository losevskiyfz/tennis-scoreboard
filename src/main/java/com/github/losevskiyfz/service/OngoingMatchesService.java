package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatch;

import java.util.Optional;
import java.util.UUID;

public interface OngoingMatchesService {
    void put(UUID matchId, CurrentMatch currentMatchDto);
    Optional<CurrentMatch> removeMatch(UUID matchId);
    Optional<CurrentMatch> get(UUID matchId);
}
