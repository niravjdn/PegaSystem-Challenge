package com.stocktrading.stocktradingdemo;

import com.stocktrading.enums.OrderType;
import com.stocktrading.model.Order;
import com.stocktrading.model.Transaction;
import com.stocktrading.model.TransactionsResponse;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderMatchingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrlForOrders;

    private String baseUrlForTransactions;


    @Before
    public void setUp() {
        RestAssured.port = port;
        baseUrlForOrders = "http://localhost:" + port + "/orders";
        baseUrlForTransactions = "http://localhost:" + port + "/transactions";
    }


    @Test
    public void matchOrders() {

        // order 1
        Order order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(10);
        order.setStock_symbol("AAPL");
        order.setPrice(200);
        order.setOrder_type(OrderType.BUY);
        Order result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // order 2
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(20);
        order.setStock_symbol("AAPL");
        order.setPrice(150);
        order.setOrder_type(OrderType.BUY);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // order 3
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(60);
        order.setStock_symbol("AAPL");
        order.setPrice(145);
        order.setOrder_type(OrderType.BUY);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // order 4
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(50);
        order.setStock_symbol("AAPL");
        order.setPrice(130);
        order.setOrder_type(OrderType.BUY);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // order 5
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(50);
        order.setStock_symbol("MSFT");
        order.setPrice(130);
        order.setOrder_type(OrderType.BUY);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // -----------------------------------------------------------------
        // order 1
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(20);
        order.setStock_symbol("AAPL");
        order.setPrice(90);
        order.setOrder_type(OrderType.SELL);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // order 2
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(20);
        order.setStock_symbol("AAPL");
        order.setPrice(95);
        order.setOrder_type(OrderType.SELL);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // order 3
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(45);
        order.setStock_symbol("AAPL");
        order.setPrice(100);
        order.setOrder_type(OrderType.SELL);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // order 4
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(50);
        order.setStock_symbol("AAPL");
        order.setPrice(120);
        order.setOrder_type(OrderType.SELL);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);

        // order 4
        order = new Order();
        order.setUser_id("niravjdn");
        order.setUnits(50);
        order.setStock_symbol("MSFT");
        order.setPrice(130);
        order.setOrder_type(OrderType.SELL);
        result = restTemplate.postForObject(baseUrlForOrders, order, Order.class);


//        assertEquals(aaplResult.getUnits().intValue(), 7); //there should be 7 trades for AAPL
//        assertEquals(msftResult.getUnits().intValue(), 1); //there should be 1 trade for MSFT

        TransactionsResponse AAPLTransactions = restTemplate.getForObject(baseUrlForTransactions+"/{company}", TransactionsResponse.class, "AAPL");
        TransactionsResponse MSFTTransactions = restTemplate.getForObject(baseUrlForTransactions+"/{company}", TransactionsResponse.class, "MSFT");
        assertEquals(AAPLTransactions.getTransactions().size(),7);
        assertEquals(MSFTTransactions.getTransactions().size(),1);
    }


}
