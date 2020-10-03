package com.stocktrading.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", nullable = false)
    Order buyOrder;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", nullable = false)
    Order sellOrder;

    private String stock_symbol;

    private int units;

    private double price;

    @Builder.Default
    private Date order_time = new Date();
}
