package com.restfriedchicken.ordering.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
@PropertySource(value = "classpath:datasource.properties")
public class PersistenceConfig {


    @Bean
    @ConfigurationProperties(prefix="datasource")
    public DataSource dataSource() {
        return new BasicDataSource();
    }
}
