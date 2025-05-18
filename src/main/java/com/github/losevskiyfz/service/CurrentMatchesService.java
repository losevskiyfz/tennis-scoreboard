package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatchDto;

import java.util.Optional;
import java.util.UUID;

public interface CurrentMatchesService {
    void addMatch(UUID matchId, CurrentMatchDto currentMatchDto);

    Optional<CurrentMatchDto> removeMatch(UUID matchId);

    Optional<CurrentMatchDto> getMatch(UUID matchId);
}
