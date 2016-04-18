package ar.fi.uba.trackerman.domains;

public class OrderItem {
    private long id;
    private String thumbnail;
    private String brand;
    private long productId;
    private String name;
    private double unitPrice;
    private String currency;
    private int quantity;

    public OrderItem(long id, long productId, String name, int quantity, double unitPrice, String currency, String brand, String thumbnail){
        this.id= id;
        this.productId= productId;
        this.name= name;
        this.unitPrice= unitPrice;
        this.currency= currency;
        this.quantity= quantity;
        this.thumbnail= thumbnail;
        this.brand=brand;
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBrand() {
        return brand;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public double getTotalPrice() {
        return quantity*unitPrice;
    }

    @Override
    public String toString() {
        return name;
    }
}