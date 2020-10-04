package com.stocktrading.controller;

import com.stocktrading.model.Transaction;
import com.stocktrading.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }
}
