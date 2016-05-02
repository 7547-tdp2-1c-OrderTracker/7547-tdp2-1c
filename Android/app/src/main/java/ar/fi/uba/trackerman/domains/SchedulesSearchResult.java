package ar.fi.uba.trackerman.domains;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plucadei on 3/4/16.
 */
public class SchedulesSearchResult {
    long offset;
    long total;
    List<ScheduleEntry> schedules;

    public SchedulesSearchResult(){
        super();
        offset=0;
        total=1;
        schedules = new ArrayList<>();
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

    public List<ScheduleEntry> getSchedules() {
        return schedules;
    }

    public void addScheduleEntry(ScheduleEntry schedule){
        this.schedules.add(schedule);
    }

    public static SchedulesSearchResult fromJson(JSONObject json) {
        SchedulesSearchResult schedulesSearchResult = null;
        try {
            schedulesSearchResult = new SchedulesSearchResult();
            JSONObject pagingJSON = json.getJSONObject("paging");
            schedulesSearchResult.setTotal(pagingJSON.getLong("total"));
            schedulesSearchResult.setOffset(pagingJSON.getLong("offset"));
            JSONArray resultJSON = (JSONArray) json.get("results");
            for (int i = 0; i < resultJSON.length(); i++) {
                schedulesSearchResult.addScheduleEntry(ScheduleEntry.fromJson(resultJSON.getJSONObject(i)));
            }
        } catch(JSONException e) {

        }
        return schedulesSearchResult;
    }
}
