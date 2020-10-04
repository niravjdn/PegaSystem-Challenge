package com.stocktrading.stocktradingdemo;

import com.stocktrading.enums.OrderType;
import com.stocktrading.model.Order;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderBookRestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    private String orderID;

    @Before
    public void setUp() {
        RestAssured.port = port;
        baseUrl = "http://localhost:" + port + "/orders";
        Order o = restTemplate.getForEntity(baseUrl+"/"+orderID, Order.class).getBody();
        Order result = restTemplate.postForObject(baseUrl, o, Order.class);
        orderID = result.getId();
    }

    @Test
    public void createOrder() {
        Order o = createOrderObject();
        Order result = restTemplate.postForObject(baseUrl, o, Order.class);
        assertEquals(result.getUnits(), 10);
        assertEquals(result.getStock_symbol(), "AAPL");
        orderID = result.getId();
    }

    @Test
    public void updateOrder() {
        createOrder();
        Order placedOrder = restTemplate.getForEntity(baseUrl+"/"+orderID, Order.class).getBody();
        placedOrder.setPrice(220);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", orderID);
        restTemplate.put(baseUrl+"/"+orderID, placedOrder, Order.class);
        assertEquals(restTemplate.getForEntity(baseUrl+"/"+orderID, Order.class).getBody().getPrice(), 220, 0.001);
    }

    @Test
    public void deleteOrder() {
        createOrder();
        restTemplate.delete(baseUrl+"/"+orderID);
        ResponseEntity<Order> deletedOrder = restTemplate.getForEntity(baseUrl+"/"+orderID, Order.class);
        assertEquals(deletedOrder.getStatusCode(), HttpStatus.NOT_FOUND);
    }


    private Order createOrderObject() {
        Order order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(10);
        order.setStock_symbol("AAPL");
        order.setPrice(100);
        order.setOrder_type(OrderType.BUY);
        return order;
    }
}
