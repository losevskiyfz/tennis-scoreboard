package com.github.losevskiyfz.conf;

import java.util.HashMap;

public class DbProperties extends HashMap<String, String> {
    public DbProperties() {
        put("jakarta.persistence.jdbc.user", PropertiesProvider.get("jakarta.persistence.jdbc.user"));
        put("jakarta.persistence.jdbc.url", PropertiesProvider.get("jakarta.persistence.jdbc.url"));
        put("jakarta.persistence.jdbc.password", PropertiesProvider.get("jakarta.persistence.jdbc.password"));
        String passwordEnv = System.getenv("H2_DB_PASSWORD");
        if (passwordEnv != null) {
            put("jakarta.persistence.jdbc.password", passwordEnv);
        }
    }
}
