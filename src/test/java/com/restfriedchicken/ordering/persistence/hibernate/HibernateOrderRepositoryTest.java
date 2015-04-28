package com.restfriedchicken.ordering.persistence.hibernate;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.restfriedchicken.ordering.Application;
import com.restfriedchicken.ordering.core.Order;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DbUnitTestExecutionListener.class, FlywayTestExecutionListener.class})
@FlywayTest
public class HibernateOrderRepositoryTest {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private HibernateOrderRepository subject;

    @ExpectedDatabase(value="classpath:order_save_expected.xml", assertionMode= NON_STRICT_UNORDERED)
    @Test
    public void should_saves_order() throws Exception {

        final String trackingId = "123456";
        final Order order = new Order(trackingId);
        order.append("item1", 1);
        order.append("item2", 2);

        subject.store(order);
    }


}
