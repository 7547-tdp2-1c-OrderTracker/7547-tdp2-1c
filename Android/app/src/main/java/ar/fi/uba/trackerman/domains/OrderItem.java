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

    @Override
    public String toString() {
        return name;
    }

}
