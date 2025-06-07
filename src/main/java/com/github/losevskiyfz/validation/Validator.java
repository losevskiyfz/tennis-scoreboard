package com.github.losevskiyfz.validation;

import com.github.losevskiyfz.dto.MatchesRequest;
import com.github.losevskiyfz.dto.ScoreRequest;
import com.github.losevskiyfz.dto.TwoPlayersRequest;
import com.github.losevskiyfz.dto.UuidRequest;
import com.github.losevskiyfz.exception.BadPlayerNameException;
import com.github.losevskiyfz.exception.BadScoreRequestException;

import java.util.Objects;
import java.util.UUID;

public class Validator {
    private static final int PLAYER_NAME_LENGTH_MIN = 2;
    private static final int PLAYER_NAME_LENGTH_MAX = 50;

    public void validate(Object obj) {
        if (obj instanceof TwoPlayersRequest) {
            validatePlayerPair((TwoPlayersRequest) obj);
        } else if (obj instanceof ScoreRequest) {
            validateScoreRequest((ScoreRequest) obj);
        } else if (obj instanceof UuidRequest) {
            validateUuidRequest((UuidRequest) obj);
        } else if (obj instanceof MatchesRequest) {
            validateMatchesRequest((MatchesRequest) obj);
        }
    }

    private void validateMatchesRequest(MatchesRequest request) {
        if (request.getPage() == null || request.getPage().isEmpty()) {
            throw new IllegalArgumentException("page is not provided");
        }
        try {
            Integer.parseInt(request.getPage());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("can't parse page number %s".formatted(request.getPage()));
        }
        int page = Integer.parseInt(request.getPage());
        if (page < 1) {
            throw new IllegalArgumentException("page number can't be less than 1");
        }
        if (request.getFilterByPlayer() == null) {
            throw new IllegalArgumentException("search player filter is not provided");
        }
    }

    private void validateUuidRequest(UuidRequest request) {
        if (request.getUuid() == null || request.getUuid().isEmpty()) {
            throw new IllegalArgumentException("uuid param is not provided");
        }
        try {
            UUID.fromString(request.getUuid());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided uuid is not valid");
        }
    }

    private void validateScoreRequest(ScoreRequest request) {
        if (request.getPlayerNumber() == null || request.getPlayerNumber().isEmpty()) {
            throw new BadScoreRequestException("playerNumber param is not provided");
        }
        if (!Objects.equals(request.getPlayerNumber(), "1") && !Objects.equals(request.getPlayerNumber(), "2")) {
            throw new BadScoreRequestException("Player number may be only 1 or 2");
        }
    }

    private void validatePlayerPair(TwoPlayersRequest twoPlayersRequest) {
        validatePlayerName(twoPlayersRequest.getPlayer1(), "1");
        validatePlayerName(twoPlayersRequest.getPlayer2(), "2");
        if (twoPlayersRequest.getPlayer1().equals(twoPlayersRequest.getPlayer2())) {
            throw new BadPlayerNameException("Player can't play with himself");
        }
    }

    private void validatePlayerName(String playerName, String playerNum) {
        if (playerName == null || playerName.isEmpty()) {
            throw new BadPlayerNameException("Player %s name cannot be empty".formatted(playerNum));
        }
        if (playerName.length() < 3 || playerName.length() > 50) {
            throw new BadPlayerNameException("Player %s name must be %s–%s characters long".formatted(playerNum, PLAYER_NAME_LENGTH_MIN, PLAYER_NAME_LENGTH_MAX));
        }
        if (!playerName.matches("^[\\p{L}'\\- ]+$")) {
            throw new BadPlayerNameException("Player %s name may contain only letters, apostrophes, hyphens, and spaces".formatted(playerNum));
        }
        if (!playerName.matches("^\\p{L}.*\\p{L}$")) {
            throw new BadPlayerNameException("Player %s name must start and end with a letter".formatted(playerNum));
        }
        if (playerName.matches(".*[ '\\-]{2,}.*")) {
            throw new BadPlayerNameException("Player %s name may not contain consecutive non-letter characters".formatted(playerNum));
        }
    }
}


