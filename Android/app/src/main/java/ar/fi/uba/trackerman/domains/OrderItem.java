package ar.fi.uba.trackerman.domains;

public class OrderItem {

    private long id;
    private long productId;
    private String name;
    private long orderId;
    private int quantity;
    private double unitPrice;
    private String currency;
    private String brandName;

    public OrderItem(long id, long orderId, long productId, int quantity){
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
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
        if (brandName != null ? !brandName.equals(orderItem.brandName) : orderItem.brandName != null) return false;
        return !(currency != null ? !currency.equals(orderItem.currency) : orderItem.currency != null);

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
        return result;
    }
}
