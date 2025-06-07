package com.github.losevskiyfz.dao;

import com.github.losevskiyfz.entity.Player;
import jakarta.persistence.EntityManager;

public interface PlayerDao {
    Player save(Player player, EntityManager em);
    Player findByName(String name, EntityManager em);
}
