package com.stocktrading.controller;

import com.stocktrading.model.Transaction;
import com.stocktrading.model.TransactionsResponse;
import com.stocktrading.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{symbol}")
    public TransactionsResponse findBySymbol(@PathVariable("symbol") String symbol) {
        List<Transaction> transactions = transactionService.findBySymbol(symbol);
        TransactionsResponse response = new TransactionsResponse();
        response.setTransactions(transactions);
        return response;
    }
}
