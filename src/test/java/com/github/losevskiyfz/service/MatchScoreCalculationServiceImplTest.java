package com.github.losevskiyfz.service;

import com.github.losevskiyfz.dto.*;
import com.github.losevskiyfz.mapper.MatchMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MatchScoreCalculationServiceImplTest {
    private final MatchScoreCalculationService scoreService = new MatchScoreCalculationServiceImpl();
    private final PlayerDto p1 = PlayerDto.builder().id(1).name("Jake Smith").build();
    private final PlayerDto p2 = PlayerDto.builder().id(1).name("Alan Green").build();
    private final MatchMapper mapper = MatchMapper.INSTANCE;

    @Test
    void playerAdvantageInOnePointAfterFortyTest() {
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(
                        Stream.concat(
                                IntStream.range(0, 10)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 10)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .games(new ArrayList<>())
                .sets(new ArrayList<>())
                .isTieBreak(false)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("AD")
                .p2Scores("40")
                .p1Games("0")
                .p2Games("0")
                .p1Sets("0")
                .p2Sets("0")
                .build();

        scoreService.addScore(match, PlayerNumber.ONE);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }

    @Test
    void playerAdvantageOfTwoPointsBeforeFortyTest() {
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(
                        Stream.concat(
                                IntStream.range(0, 2)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 1)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .games(new ArrayList<>())
                .sets(new ArrayList<>())
                .isTieBreak(false)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("40")
                .p2Scores("15")
                .p1Games("0")
                .p2Games("0")
                .p1Sets("0")
                .p2Sets("0")
                .build();

        scoreService.addScore(match, PlayerNumber.ONE);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }

    @Test
    void twoPlayersGotEqualScoresTest() {
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(
                        Stream.concat(
                                IntStream.range(0, 10)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 9)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .games(new ArrayList<>())
                .sets(new ArrayList<>())
                .isTieBreak(false)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("40")
                .p2Scores("40")
                .p1Games("0")
                .p2Games("0")
                .p1Sets("0")
                .p2Sets("0")
                .build();

        scoreService.addScore(match, PlayerNumber.TWO);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }

    @Test
    void tieBreakIsStartedTest() {
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(
                        Stream.concat(
                                IntStream.range(0, 7)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .games(
                        Stream.concat(
                                IntStream.range(0, 5)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .sets(new ArrayList<>())
                .isTieBreak(false)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("1")
                .p2Scores("0")
                .p1Games("6")
                .p2Games("6")
                .p1Sets("0")
                .p2Sets("0")
                .build();

        scoreService.addScore(match, PlayerNumber.ONE);
        scoreService.addScore(match, PlayerNumber.ONE);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }

    @Test
    void overflowHandledTest() {
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(
                        Stream.concat(
                                IntStream.range(0, 99)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 99)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .games(
                        Stream.concat(
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .sets(new ArrayList<>())
                .isTieBreak(true)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("1")
                .p2Scores("0")
                .p1Games("6")
                .p2Games("6")
                .p1Sets("0")
                .p2Sets("0")
                .build();

        scoreService.addScore(match, PlayerNumber.ONE);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }

    @Test
    void overflowAdvantageTwoPointsTest() {
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(
                        Stream.concat(
                                IntStream.range(0, 99)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 99)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .games(
                        Stream.concat(
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .sets(new ArrayList<>())
                .isTieBreak(true)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("0")
                .p2Scores("0")
                .p1Games("0")
                .p2Games("0")
                .p1Sets("1")
                .p2Sets("0")
                .build();

        scoreService.addScore(match, PlayerNumber.ONE);
        scoreService.addScore(match, PlayerNumber.ONE);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }

    @Test
    void twoPointsAdvantageInTieBreakAfterSixPointsTest() {
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(
                        Stream.concat(
                                IntStream.range(0, 5)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .games(
                        Stream.concat(
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .sets(new ArrayList<>())
                .isTieBreak(true)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("0")
                .p2Scores("0")
                .p1Games("0")
                .p2Games("0")
                .p1Sets("0")
                .p2Sets("1")
                .build();

        scoreService.addScore(match, PlayerNumber.TWO);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }

    @Test
    void twoPointsAdvantageInTieBreakBeforeSixPointsTest() {
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(
                        Stream.concat(
                                IntStream.range(0, 3)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 4)
                                        .mapToObj(i -> Score.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .games(
                        Stream.concat(
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 6)
                                        .mapToObj(i -> Game.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .sets(new ArrayList<>())
                .isTieBreak(true)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("3")
                .p2Scores("5")
                .p1Games("6")
                .p2Games("6")
                .p1Sets("0")
                .p2Sets("0")
                .build();

        scoreService.addScore(match, PlayerNumber.TWO);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }

    @Test
    void gameEndedTest(){
        CurrentMatch match = CurrentMatch.builder()
                .player1(p1)
                .player2(p2)
                .scores(new ArrayList<>())
                .games(new ArrayList<>())
                .sets(
                        Stream.concat(
                                IntStream.range(0, 1)
                                        .mapToObj(i -> Set.builder().winner(PlayerNumber.ONE).build()),
                                IntStream.range(0, 2)
                                        .mapToObj(i -> Set.builder().winner(PlayerNumber.TWO).build())
                        ).collect(Collectors.toList())
                )
                .isTieBreak(false)
                .isOverflow(false)
                .isOver(false)
                .build();

        MatchScoreModel expected = MatchScoreModel.builder()
                .p1Name(p1.getName())
                .p2Name(p2.getName())
                .p1Scores("0")
                .p2Scores("0")
                .p1Games("0")
                .p2Games("0")
                .p1Sets("1")
                .p2Sets("2")
                .build();

        scoreService.addScore(match, PlayerNumber.ONE);
        scoreService.addScore(match, PlayerNumber.TWO);
        assertEquals(expected, mapper.toMatchScoreModel(match));
    }
}