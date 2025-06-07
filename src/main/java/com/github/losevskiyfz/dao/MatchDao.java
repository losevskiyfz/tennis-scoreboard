package com.github.losevskiyfz.dao;

import com.github.losevskiyfz.entity.Match;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface MatchDao {
    Match save(Match match, EntityManager em);

    List<Match> findByMatchPaged(int pageNumber, int pageSize, String nameFilter, EntityManager em);
}
