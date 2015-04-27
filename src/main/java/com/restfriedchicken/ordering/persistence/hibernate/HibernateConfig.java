package com.restfriedchicken.ordering.persistence.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {


    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactory(DataSource dataSrouce) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSrouce);
        sessionFactory.setPackagesToScan(new String[]{"com.restfriedchicken.ordering.core"});
        return sessionFactory;
    }

}
