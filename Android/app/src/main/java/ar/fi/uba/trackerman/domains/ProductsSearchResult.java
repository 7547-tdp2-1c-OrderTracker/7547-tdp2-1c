package ar.fi.uba.trackerman.domains;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plucadei on 3/4/16.
 */
public class ProductsSearchResult {
    long offset;
    long total;
    List<Product> products;

    public ProductsSearchResult(){
        super();
        offset=0;
        total=1;
        products= new ArrayList<>();
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getOffset() {
        return offset;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product){
        this.products.add(product);
    }
}
