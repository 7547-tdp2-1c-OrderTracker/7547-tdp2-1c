package ar.fi.uba.trackerman.tasks.visit;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.domains.Visit;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.NoStockException;
import ar.fi.uba.trackerman.fragments.DailyRouteFragment;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.ShowMessage;

public class PostVisitTask extends AbstractTask<String,Void,Visit,DailyRouteFragment> {

    public PostVisitTask(DailyRouteFragment fragment) {
        super(fragment);
    }

    public Visit createVisit(String clientId, String dayOfWeek, String date, String comment) {
        String body = "{\"client_id\": "+ clientId + ",\"seller_id\":"+ AppSettings.getSellerId() + ",\"date\":\""+date+"\",\"comment\":\""+comment+"\"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String url = "/v1/visits";
        return (Visit) restClient.post(url, body, headers);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject orderItemJson = new JSONObject(json);
        return Visit.fromJson(orderItemJson);
    }

    @Override
    protected Visit doInBackground(String... params) {
        try {
            return this.createVisit(params[0], params[1], params[2], params[3]);
        } catch (BusinessException e) {
            ShowMessage.showSnackbarSimpleMessage(weakReference.get().getView(),e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Visit visit) {
        super.onPostExecute(visit);
        if (visit != null) {
            weakReference.get().afterCreatingVisit(visit);
        }
    }

    public interface VisitCreator {
        public void afterCreatingVisit(Visit visit);
    }
}
