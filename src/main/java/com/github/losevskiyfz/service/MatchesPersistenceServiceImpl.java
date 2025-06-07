package com.github.losevskiyfz.service;

import com.github.losevskiyfz.cdi.ApplicationContext;
import com.github.losevskiyfz.dao.MatchDao;
import com.github.losevskiyfz.dao.PlayerDao;
import com.github.losevskiyfz.dto.CurrentMatch;
import com.github.losevskiyfz.entity.Match;
import com.github.losevskiyfz.entity.Player;
import com.github.losevskiyfz.mapper.PlayerMapper;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.github.losevskiyfz.utils.TransactionUtils.executeInTransaction;

public class MatchesPersistenceServiceImpl implements MatchesPersistenceService {
    private final ApplicationContext context = ApplicationContext.getInstance();
    private final PlayerDao playerDao = context.resolve(PlayerDao.class);
    private final EntityManagerFactory emf = context.resolve(EntityManagerFactory.class);
    private static final Logger LOG = Logger.getLogger(MatchesPersistenceServiceImpl.class.getName());
    private final PlayerMapper mapper = PlayerMapper.INSTANCE;
    private final MatchDao matchDao = context.resolve(MatchDao.class);

    @Override
    public CurrentMatch newMatch(String player1, String player2) {
        LOG.info(String.format("Creating new match between %s and %s", player1, player2));
        Player p1 = getOrCreatePlayer(player1);
        Player p2 = getOrCreatePlayer(player2);
        return CurrentMatch.builder()
                .player1(mapper.toDto(p1))
                .player2(mapper.toDto(p2))
                .scores(new ArrayList<>())
                .games(new ArrayList<>())
                .sets(new ArrayList<>())
                .isTieBreak(false)
                .isOverflow(false)
                .build();
    }

    @Override
    public Match save(Match match) {
        LOG.info(String.format("Persisting match between %s and %s, winner: %s", match.getPlayer1(), match.getPlayer2(), match.getWinner()));
        return executeInTransaction(
                emf,
                em -> matchDao.save(match, em)
        );
    }

    @Override
    public List<Match> findByNamePaged(int pageNumber, int pageSize, String nameFilter) {
        LOG.info(String.format("Looking for matches, pageNumber:%s, pageSize:%s, nameFilter:%s", pageNumber, pageSize, nameFilter));
        return executeInTransaction(
                emf,
                em -> matchDao.findByMatchPaged(pageNumber, pageSize, nameFilter, em)
        );
    }

    private Player getOrCreatePlayer(String playerName) {
        try {
            return executeInTransaction(
                    emf,
                    em -> playerDao.save(Player.builder().name(playerName).build(), em)
            );
        } catch (ConstraintViolationException e) {
            LOG.warning(String.format("Tried to create player %s that already exists", playerName));
            return executeInTransaction(
                    emf,
                    em -> playerDao.findByName(playerName, em)
            );
        }
    }
}
