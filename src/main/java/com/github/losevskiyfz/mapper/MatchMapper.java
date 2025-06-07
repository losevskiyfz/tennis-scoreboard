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
    @Mapping(source = "scores", target = "p1Scores", qualifiedByName = "countScoresP1")
    @Mapping(source = "scores", target = "p2Scores", qualifiedByName = "countScoresP2")
    @Mapping(source = "games", target = "p1Games", qualifiedByName = "countGamesP1")
    @Mapping(source = "games", target = "p2Games", qualifiedByName = "countGamesP2")
    @Mapping(source = "sets", target = "p1Sets", qualifiedByName = "countSetsP1")
    @Mapping(source = "sets", target = "p2Sets", qualifiedByName = "countSetsP2")
    MatchScoreModel toMatchScoreModel(CurrentMatch match);

    @Named("countScoresP1")
    default String countScoresP1(List<Score> scores) {
        return String.valueOf(scores.stream().filter(s -> s.getWinner() == PlayerNumber.ONE).count());
    }

    @Named("countScoresP2")
    default String countScoresP2(List<Score> scores) {
        return String.valueOf(scores.stream().filter(s -> s.getWinner() == PlayerNumber.TWO).count());
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
