package com.restfriedchicken.ordering.persistence.hibernate;

import com.restfriedchicken.ordering.core.Order;
import com.restfriedchicken.ordering.core.OrderRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public class HibernateOrderRepository implements OrderRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<Order> findByTrackingId(String trackingId) {
        Order load = (Order) sessionFactory.getCurrentSession().byId(Order.class).load(trackingId);
        return Optional.ofNullable(load);
    }

    @Override
    public void store(Order order) {
        sessionFactory.getCurrentSession().save(order);
    }
}
