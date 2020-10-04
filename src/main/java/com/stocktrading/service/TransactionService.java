package com.stocktrading.service;

import com.stocktrading.model.Transaction;
import com.stocktrading.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional (readOnly = true)
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

}
