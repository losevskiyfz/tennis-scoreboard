package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.*;

import java.util.ArrayList;
import java.util.List;

public class MatchScoreCalculationServiceImpl implements MatchScoreCalculationService {

    public MatchScoreCalculationServiceImpl() {
    }

    @Override
    public CurrentMatch addScore(CurrentMatch match, PlayerNumber winner) {
        if (isMatchOver(match)) return match;
        match.getScores().add(Score.builder().winner(winner).build());
        scoreBoardOverflowCheck(match);
        return renderMatch(match);
    }

    private CurrentMatch renderMatch(CurrentMatch match) {
        int p1Scores = wonScoresForPlayer(match, PlayerNumber.ONE);
        int p2Scores = wonScoresForPlayer(match, PlayerNumber.TWO);
        if (!match.isTieBreak()){
            if (p1Scores >= 4 && p1Scores - p2Scores >= 2) {
                resetScores(match);
                match.getGames().add(Game.builder().winner(PlayerNumber.ONE).build());
                return renderGames(match);
            }
            if (p2Scores >= 4 && p2Scores - p1Scores >= 2) {
                resetScores(match);
                match.getGames().add(Game.builder().winner(PlayerNumber.TWO).build());
                return renderGames(match);
            }
        } else {
            if (match.isOverflow()){
                if (p1Scores - p2Scores >= 2){
                    resetScores(match);
                    match.getGames().add(Game.builder().winner(PlayerNumber.ONE).build());
                    match.setOverflow(false);
                    match.setTieBreak(false);
                    return renderGames(match);
                }
                if (p2Scores - p1Scores >= 2){
                    resetScores(match);
                    match.getGames().add(Game.builder().winner(PlayerNumber.TWO).build());
                    match.setOverflow(false);
                    match.setTieBreak(false);
                    return renderGames(match);
                }
            } else {
                if (p1Scores >= 7 && p1Scores - p2Scores >= 2) {
                    resetScores(match);
                    match.getGames().add(Game.builder().winner(PlayerNumber.ONE).build());
                    match.setTieBreak(false);
                    return renderGames(match);
                }
                if (p2Scores >= 7 && p2Scores - p1Scores >= 2) {
                    resetScores(match);
                    match.getGames().add(Game.builder().winner(PlayerNumber.TWO).build());
                    match.setTieBreak(false);
                    return renderGames(match);
                }
            }
        }
        return match;
    }

    private CurrentMatch renderGames(CurrentMatch match) {
        int p1Games = wonGamesForPlayer(match, PlayerNumber.ONE);
        int p2Games = wonGamesForPlayer(match, PlayerNumber.TWO);
        if (!match.isTieBreak() && p1Games == 6 && p2Games == 6) {
            match.setTieBreak(true);
            resetScores(match);
            return match;
        }
        if ((p1Games >= 6 && p1Games - p2Games >= 2) || p1Games == 7) {
            resetGames(match);
            match.getSets().add(Set.builder().winner(PlayerNumber.ONE).build());
            return match;
        }

        if ((p2Games >= 6 && p2Games - p1Games >= 2) || p2Games == 7) {
            resetGames(match);
            match.getSets().add(Set.builder().winner(PlayerNumber.TWO).build());
            return match;
        }
        return match;
    }

    private boolean isMatchOver(CurrentMatch currentMatch) {
        int p1Sets = wonSetsForPlayer(currentMatch, PlayerNumber.ONE);
        int p2Sets = wonSetsForPlayer(currentMatch, PlayerNumber.TWO);
        return p1Sets == 2 || p2Sets == 2;
    }

    private void resetScores(CurrentMatch currentMatch) {
        currentMatch.setScores(new ArrayList<>());
    }

    private void resetGames(CurrentMatch currentMatch) {
        currentMatch.setGames(new ArrayList<>());
    }

    private int wonSetsForPlayer(CurrentMatch match, PlayerNumber playerNumber) {
        return Math.toIntExact(match.getSets().stream()
                .filter(set -> set.getWinner().equals(playerNumber))
                .count());
    }

    private int wonGamesForPlayer(CurrentMatch match, PlayerNumber playerNumber) {
        return Math.toIntExact(match.getGames().stream()
                .filter(game -> game.getWinner().equals(playerNumber))
                .count());
    }

    private int wonScoresForPlayer(CurrentMatch match, PlayerNumber playerNumber) {
        return Math.toIntExact(match.getScores().stream()
                .filter(score -> score.getWinner().equals(playerNumber))
                .count());
    }

    private void scoreBoardOverflowCheck(CurrentMatch match) {
        if (match.isTieBreak() && wonScoresForPlayer(match, PlayerNumber.ONE) == 100){
            match.setScores(
                    new ArrayList<>(List.of(Score.builder().winner(PlayerNumber.ONE).build()))
            );
            match.setOverflow(true);
        }
        if (match.isTieBreak() && wonScoresForPlayer(match, PlayerNumber.TWO) == 100){
            match.setScores(
                    new ArrayList<>(List.of(Score.builder().winner(PlayerNumber.TWO).build()))
            );
            match.setOverflow(true);
        }
    }
}
