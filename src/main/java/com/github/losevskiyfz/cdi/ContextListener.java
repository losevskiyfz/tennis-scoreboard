package com.github.losevskiyfz.cdi;

import com.github.losevskiyfz.dao.PlayerDao;
import com.github.losevskiyfz.dao.PlayerDaoImpl;
import com.github.losevskiyfz.service.*;
import com.github.losevskiyfz.validation.Validator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Logger;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(ContextListener.class.getName());
    private final ApplicationContext context = ApplicationContext.getInstance();

    private void initializeApplicationContext() {
        context.register(
                EntityManagerFactory.class,
                Persistence.createEntityManagerFactory("tennisScoreboardPU")
        );
        context.register(
                PlayerDao.class,
                new PlayerDaoImpl()
        );
        context.register(
                MatchesPersistenceService.class,
                new MatchesPersistenceServiceImpl()
        );
        context.register(
                OngoingMatchesService.class,
                new OngoingMatchesServiceImpl()
        );
        context.register(
                MatchScoreCalculationService.class,
                new MatchScoreCalculationServiceImpl()
        );
        context.register(
                Validator.class,
                new Validator()
        );
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initializeApplicationContext();
        LOG.info("Application context is initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        context.resolve(EntityManagerFactory.class).close();
    }
}