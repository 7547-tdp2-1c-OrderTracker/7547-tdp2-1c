package ar.fi.uba.trackerman.tasks.schedule;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.activities.MyWeekAgendaActivity;
import ar.fi.uba.trackerman.domains.ScheduleWeekView;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by smpiano on 5/1/16.
 */
public class GetScheduleWeekTask  extends AbstractTask<String,Void,ScheduleWeekView,MyWeekAgendaActivity> {

    public GetScheduleWeekTask(MyWeekAgendaActivity activity) {
        super(activity);
    }

    @Override
    protected ScheduleWeekView doInBackground(String... params) {
        Context ctx = weakReference.get().getApplicationContext();
        String urlString = "/v1/schedule/week";
        if (params.length == 2) {
            String dateConsult = params[0];
            String seller = params[1];
            urlString += "?date=" + dateConsult + "&seller_id=" + seller;
        } else if (params.length == 4) {
            String dateConsult = params[0];
            String seller = params[1];
            String lat = params[2];
            String lon = params[3];
            urlString += "?date=" + dateConsult + "&seller_id=" + seller + "&lat="+lat+"&lon="+lon;
        } else {
            ShowMessage.toastMessage(ctx, "Parametros incorrectos");
        }

        ScheduleWeekView week = null;
        try{
            week = (ScheduleWeekView) restClient.get(urlString, withAuth(ctx));
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
        }
        return week;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        return ScheduleWeekView.fromJson(new JSONObject(json));
    }

    @Override
    protected void onPostExecute(ScheduleWeekView week) {
        weakReference.get().fillWeek(week);
    }

}
