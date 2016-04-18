package ar.fi.uba.trackerman.domains;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Order {

    private long id;
    private long vendorId;
    private double totalPrice;
    private Date deliveryDate;
    private Date dateCreated;
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

    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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
        if (vendorId != order.vendorId) return false;
        if (Double.compare(order.totalPrice, totalPrice) != 0) return false;
        if (clientId != order.clientId) return false;
        if (deliveryDate != null ? !deliveryDate.equals(order.deliveryDate) : order.deliveryDate != null)
            return false;
        if (dateCreated != null ? !dateCreated.equals(order.dateCreated) : order.dateCreated != null)
            return false;
        return status.equals(order.status);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (vendorId ^ (vendorId >>> 32));
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (deliveryDate != null ? deliveryDate.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + status.hashCode();
        result = 31 * result + (int) (clientId ^ (clientId >>> 32));
        return result;
    }
}
