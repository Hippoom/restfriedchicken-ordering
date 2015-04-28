package com.restfriedchicken.ordering.persistence.hibernate;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.restfriedchicken.ordering.Application;
import com.restfriedchicken.ordering.core.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class HibernateOrderRepositoryTest {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private HibernateOrderRepository subject;

    @ExpectedDatabase("classpath:order_save_expected.xml")
    @Test
    public void should_saves_order() throws Exception {

        final String trackingId = "123456";
        final Order order = new Order(trackingId);
        order.append("item1", 1);
        order.append("item2", 2);

        subject.store(order);
    }


}
