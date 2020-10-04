package com.stocktrading.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsResponse {

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Transaction> transactions;

}
