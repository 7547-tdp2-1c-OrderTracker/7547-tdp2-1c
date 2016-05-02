package ar.fi.uba.trackerman.domains;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.exceptions.BusinessException;

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

    public static ProductsSearchResult fromJson(JSONObject json) {
        ProductsSearchResult productsSearchResult = null;
        try {
            productsSearchResult = new ProductsSearchResult();
            JSONObject pagingJSON = json.getJSONObject("paging");
            productsSearchResult.setTotal(pagingJSON.getLong("total"));
            productsSearchResult.setOffset(pagingJSON.getLong("offset"));
            JSONArray resultJSON = (JSONArray) json.get("results");
            for (int i = 0; i < resultJSON.length(); i++) {
                productsSearchResult.addProduct(Product.fromJson(resultJSON.getJSONObject(i)));
            }
        } catch(JSONException e) {
            throw new BusinessException("Error parsing ProductSearchResult.",e);
        }
        return productsSearchResult;
    }
}
