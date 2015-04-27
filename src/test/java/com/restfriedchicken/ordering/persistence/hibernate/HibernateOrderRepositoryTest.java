package com.restfriedchicken.ordering.persistence.hibernate;

import com.restfriedchicken.ordering.Application;
import com.restfriedchicken.ordering.core.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static com.restfriedchicken.ordering.core.Order.Status.WAIT_PAYMENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class HibernateOrderRepositoryTest {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private HibernateOrderRepository subject;

    @Test
    public void should_saves_order() throws Exception {

        final String trackingId = "123456";
        final Order order = new Order(trackingId);
        order.append("item1", 1);
        order.append("item2", 2);

        subject.store(order);


        new TransactionTemplate(transactionManager)
                .execute(status -> {

                    final Optional<Order> saved = subject.findByTrackingId(trackingId);
                    assertThat(saved.isPresent(), is(true));
                    final Order loaded = saved.get();
                    assertThat(order.getStatus(), equalTo(WAIT_PAYMENT));
                    assertThat(loaded.getItems().size(), is(2));

                    assertThat(loaded.getItems().get(0).getName(), equalTo("item1"));
                    assertThat(loaded.getItems().get(0).getQuantity(), is(1));

                    assertThat(loaded.getItems().get(1).getName(), equalTo("item2"));
                    assertThat(loaded.getItems().get(1).getQuantity(), is(2));

                    return loaded;
                });
    }


}
