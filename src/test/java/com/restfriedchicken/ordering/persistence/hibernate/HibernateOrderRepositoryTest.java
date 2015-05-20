package com.restfriedchicken.ordering.persistence.hibernate;

import com.github.hippoom.xunittemplate.springtestdbunit.dataset.CreateTemplateModifier;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.restfriedchicken.ordering.Application;
import com.restfriedchicken.ordering.core.Order;
import com.restfriedchicken.ordering.core.OrderRepository;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.modelmapper.config.Configuration.AccessLevel.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DbUnitTestExecutionListener.class, FlywayTestExecutionListener.class})
@FlywayTest(invokeCleanDB = false)
public class HibernateOrderRepositoryTest {

    public static final String TRACKING_ID_OF_PROTOTYPE = "240eff2f-6c38-4998-9287-2e447dac4fd3";
    public static final String TRACKING_ID_OF_TO_BE_SAVED = "240eff2f-6c38-4998-9287-2e447dac4fd4";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private OrderRepository subject;


    @DatabaseSetup("classpath:order_save_template.xml")
    @ExpectedDatabase(value = "classpath:order_save_template.xml",
            assertionMode = NON_STRICT_UNORDERED,
            modifiers = {OrderCreateTemplateModifier.class})
    @Test
    public void should_saves_order() throws Exception {

        final Order toBeSaved = new TransactionTemplate(transactionManager)
                .execute(status -> {
                    final Optional<Order> orderMaybe = subject.findByTrackingId(TRACKING_ID_OF_PROTOTYPE);
                    return clone(orderMaybe.get(), TRACKING_ID_OF_TO_BE_SAVED);
                });

        subject.store(toBeSaved);
    }

    private class OrderCreateTemplateModifier extends CreateTemplateModifier {

        public OrderCreateTemplateModifier() {
            super("tracking_id", TRACKING_ID_OF_TO_BE_SAVED);
        }
    }

    private Order clone(Order prototype, String newTrackingId) {
        ModelMapper mapper = new ModelMapper();

        PropertyMap<Order, Order> orderMap = new PropertyMap<Order, Order>() {
            protected void configure() {
                map(newTrackingId, destination.getTrackingId());
            }
        };
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        mapper.addMappings(orderMap);

        return mapper.map(prototype, Order.class);
    }
}
