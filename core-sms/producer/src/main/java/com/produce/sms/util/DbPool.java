package com.produce.sms.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;

public class DbPool {
    private static HikariDataSource ds;
    public static void initialize() {
        try {
            Properties props = new Properties();
            try (InputStream in = DbPool.class.getClassLoader().getResourceAsStream("application.properties")) {
                props.load(in);
            }
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            ds = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DB pool", e);
        }
    }
    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            initialize();
        }
        return ds.getConnection();
    }
    public static void close() {
        if (ds != null) ds.close();
    }
}
