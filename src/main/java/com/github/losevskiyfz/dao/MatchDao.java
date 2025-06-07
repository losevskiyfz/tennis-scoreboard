package com.github.losevskiyfz.dao;

import com.github.losevskiyfz.entity.Match;
import jakarta.persistence.EntityManager;

public interface MatchDao {
    Match save(Match match, EntityManager em);
}
