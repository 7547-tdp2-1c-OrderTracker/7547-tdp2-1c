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

/*
    public static Promotion fromJson(JSONObject json) {
        Promotion promotion = null;
        try {
            String dateCreatedStr = json.getString("date_created");
            Date dateCreated = null;
            if (FieldValidator.isValid(dateCreatedStr)) dateCreated = DateUtils.parseDate(dateCreatedStr);

            promotion = new Promotion(json.getLong("id"), dateCreated);

            String dateVisitStr = json.getString("date");
            Date dateVisit = null;
            if (FieldValidator.isValid(dateVisitStr)) dateVisit = DateUtils.parseDate(dateVisitStr);
            visit.setDateVisit(dateVisit);

            String lastModifiedStr = json.getString("last_modified");
            Date lastModified = null;
            if (FieldValidator.isValid(lastModifiedStr)) lastModified = DateUtils.parseDate(lastModifiedStr);
            visit.setLastModified(lastModified);

        } catch(JSONException e) {
            throw new BusinessException("Error parsing Visit.",e);
        }
        return visit;
    }
    */
}
