package com.saatvik.app.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/db_jwt"); // Replace with your database URL
        config.setUsername("root"); // Replace with your database username
        config.setPassword(""); // Replace with your database password
        config.setDriverClassName("com.mysql.cj.jdbc.Driver"); // MySQL driver class

        // Optional: Additional HikariCP settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        config.setConnectionTestQuery("SELECT 1"); // Test query to validate connections
        return new HikariDataSource(config);
    }
}
