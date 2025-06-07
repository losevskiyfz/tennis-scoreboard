package com.github.losevskiyfz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class MatchScoreModel {
    private String p1Scores;
    private String p2Scores;
    private String p1Games;
    private String p2Games;
    private String p1Sets;
    private String p2Sets;
    private String p1Name;
    private String p2Name;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        MatchScoreModel that = (MatchScoreModel) object;
        return Objects.equals(p1Scores, that.p1Scores) && Objects.equals(p2Scores, that.p2Scores) && Objects.equals(p1Games, that.p1Games) && Objects.equals(p2Games, that.p2Games) && Objects.equals(p1Sets, that.p1Sets) && Objects.equals(p2Sets, that.p2Sets) && Objects.equals(p1Name, that.p1Name) && Objects.equals(p2Name, that.p2Name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(p1Scores);
        result = 31 * result + Objects.hashCode(p2Scores);
        result = 31 * result + Objects.hashCode(p1Games);
        result = 31 * result + Objects.hashCode(p2Games);
        result = 31 * result + Objects.hashCode(p1Sets);
        result = 31 * result + Objects.hashCode(p2Sets);
        result = 31 * result + Objects.hashCode(p1Name);
        result = 31 * result + Objects.hashCode(p2Name);
        return result;
    }
}
