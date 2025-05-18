package com.github.losevskiyfz.service;

import com.github.losevskiyfz.cdi.ApplicationContext;
import com.github.losevskiyfz.dao.PlayerDao;
import com.github.losevskiyfz.dto.CurrentMatchDto;
import com.github.losevskiyfz.entity.Player;
import com.github.losevskiyfz.mapper.PlayerMapper;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.util.logging.Logger;

import static com.github.losevskiyfz.utils.TransactionUtils.executeInTransaction;

public class MatchServiceImpl implements MatchService {
    private final ApplicationContext context = ApplicationContext.getInstance();
    private final PlayerDao playerDao = context.resolve(PlayerDao.class);
    private final EntityManagerFactory emf = context.resolve(EntityManagerFactory.class);
    private static final Logger LOG = Logger.getLogger(MatchServiceImpl.class.getName());
    private static final PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    @Override
    public CurrentMatchDto newMatch(String player1, String player2) {
        LOG.info(String.format("Creating new match between %s and %s", player1, player2));
        Player p1 = getOrCreatePlayer(player1);
        Player p2 = getOrCreatePlayer(player2);
        return CurrentMatchDto.builder()
                .p1(playerMapper.toDto(p1))
                .p2(playerMapper.toDto(p2))
                .p1Points(0)
                .p2Points(0)
                .p1Games(0)
                .p2Games(0)
                .p1Sets(0)
                .p2Sets(0)
                .build();
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
