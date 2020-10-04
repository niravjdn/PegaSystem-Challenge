package com.stocktrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@SpringBootApplication
public class StockTradingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockTradingDemoApplication.class, args);
	}
}
