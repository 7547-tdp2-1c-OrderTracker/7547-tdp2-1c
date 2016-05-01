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
public class ScheduleDay {
    private Client client;
    private Date visited;

    public ScheduleDay(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getVisited() {
        return visited;
    }

    public void setVisited(Date visited) {
        this.visited = visited;
    }

    public static ScheduleDay fromJson(JSONObject json) {
        ScheduleDay scheduleDay = null;
        try {
            Client client = Client.fromJson(json);

            scheduleDay = new ScheduleDay(client);

            String visitedStr = json.getString("visited");
            Date visited = null;
            if (FieldValidator.isValid(visitedStr)) visited = DateUtils.parseDate(visitedStr);
            scheduleDay.setVisited(visited);

        } catch(JSONException e) {
            throw new BusinessException("Error parsing Product.",e);
        }
        return scheduleDay;
    }
}
