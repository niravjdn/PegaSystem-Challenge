package com.stocktrading.service;

import com.stocktrading.model.Order;
import com.stocktrading.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }
}
