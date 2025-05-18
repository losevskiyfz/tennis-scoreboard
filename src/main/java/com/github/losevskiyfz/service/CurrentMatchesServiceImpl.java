package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatchDto;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class CurrentMatchesServiceImpl implements CurrentMatchesService {
    private final Map<UUID, CurrentMatchDto> currentMatches = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(CurrentMatchesServiceImpl.class.getName());

    @Override
    public void addMatch(UUID matchId, CurrentMatchDto currentMatchDto) {
        LOG.info(String.format("Adding match with id %s", matchId));
        currentMatches.put(matchId, currentMatchDto);
    }
    @Override
    public Optional<CurrentMatchDto> removeMatch(UUID matchId) {
        LOG.info(String.format("Removing match with id %s", matchId));
        return Optional.ofNullable(currentMatches.remove(matchId));
    }

    @Override
    public Optional<CurrentMatchDto> getMatch(UUID matchId) {
        LOG.info(String.format("Getting current match with id %s", matchId));
        return Optional.ofNullable(currentMatches.get(matchId));
    }
}