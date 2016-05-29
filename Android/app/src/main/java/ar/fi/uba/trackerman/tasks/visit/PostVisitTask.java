package ar.fi.uba.trackerman.tasks.visit;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.domains.Visit;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.fragments.DailyRouteFragment;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.MyPreferenceHelper;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;

public class PostVisitTask extends AbstractTask<String,Void,Visit,DailyRouteFragment> {

    private MyPreferenceHelper helper;
    private String key;

    public PostVisitTask(DailyRouteFragment fragment) {
        super(fragment);
        this.key = fragment.getContext().getString(R.string.shared_pref_current_seller);
        helper = new MyPreferenceHelper(fragment.getContext());
    }

    public Visit createVisit(String clientId, String dayOfWeek, String date, String comment) {
        String body = "{\"client_id\": "+ clientId + ",\"seller_id\":"+ helper.getSeller().getId() + ",\"date\":\""+date+"\",\"comment\":\""+comment+"\"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String url = "/v1/visits";
        Visit visit = null;
        Context ctx = weakReference.get().getContext();
        try {
            visit = (Visit) restClient.post(url, body, withAuth(ctx,headers));
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
        }
        return visit;
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
