package com.github.losevskiyfz.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.logging.Logger;

public class TransactionUtils {
    private static final Logger LOG = Logger.getLogger(TransactionUtils.class.getName());

    public static <R> R executeInTransaction(EntityManagerFactory emf, EntityManagerCallback<R> callback) {
        EntityManager em = emf.createEntityManager();
        try {
            LOG.info("Begin transaction");
            em.getTransaction().begin();
            R result = callback.execute(em);
            em.getTransaction().commit();
            LOG.info("Transaction committed");
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                LOG.warning("Rolling back transaction");
                em.getTransaction().rollback();
            }
            LOG.warning(e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }
}