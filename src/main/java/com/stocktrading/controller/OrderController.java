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
            logger.info(order.toString());
            Order placedOrder = orderService.save(order);
            return new ResponseEntity<Order>(placedOrder, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is an issue with placing an order.");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/place/{id}", produces = "application/json")
    public ResponseEntity<HttpStatus> updateOrder(@PathVariable("id") String id, @RequestBody Order order) {
        Optional<Order> o = orderService.findById(id);

        if (o.isPresent()) {
            Order _order = o.get();
            _order.setOrder_time(new Date());
            _order.setPrice(order.getPrice());
            _order.setUnits(order.getUnits());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") String id) {
        try {
            orderService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findByID(@PathVariable("id") String id) {
        try {
           Optional<Order> o = orderService.findById(id);
           if(o.isPresent()){
               return new ResponseEntity<>(o.get(),HttpStatus.OK);
           }
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
