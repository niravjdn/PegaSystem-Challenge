package com.stocktrading.service;

import com.stocktrading.controller.OrderController;
import com.stocktrading.model.Order;
import com.stocktrading.model.Transaction;
import com.stocktrading.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import workers.OrderBook;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private static Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private TransactionService transactionService;

    private static Map<String, OrderBook> orderBooks = new HashMap<>();

    @Value("${isExecuteOnCreate}")
    private boolean isExecuteOnCreate;

    @Transactional
    public Order save(Order order) {
        Order placedOrder = orderRepository.save(order);
        OrderBook orderBook = orderBooks.get(order.getStock_symbol());
        if (orderBook == null) {
            orderBook = new OrderBook();
            orderBooks.put(order.getStock_symbol(), orderBook);
        }

        orderBook.addOrder(order);

        if (isExecuteOnCreate)
            executeOrder(orderBook,order);

        return placedOrder;
    }

    @Transactional
    public void deleteById(String id) {
        Optional<Order> o = orderRepository.findById(id);
        orderRepository.deleteById(id);
        if(o.isPresent()){
            OrderBook orderBook = orderBooks.get(o.get().getStock_symbol());
            orderBook.deleteOrder(o.get());
        }
    }

    @Transactional
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    private List<Transaction> executeOrder(OrderBook orderBook, Order o) {
        List<Transaction> matchedOrders = orderBook.executeOrder(o);
        logger.info(matchedOrders.toString());
        for (Transaction transaction : matchedOrders) {
            transactionService.save(transaction);
            orderRepository.save(transaction.getSellOrder());
            orderRepository.save(transaction.getBuyOrder());
        }

        return matchedOrders;
    }



}
