package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.FieldValidator;

/**
 * Created by guido on 09/05/16.
 */
public class Promotion {

    private long id;
    private String name;
    private Date beginDate;
    private Date endDate;
    private int percent;
    private int brandId;
    private int productId;
    private int minQuantity;
    private Date createdDate;
    private Date lastModified;

    public Promotion(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public static Promotion fromJson(JSONObject json) {
        Promotion promotion = null;
        try {
            promotion = new Promotion(json.getLong("id"));
            promotion.setName(json.getString("name"));
            promotion.setPercent(json.getInt("percent"));
            promotion.setMinQuantity(json.getInt("min_quantity"));

            String promotionBeginDateStr = json.getString("begin_date");
            Date beginDate = null;
            if (FieldValidator.isValid(promotionBeginDateStr)) beginDate = DateUtils.parseDate(promotionBeginDateStr);
            promotion.setBeginDate(beginDate);

            String promotionEndDateStr = json.getString("end_date");
            Date endDate = null;
            if (FieldValidator.isValid(promotionEndDateStr)) endDate = DateUtils.parseDate(promotionEndDateStr);
            promotion.setEndDate(endDate);

        } catch(JSONException e) {
            throw new BusinessException("Error parsing Promotion.",e);
        }
        return promotion;
    }

}
