package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.CurrentMatch;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class OngoingMatchesServiceImpl implements OngoingMatchesService {
    private final Map<UUID, CurrentMatch> currentMatches = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(OngoingMatchesServiceImpl.class.getName());

    @Override
    public void put(UUID matchId, CurrentMatch currentMatchDto) {
        LOG.info(String.format("Adding match with id %s", matchId));
        currentMatches.put(matchId, currentMatchDto);
    }
    @Override
    public Optional<CurrentMatch> removeMatch(UUID matchId) {
        LOG.info(String.format("Removing match with id %s", matchId));
        return Optional.ofNullable(currentMatches.remove(matchId));
    }

    @Override
    public Optional<CurrentMatch> get(UUID matchId) {
        LOG.info(String.format("Getting current match with id %s", matchId));
        return Optional.ofNullable(currentMatches.get(matchId));
    }
}