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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return name;
    }

    public double getTotalPrice() {
        return quantity*unitPrice;
    }
}
