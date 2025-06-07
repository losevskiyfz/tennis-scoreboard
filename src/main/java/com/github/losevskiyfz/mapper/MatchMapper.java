package com.github.losevskiyfz.mapper;

import com.github.losevskiyfz.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(source = "player1.name", target = "p1Name")
    @Mapping(source = "player2.name", target = "p2Name")
    @Mapping(source = "match", target = "p1Scores", qualifiedByName = "mapP1Score")
    @Mapping(source = "match", target = "p2Scores", qualifiedByName = "mapP2Score")
    @Mapping(source = "games", target = "p1Games", qualifiedByName = "countGamesP1")
    @Mapping(source = "games", target = "p2Games", qualifiedByName = "countGamesP2")
    @Mapping(source = "sets", target = "p1Sets", qualifiedByName = "countSetsP1")
    @Mapping(source = "sets", target = "p2Sets", qualifiedByName = "countSetsP2")
    MatchScoreModel toMatchScoreModel(CurrentMatch match);

    List<String> POINTS = List.of("0", "15", "30", "40");

    @Named("mapP1Score")
    default String mapP1Score(CurrentMatch match) {
        int p1 = (int) match.getScores().stream().filter(s -> s.getWinner() == PlayerNumber.ONE).count();
        int p2 = (int) match.getScores().stream().filter(s -> s.getWinner() == PlayerNumber.TWO).count();

        if (match.isTieBreak()) {
            return String.valueOf(p1);
        }

        if (p1 >= 3 && p2 >= 3) {
            if (p1 == p2) return "40";
            if (p1 > p2) return "AD";
            return "40";
        }

        return p1 < POINTS.size() ? POINTS.get(p1) : "40";
    }

    @Named("mapP2Score")
    default String mapP2Score(CurrentMatch match) {
        int p1 = (int) match.getScores().stream().filter(s -> s.getWinner() == PlayerNumber.ONE).count();
        int p2 = (int) match.getScores().stream().filter(s -> s.getWinner() == PlayerNumber.TWO).count();

        if (match.isTieBreak()) {
            return String.valueOf(p2);
        }

        if (p1 >= 3 && p2 >= 3) {
            if (p1 == p2) return "40";
            if (p1 > p2) return "40";
            return "AD";
        }

        return p2 < POINTS.size() ? POINTS.get(p2) : "40";
    }

    @Named("countGamesP1")
    default String countGamesP1(List<Game> games) {
        return String.valueOf(games.stream().filter(g -> g.getWinner() == PlayerNumber.ONE).count());
    }

    @Named("countGamesP2")
    default String countGamesP2(List<Game> games) {
        return String.valueOf(games.stream().filter(g -> g.getWinner() == PlayerNumber.TWO).count());
    }

    @Named("countSetsP1")
    default String countSetsP1(List<Set> sets) {
        return String.valueOf(sets.stream().filter(s -> s.getWinner() == PlayerNumber.ONE).count());
    }

    @Named("countSetsP2")
    default String countSetsP2(List<Set> sets) {
        return String.valueOf(sets.stream().filter(s -> s.getWinner() == PlayerNumber.TWO).count());
    }
}
