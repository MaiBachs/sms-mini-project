package com.consumer.sms.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import com.consumer.sms.util.Constant;
import java.util.Properties;

public class DbPool {
    private static HikariDataSource ds;
    public static void initialize(Properties props) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty(Constant.Property.DB_URL));
            config.setUsername(props.getProperty(Constant.Property.DB_USERNAME));
            config.setPassword(props.getProperty(Constant.Property.DB_PASSWORD));
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty(Constant.Property.POOL_MAXIMUM_POOL_SIZE, "10")));
            ds = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DB pool", e);
        }
    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    public static void close() {
        if (ds != null) ds.close();
    }
}
