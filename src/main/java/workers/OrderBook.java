package workers;

import com.stocktrading.enums.OrderStateEnum;
import com.stocktrading.enums.OrderType;
import com.stocktrading.model.Order;
import com.stocktrading.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class OrderBook {

    //Sorted maps to keep bid and ask orders by map
    //bids are kept in descending order of price
    //asks are kept in acending order of price
    Map<Double, List<Order>> bidOrderMap;
    Map<Double, List<Order>> askOrderMap;

    public Map<Double, List<Order>> getBidOrderMap() {
        return bidOrderMap;
    }

    //make the TreeMaps thread safe for concurrency
    public OrderBook() {
        bidOrderMap = Collections.synchronizedSortedMap(new TreeMap(Collections.reverseOrder()));
        askOrderMap = Collections.synchronizedSortedMap(new TreeMap());

    }

    //add order to the book
    public void addOrder(Order order) {
        if (order.getOrder_type() == OrderType.BUY) {
            addOrder(bidOrderMap, order);

        } else {
            addOrder(askOrderMap, order);
        }

    }

    //cancel order request, remove the order from orderbook
    public void deleteOrder(Order order) {
        if (order.getOrder_type() == OrderType.BUY) {
            removeOrder(bidOrderMap, order);
        } else {
            removeOrder(askOrderMap, order);
        }
    }

    //execute the order in the order book
    public List<Transaction> executeOrder(Order order) {
        if (order.getOrder_type() == OrderType.SELL) {
            return matchOrders(bidOrderMap, order);
        } else {
            return matchOrders(askOrderMap, order);

        }
    }

    //method for matching bids and asks
    private List<Transaction> matchOrders(Map<Double, List<Order>> m, Order order) {
        int quantity = order.getUnits();
        boolean foundMatch = false;
        List<Transaction> matchedOrders = new ArrayList<Transaction>();
        for (Map.Entry<Double, List<Order>> entry : m.entrySet()) {
            Double price = entry.getKey();
            List<Order> orderList = entry.getValue();
            if (matchPrice(price, order) && quantity > 0) {
                List<Order> removelist = new ArrayList();
                for (Order bid : orderList) {
                    quantity = quantity - (bid.getUnits() - bid.getExecuted_quantity());
                    foundMatch = true;
                    if (quantity == 0) { // perfect match
                        Transaction executionRecord = createExecutionRecord(order, bid, order.getPrice());
                        bid.setExecuted_quantity(bid.getUnits());
                        bid.setOrderStatus(OrderStateEnum.EXECUTED);
                        order.setExecuted_quantity(order.getUnits());
                        order.setOrderStatus(OrderStateEnum.EXECUTED);
                        matchedOrders.add(executionRecord);
                        removelist.add(bid);
                        break;
                    } else if (quantity < 0) {
                        Transaction executionRecord = createExecutionRecord(order, bid, order.getPrice());
                        bid.setExecuted_quantity(bid.getUnits() + quantity);
                        bid.setOrderStatus(OrderStateEnum.PARTIAL_MATCH);
                        order.setExecuted_quantity(order.getUnits());
                        order.setOrderStatus(OrderStateEnum.EXECUTED);
                        matchedOrders.add(executionRecord);						removedMatched(order);
                        break; // order full filled no need to search more
                    } else { // more quantities left
                        Transaction executionRecord = createExecutionRecord(order, bid, order.getPrice());
                        bid.setExecuted_quantity(bid.getUnits());
                        order.setExecuted_quantity(order.getUnits() - quantity);
                        bid.setOrderStatus(OrderStateEnum.EXECUTED);
                        matchedOrders.add(executionRecord);						removelist.add(bid);
                    }

                }
                // remove matched orders from the order book
                orderList.removeAll(removelist);

            } else {
                return matchedOrders;
            }
        }
        if (quantity > 0 && foundMatch) {
            order.setExecuted_quantity(order.getUnits() - quantity);
            order.setOrderStatus(OrderStateEnum.PARTIAL_MATCH);
        }
        return matchedOrders;

    }

    //keep record of bid, ask match pair, create entry in Order_Execution_Record
    private Transaction createExecutionRecord(Order order1, Order order2, Double price){
        Transaction executionRecord = new Transaction();
        if (order1.getOrder_type() == OrderType.BUY) {
            executionRecord.setBuyOrder(order1);
            executionRecord.setSellOrder(order2);
            executionRecord.setPrice(price);
        } else {
            executionRecord.setBuyOrder(order2);
            executionRecord.setSellOrder(order1);
            executionRecord.setPrice(price);
        }
        int qtyAvailable1 = order1.getUnits() - order1.getExecuted_quantity();
        int qtyAvailable2 = order2.getUnits() - order2.getExecuted_quantity();
        executionRecord.setUnits(Math.min(qtyAvailable1, qtyAvailable2));
        executionRecord.setStock_symbol(order1.getStock_symbol());
        return executionRecord;
    }

    //remote the matched order from the order book
    private void removedMatched(Order order) {
        Map<Double, List<Order>> orderMap;
        if (order.getOrder_type() == OrderType.BUY) {
            orderMap = bidOrderMap;
        } else {
            orderMap = askOrderMap;
        }

        List<Order> orderList = orderMap.get(order.getPrice());
        if (orderList != null) {
            orderList.remove(order);
        }

    }

    //check if the price matches for the order
    private boolean matchPrice(Double price, Order o2) {
        if (o2.getOrder_type() == OrderType.BUY) {
            if (price <= o2.getPrice())
                return true;
            return false;
        } else {
            if (price >= o2.getPrice())
                return true;
            return false;
        }
    }

    //add order in the order book
    private void addOrder(Map<Double, List<Order>> m, Order order) {
        List<Order> orderList = m.get(order.getPrice());
        if (orderList == null) {
            orderList = new LinkedList<Order>();
            m.put(order.getPrice(), orderList);
        }
        orderList.add(order);
    }

    //remove order for the order book
    private void removeOrder(Map<Double, List<Order>> m, Order order) {
        List<Order> orderList = m.get(order.getPrice());
        if (orderList != null) {
            orderList.remove(order);
        }
    }

}