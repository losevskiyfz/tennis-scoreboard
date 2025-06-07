package com.github.losevskiyfz.validation;

import com.github.losevskiyfz.dto.ScoreRequest;
import com.github.losevskiyfz.dto.TwoPlayersRequest;
import com.github.losevskiyfz.dto.UuidRequest;
import com.github.losevskiyfz.exception.BadPlayerNameException;
import com.github.losevskiyfz.exception.BadScoreRequestException;

import java.util.Objects;
import java.util.UUID;

public class Validator {
    public void validate(Object obj) {
        if (obj instanceof TwoPlayersRequest) {
            validatePlayerPair((TwoPlayersRequest) obj);
        } else if (obj instanceof ScoreRequest) {
            validateScoreRequest((ScoreRequest) obj);
        } else if (obj instanceof UuidRequest){
            validateUuidRequest((UuidRequest) obj);
        }
    }

    private void validateUuidRequest(UuidRequest request){
        if (request.getUuid() == null || request.getUuid().isEmpty()){
            throw new IllegalArgumentException("uuid param is not provided");
        }
        try {
            UUID.fromString(request.getUuid());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Provided uuid is not valid");
        }
    }

    private void validateScoreRequest(ScoreRequest request) {
        if (request.getPlayerNumber() == null || request.getPlayerNumber().isEmpty()){
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
            throw new BadPlayerNameException("Player %s name must be 2–50 characters long".formatted(playerNum));
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


