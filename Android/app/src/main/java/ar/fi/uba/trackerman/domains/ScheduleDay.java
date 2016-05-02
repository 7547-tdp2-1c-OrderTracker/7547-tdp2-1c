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
public class ScheduleDay {
    private long sellerId;
    private Date date;
    private List<Client> clients;

    public ScheduleDay(long sellerId, Date date) {
        this.sellerId = sellerId;
        this.date = date;
        this.clients = new ArrayList<Client>();
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

    public List<Client> getClients() {
        return clients;
    }

    public static ScheduleDay fromJson(JSONObject json) {
        ScheduleDay scheduleDay = null;
        try {
            String dateStr = json.getString("date");
            Date date = null;
            if (FieldValidator.isValid(dateStr)) date = DateUtils.parseShortDate(dateStr);

            scheduleDay = new ScheduleDay(json.getLong("seller_id"),date);
            JSONArray resultJSON = (JSONArray) json.get("clients");
            for (int i = 0; i < resultJSON.length(); i++) {
                scheduleDay.getClients().add(Client.fromJson(resultJSON.getJSONObject(i)));
            }

        } catch(JSONException e) {
            throw new BusinessException("Error parsing Product.",e);
        }
        return scheduleDay;
    }
}
