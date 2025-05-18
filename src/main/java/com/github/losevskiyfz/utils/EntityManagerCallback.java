package com.github.losevskiyfz.utils;

import jakarta.persistence.EntityManager;

@FunctionalInterface
public interface EntityManagerCallback<R> {
    R execute(EntityManager em);
}