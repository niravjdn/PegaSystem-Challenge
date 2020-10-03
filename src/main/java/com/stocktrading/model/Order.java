package com.stocktrading.model;

import com.stocktrading.enums.OrderStateEnum;
import com.stocktrading.enums.OrderType;
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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    private String user_id;

    private String stock_symbol;

    @Enumerated(EnumType.STRING)
    private OrderType order_type;

    @Enumerated(EnumType.STRING)
    private OrderStateEnum orderStatus = OrderStateEnum.NEW;

    private int units;

    private double price;

    private int executed_quantity;

    @Builder.Default
    private Date order_time = new Date();
}
