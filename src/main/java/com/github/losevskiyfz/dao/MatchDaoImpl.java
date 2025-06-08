package com.github.losevskiyfz.dao;

import com.github.losevskiyfz.entity.Match;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.logging.Logger;

public class MatchDaoImpl implements MatchDao {
    private static final Logger LOG = Logger.getLogger(MatchDaoImpl.class.getName());

    @Override
    public Match save(Match match, EntityManager em) {
        LOG.info(String.format("Saving match with player1: %s, player2: %s, winner: %s", match.getPlayer1(), match.getPlayer2(), match.getWinner()));
        em.persist(match);
        return match;
    }

    @Override
    public List<Match> findByMatchPaged(int pageNumber, int pageSize, String nameFilter, EntityManager em) {

        String idQuery = """
                    SELECT m.id FROM Match m
                    WHERE m.player1.name LIKE :nameFilter OR m.player2.name LIKE :nameFilter
                    ORDER BY m.id DESC
                """;

        List<Integer> ids = em.createQuery(idQuery, Integer.class)
                .setParameter("nameFilter", "%" + nameFilter + "%")
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        if (ids.isEmpty()) return List.of();

        String fullQuery = """
                    SELECT DISTINCT m FROM Match m
                    JOIN FETCH m.player1
                    JOIN FETCH m.player2
                    WHERE m.id IN :ids
                    ORDER BY m.id
                """;
        return em.createQuery(fullQuery, Match.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
