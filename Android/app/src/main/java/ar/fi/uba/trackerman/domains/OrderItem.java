package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.exceptions.OrderTrackerException;

public class OrderItem {

    private long id;
    private long productId;
    private String name;
    private long orderId;
    private int quantity;
    private double unitPrice;
    private String currency;
    private String brandName;
    private String thumbnail;


    public OrderItem(long id, long productId, String name, int quantity, double unitPrice, String currency, String brandName, String thumbnail){
        this.id = id;
        this.productId= productId;
        this.name= name;
        this.unitPrice= unitPrice;
        this.currency= currency;
        this.quantity= quantity;
        this.thumbnail= thumbnail;
        this.brandName=brandName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Double getTotalPrice() {
        return this.unitPrice * this.quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (id != orderItem.id) return false;
        if (productId != orderItem.productId) return false;
        if (orderId != orderItem.orderId) return false;
        if (quantity != orderItem.quantity) return false;
        if (Double.compare(orderItem.unitPrice, unitPrice) != 0) return false;
        if (name != null ? !name.equals(orderItem.name) : orderItem.name != null) return false;
        if (currency != null ? !currency.equals(orderItem.currency) : orderItem.currency != null)
            return false;
        if (brandName != null ? !brandName.equals(orderItem.brandName) : orderItem.brandName != null)
            return false;
        return !(thumbnail != null ? !thumbnail.equals(orderItem.thumbnail) : orderItem.thumbnail != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + quantity;
        temp = Double.doubleToLongBits(unitPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (brandName != null ? brandName.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    public static OrderItem fromJson(JSONObject json) {
        OrderItem orderItem = null;
        try {
            long orderItemId = json.getLong("id");
            long product_id = json.getLong("product_id");
            String name = json.getString("name");
            int quantity = json.getInt("quantity");
            double price = json.getDouble("unit_price");
            String currencyItem = json.getString("currency");
            String brand = json.getString("brand_name");
            String picture = json.getString("thumbnail");
            orderItem = new OrderItem(orderItemId,product_id,name,quantity,price,currencyItem,brand,picture);
            orderItem.setOrderId(json.getLong("order_id"));
        } catch (JSONException e) {
            throw new OrderTrackerException("Error parsing OrderItem", e);
        }
        return orderItem;
    }
}
