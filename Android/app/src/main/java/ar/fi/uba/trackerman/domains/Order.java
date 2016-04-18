package ar.fi.uba.trackerman.domains;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by plucadei on 17/4/16.
 */
public class Order {

    private long id;
    private long clientId;
    private long vendorId;
    private Date deliveryDate;
    private Date dateCreated;
    private String status;
    private double totalPrice;
    private String currency;
    private List<OrderItem> orderItems;

    public Order(long id, long clientId, long vendorId, String dateCreated, String status, double totalPrice, String currency){
        this.id=id;
        this.clientId= clientId;
        this.vendorId= vendorId;
        this.dateCreated= dateCreated;
        this.status= status;
        this.totalPrice= totalPrice;
        this.currency= currency;
        this.orderItems= new ArrayList<OrderItem>();
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
    }

    @Override
    public int hashCode() {
        return (int)(this.id % Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Order){
            Order order= (Order) object;
            return this.id==order.id;
        }
        return false;
    }
}
