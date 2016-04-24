package ar.fi.uba.trackerman.domains;

import java.util.ArrayList;
import java.util.List;

public class OrdersSearchResult {
    long offset;
    long total;
    List<Order> orders;

    public OrdersSearchResult(){
        offset=0;
        total=0;
        orders= new ArrayList<>();
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public long getOffset() {
        return offset;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
