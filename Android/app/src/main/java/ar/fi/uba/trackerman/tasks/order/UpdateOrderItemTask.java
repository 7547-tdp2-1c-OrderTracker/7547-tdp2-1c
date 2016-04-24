package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.Order;

import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.AppSettings;

public class UpdateOrderItemTask extends AbstractTask<String,Void,String,OrderActivity> {

    public UpdateOrderItemTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        String orderId = params[0];
        String itemId = params[1];
        String quantity = params[2];

        String url = "/v1/orders/" + orderId + "/order_items/" + itemId;
        String body = "{\"quantity\": " + quantity + "}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String resp = (String) restClient.put(url,body,headers);
        if (resp == null) resp = "FAIL";
        return resp;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        return "OK";
    }

    @Override
    protected void onPostExecute(String result) {
        OrderItemModifier modifier= weakReference.get();
        if(modifier!=null){
            modifier.afterUpdateOrderItem(result);
        }else{
            Log.e(this.getClass().getCanonicalName(), "Adapter no longer available!");
        }
    }

    public interface OrderItemModifier {
        public void afterUpdateOrderItem(String Item);
    }

}
