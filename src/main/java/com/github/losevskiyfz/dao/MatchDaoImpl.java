package com.github.losevskiyfz.dao;

import com.github.losevskiyfz.entity.Match;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.logging.Logger;

public class MatchDaoImpl implements MatchDao{
    private static final Logger LOG = Logger.getLogger(MatchDaoImpl.class.getName());

    @Override
    public Match save(Match match, EntityManager em) {
        LOG.info(String.format("Saving match with player1: %s, player2: %s, winner: %s", match.getPlayer1(), match.getPlayer2(), match.getWinner()));
        em.persist(match);
        return match;
    }

    @Override
    public List<Match> findByMatchPaged(int pageNumber, int pageSize, String nameFilter, EntityManager em) {
        String hql = "FROM Match m WHERE m.player1.name LIKE :nameFilter OR m.player2.name LIKE :nameFilter ORDER BY m.ID";
        return em.createQuery(hql, Match.class)
                .setParameter("nameFilter", "%" + nameFilter + "%")
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
