package ar.fi.uba.trackerman.tasks;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.DateUtils;

public class PostOrdersTask extends AbstractTask<String,Void,Order,ClientActivity>{

    public PostOrdersTask(ClientActivity activity) {
        super(activity);
    }

    @Override
    protected Order doInBackground(String... params) {
        String vendorId = params[0];
        String clientId = params[1];
        String body = "{\"client_id\": "+clientId+",\"vendor_id\":"+vendorId+"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        return (Order) restClient.post("/v1/orders", body, headers);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject orderJson = new JSONObject(json);
        return Order.fromJson(orderJson);
    }

    @Override
    protected void onPostExecute(Order order) {
        super.onPostExecute(order);
        if (order == null){
            weakReference.get().showSnackbarSimpleMessage("Cáspitas! Tenemos un problema!");
        } else {
            weakReference.get().afterCreatingOrder(order);
        }
    }
}
