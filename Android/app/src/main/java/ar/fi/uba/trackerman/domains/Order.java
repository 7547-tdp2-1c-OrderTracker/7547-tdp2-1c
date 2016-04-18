package ar.fi.uba.trackerman.domains;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Order {

    private long id;
    private long visitId;
    private double totalPrice;
    private Date deliveryDate;
    private String status;
    private long clientId;
    private List<OrderEntry> orderEntries;

    public Order(long id, long clientId) {
        this.id= id;
        this.clientId = clientId;
        this.orderEntries = Collections.EMPTY_LIST;
    }

    public long getId() {
        return id;
    }

    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public List getOrderEntries() {
        return orderEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (visitId != order.visitId) return false;
        if (Double.compare(order.totalPrice, totalPrice) != 0) return false;
        if (clientId != order.clientId) return false;
        if (deliveryDate != null ? !deliveryDate.equals(order.deliveryDate) : order.deliveryDate != null)
            return false;
        return status.equals(order.status);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (visitId ^ (visitId >>> 32));
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (deliveryDate != null ? deliveryDate.hashCode() : 0);
        result = 31 * result + status.hashCode();
        result = 31 * result + (int) (clientId ^ (clientId >>> 32));
        return result;
    }
}
