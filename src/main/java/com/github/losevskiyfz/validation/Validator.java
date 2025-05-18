package com.github.losevskiyfz.validation;

import com.github.losevskiyfz.dto.TwoPlayersRequest;

public class Validator {
    public void validate(Object obj) {
        if (obj instanceof TwoPlayersRequest) {
            validatePlayerPair((TwoPlayersRequest) obj);
        }
    }

    private void validatePlayerPair(TwoPlayersRequest twoPlayersRequest) {
        validatePlayerName(twoPlayersRequest.getPlayer1(), "1");
        validatePlayerName(twoPlayersRequest.getPlayer2(), "2");
        if (twoPlayersRequest.getPlayer1().equals(twoPlayersRequest.getPlayer2())) {
            throw new IllegalArgumentException("Player can't play with himself");
        }
    }

    private void validatePlayerName(String playerName, String playerNum) {
        if (playerName == null || playerName.isEmpty()) {
            throw new IllegalArgumentException("Player %s name cannot be empty".formatted(playerNum));
        }
        if (playerName.length() < 3 || playerName.length() > 50) {
            throw new IllegalArgumentException("Player %s name must be 2–50 characters long".formatted(playerNum));
        }
        if (!playerName.matches("^[\\p{L}'\\- ]+$")) {
            throw new IllegalArgumentException("Player %s name may contain only letters, apostrophes, hyphens, and spaces".formatted(playerNum));
        }
        if (!playerName.matches("^\\p{L}.*\\p{L}$")) {
            throw new IllegalArgumentException("Player %s name must start and end with a letter".formatted(playerNum));
        }
        if (playerName.matches(".*[ '\\-]{2,}.*")) {
            throw new IllegalArgumentException("Player %s name may not contain consecutive non-letter characters".formatted(playerNum));
        }
    }
}


