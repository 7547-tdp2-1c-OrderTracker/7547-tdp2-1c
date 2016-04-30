package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.exceptions.BusinessException;

/**
 * Created by plucadei on 3/4/16.
 */
public class Product {
    private long id;
    private String name;
    private long brandId;
    private String brandName;
    private int stock;
    private String code;
    private String status;
    private String description;

    private String currency;
    private double price;
    private double retailPrice; // minorista
    private double wholesalePrice; // mayorista

    private String picture;
    private String thumbnail;


    public Product(long id){
        this.id=id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public long getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPriceWithCurrency() {
        return this.getCurrency() +" "+ Double.toString(this.getPrice());
    }

    public String getRetailPriceWithCurrency() {
        return this.getCurrency() +" "+ Double.toString(this.getRetailPrice());
    }

    public String getWholeSalePriceWithCurrency() {
        return this.getCurrency() +" "+ Double.toString(this.getWholesalePrice());
    }

    public static Product fromJson(JSONObject json) {
        Product product = null;
        try {
            product = new Product(json.getLong("id"));
            product.setName(json.getString("name"));
            product.setBrandId(json.getLong("brand_id"));
            product.setBrandName(json.getString("brand_name"));
            product.setStock(json.getInt("stock"));

            product.setCode(json.getString("code"));
            product.setStatus(json.getString("status"));
            product.setDescription(json.getString("description"));
            product.setCurrency(json.getString("currency"));
            product.setPrice(json.getDouble("retailPrice"));
            product.setRetailPrice(json.getDouble("retailPrice"));
            product.setWholesalePrice(json.getDouble("wholesalePrice"));

            product.setPicture(json.getString("picture"));
            product.setThumbnail(json.getString("thumbnail"));
        } catch(JSONException e) {
            throw new BusinessException("Error parsing Product.",e);
        }
        return product;
    }
}
