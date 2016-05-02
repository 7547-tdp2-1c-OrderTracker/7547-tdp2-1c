package ar.fi.uba.trackerman.tasks.schedule;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ar.fi.uba.trackerman.adapters.SchedulesListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.ScheduleDay;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by smpiano on 5/1/16.
 */
public class GetScheduleDayListTask extends AbstractTask<String,Void,ScheduleDay,SchedulesListAdapter> {

    private List<Client> clients;

    public GetScheduleDayListTask(SchedulesListAdapter adapter) {
        super(adapter);
    }

    @Override
    protected ScheduleDay doInBackground(String... params) {
        String urlString = "/v1/schedule/day";
        if (params.length == 2) {
            String dateConsult = params[0];
            String seller = params[1];
            urlString += "?date=" + dateConsult + "&seller_id=" + seller;
        } else if (params.length == 4) {
            String dateConsult = params[0];
            String seller = params[1];
            String lat = params[2];
            String lon = params[3];
            urlString += "?date=" + dateConsult + "&seller_id=" + seller +"&lat="+ lat +"&lon="+ lon;
        } else {
            ShowMessage.toastMessage(weakReference.get().getContext(), "Parmetros incorrectos");
        }

        ScheduleDay day = null;
        try{
            day = (ScheduleDay) restClient.get(urlString);
        } catch (BusinessException e) {
            ShowMessage.toastMessage(weakReference.get().getContext(), e.getMessage());
        }
        return day;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        return ScheduleDay.fromJson(new JSONObject(json));
    }

    @Override
    protected void onPostExecute(ScheduleDay day) {
        weakReference.get().setScheduleDay(day);
    }

}
