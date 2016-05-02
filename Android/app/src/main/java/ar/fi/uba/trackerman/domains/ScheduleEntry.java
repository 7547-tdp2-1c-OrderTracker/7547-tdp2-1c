package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.FieldValidator;

/**
 * Created by smpiano on 5/1/16.
 */
public class ScheduleEntry {

    private long id;
    private int dayOfWeek;
    private Date dateCreated;
    private Date lastModified;
    private long clientId;
    private long sellerId;

    public ScheduleEntry(long id, long clientId, long sellerId, Date created){
        this.id = id;
        this.clientId = clientId;
        this.sellerId = clientId;
        this.dateCreated = created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public static ScheduleEntry fromJson(JSONObject json) {
        ScheduleEntry entry = null;
        try {
            String dateCreatedStr = json.getString("date_created");
            Date dateCreated = null;
            if (FieldValidator.isValid(dateCreatedStr)) dateCreated = DateUtils.parseDate(dateCreatedStr);

            entry = new ScheduleEntry(json.getLong("id"),json.getLong("client_id"),json.getLong("seller_id"),dateCreated);
            entry.setDayOfWeek(json.getInt("day_of_week"));

            String lastModifiedStr = json.getString("last_modified");
            Date lastModified = null;
            if (FieldValidator.isValid(lastModifiedStr)) lastModified = DateUtils.parseDate(lastModifiedStr);
            entry.setLastModified(lastModified);

        } catch(JSONException e) {
            throw new BusinessException("Error parsing Product.",e);
        }
        return entry;
    }
}
