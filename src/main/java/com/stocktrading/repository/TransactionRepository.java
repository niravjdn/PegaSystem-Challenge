package com.stocktrading.repository;

import com.stocktrading.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query(
            value = "SELECT * FROM TRANSACTIONS t WHERE t.stock_symbol = :stock_symbol",
            nativeQuery = true)
    Collection<Transaction> findByStockSymbol(@Param("stock_symbol") String stock_symbol);

}