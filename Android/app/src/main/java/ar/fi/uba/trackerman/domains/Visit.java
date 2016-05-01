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
public class Visit {

    private long id;
    private Date createdDate;
    private Date lastModified;
    private String comment;
    private long scheduleEntryId;
    private Date dateVisit;

    public Visit(long id, long scheduleEntryId, Date created) {
        this.id = id;
        this.scheduleEntryId = scheduleEntryId;
        this.createdDate = created;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getScheduleEntryId() {
        return scheduleEntryId;
    }

    public void setScheduleEntryId(long scheduleEntryId) {
        this.scheduleEntryId = scheduleEntryId;
    }

    public Date getDateVisit() {
        return dateVisit;
    }

    public void setDateVisit(Date dateVisit) {
        this.dateVisit = dateVisit;
    }

    public static Visit fromJson(JSONObject json) {
        Visit visit = null;
        try {
            String dateCreatedStr = json.getString("date_created");
            Date dateCreated = null;
            if (FieldValidator.isValid(dateCreatedStr)) dateCreated = DateUtils.parseDate(dateCreatedStr);

            visit = new Visit(json.getLong("id"),json.getLong("schedule_entry_id"),dateCreated);
            visit.setComment(json.getString("comment"));

            String dateVisitStr = json.getString("date");
            Date dateVisit = null;
            if (FieldValidator.isValid(dateVisitStr)) dateVisit = DateUtils.parseDate(dateVisitStr);
            visit.setDateVisit(dateVisit);

            String lastModifiedStr = json.getString("last_modified");
            Date lastModified = null;
            if (FieldValidator.isValid(lastModifiedStr)) lastModified = DateUtils.parseDate(lastModifiedStr);
            visit.setLastModified(lastModified);

        } catch(JSONException e) {
            throw new BusinessException("Error parsing Product.",e);
        }
        return visit;
    }
}
