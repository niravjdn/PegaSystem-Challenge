package com.stocktrading.controller;

import com.stocktrading.model.Order;
import com.stocktrading.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;


@RestController
@RequestMapping("/order")
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.POST, value = "/place", produces = "application/json")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        try {
            System.out.println(order.toString());
            Order placedOrder = orderService.save(order);
            return new ResponseEntity<Order>(placedOrder, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is an issue with placing an order.");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/place/{id}", produces = "application/json")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") String id, @RequestBody Order order) {
        Optional<Order> tutorialData = orderService.findById(id);

        if (tutorialData.isPresent()) {
            Order _order = tutorialData.get();
            _order.setOrder_time(new Date());
            _order.setPrice(order.getPrice());
            _order.setUnits(order.getUnits());
            return new ResponseEntity<>(orderService.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") String id) {
        try {
            orderService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
