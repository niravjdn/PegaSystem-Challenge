package com.stocktrading.stocktradingdemo;

import com.stocktrading.enums.OrderType;
import com.stocktrading.model.Order;
import com.stocktrading.model.Transaction;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderMatching {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;


    @Before
    public void setUp() {
        RestAssured.port = port;
        baseUrl = "http://localhost:" + port + "/orders";
    }


    @Test
    public void matchOrders() {

        // order 1
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "BID");
        map.add("quantity", "10");
        map.add("price", "200");
        map.add("symbol", "AAPL");
        Order result = restTemplate.postForObject(baseUrl, map, Order.class);

        // order 2
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "BID");
        map.add("quantity", "20");
        map.add("price", "150");
        map.add("symbol", "AAPL");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        // order 3
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "BID");
        map.add("quantity", "60");
        map.add("price", "145");
        map.add("symbol", "AAPL");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        // order 4
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "BID");
        map.add("quantity", "50");
        map.add("price", "130");
        map.add("symbol", "AAPL");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        // order 5
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "BID");
        map.add("quantity", "50");
        map.add("price", "130");
        map.add("symbol", "MSFT");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        // -----------------------------------------------------------------
        // order 1
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "ASK");
        map.add("quantity", "20");
        map.add("price", "90");
        map.add("symbol", "AAPL");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        // order 2
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "ASK");
        map.add("quantity", "20");
        map.add("price", "95");
        map.add("symbol", "AAPL");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        // order 3
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "ASK");
        map.add("quantity", "45");
        map.add("price", "100");
        map.add("symbol", "AAPL");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        // order 4
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "ASK");
        map.add("quantity", "50");
        map.add("price", "120");
        map.add("symbol", "AAPL");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        // order 4
        map = new LinkedMultiValueMap<String, String>();
        map.add("orderCategory", "LIMIT");
        map.add("orderType", "ASK");
        map.add("quantity", "50");
        map.add("price", "130");
        map.add("symbol", "MSFT");
        result = restTemplate.postForObject(baseUrl, map, Order.class);

        Transaction aaplResult = restTemplate.postForObject(baseUrl + "/execute/AAPL", map,
                Transaction.class);

        Transaction msftResult = restTemplate.postForObject(baseUrl + "/execute/MSFT", map,
                Transaction.class);

//        assertEquals(aaplResult.getTotalTrades().intValue(), 7); //there should be 7 trades for AAPL
//        assertEquals(msftResult.getTotalTrades().intValue(), 1); //there should be 1 trade for MSFT

    }


}
