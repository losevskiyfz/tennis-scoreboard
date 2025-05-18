package com.github.losevskiyfz.dao;
import com.github.losevskiyfz.entity.Player;
import jakarta.persistence.EntityManager;

import java.util.logging.Logger;

public class PlayerDaoImpl implements PlayerDao {
    private static final Logger LOG = Logger.getLogger(PlayerDaoImpl.class.getName());

    @Override
    public Player save(Player player, EntityManager em) {
        LOG.info(String.format("Saving player %s", player.getName()));
        em.persist(player);
        return player;
    }

    @Override
    public Player findByName(String name, EntityManager em) {
        LOG.info(String.format("Finding player %s", name));
        return em.createQuery(
                        "SELECT p FROM Player p WHERE p.name = :name", Player.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
