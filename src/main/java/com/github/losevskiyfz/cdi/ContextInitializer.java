package com.github.losevskiyfz.cdi;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;
import java.util.logging.Logger;

@WebListener
public class ContextInitializer implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(ContextInitializer.class.getName());
    private final ApplicationContext context = ApplicationContext.getInstance();

    private void initializeApplicationContext() throws SQLException, ClassNotFoundException {
        context.register(
                EntityManagerFactory.class,
                Persistence.createEntityManagerFactory("tennisScoreboardPU")
        );
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            initializeApplicationContext();
        } catch (Exception e) {
            LOG.severe(e.getMessage());
            throw new RuntimeException(e);
        }
        LOG.info("Application context is initialized");
    }
}