package com.stocktrading.stocktradingdemo;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ OrderBookRestTest.class, OrderMatchingTest.class })
public class MainTestSuite {
}

