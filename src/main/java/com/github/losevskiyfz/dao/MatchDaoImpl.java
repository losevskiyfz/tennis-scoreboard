package com.github.losevskiyfz.dao;

import com.github.losevskiyfz.entity.Match;
import jakarta.persistence.EntityManager;

import java.util.logging.Logger;

public class MatchDaoImpl implements MatchDao{
    private static final Logger LOG = Logger.getLogger(MatchDaoImpl.class.getName());

    @Override
    public Match save(Match match, EntityManager em) {
        LOG.info(String.format("Saving match with player1: %s, player2: %s, winner: %s", match.getPlayer1(), match.getPlayer2(), match.getWinner()));
        em.persist(match);
        return match;
    }
}
