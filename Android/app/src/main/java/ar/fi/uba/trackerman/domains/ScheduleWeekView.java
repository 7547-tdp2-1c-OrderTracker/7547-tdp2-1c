package ar.fi.uba.trackerman.domains;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.FieldValidator;

/**
 * Created by smpiano on 5/1/16.
 */
public class ScheduleWeekView {
    private long sellerId;
    private Date date;
    private List<Semaphore> semaphores;

    public ScheduleWeekView(Long sellerId) {
        this.sellerId = sellerId;
        this.semaphores = new ArrayList<Semaphore>();
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Semaphore> getSemaphores() {
        return semaphores;
    }

    public static ScheduleWeekView fromJson(JSONObject json) {
        ScheduleWeekView week = null;
        try {
            week = new ScheduleWeekView(json.getLong("seller_id"));

            String dateStr = json.getString("date");
            Date date = null;
            if (FieldValidator.isValid(dateStr)) date = DateUtils.parseShortDate(dateStr);
            week.setDate(date);

            JSONArray semaphores = (JSONArray) json.get("semaphore");
            for (int i = 0; i < semaphores.length(); i++) {
                week.getSemaphores().add(Semaphore.fromJson(semaphores.getJSONObject(i)));
            }

        } catch (JSONException e) {
            throw new BusinessException("Error parsing ScheduleWeekView.",e);
        }
        return week;
    }
}
