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
import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.activities.ProductActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.DateUtils;

public class PostOrderItemsTask extends AbstractTask<String,Void,OrderItem,ProductActivity>{

    public PostOrderItemsTask(ProductActivity activity) {
        super(activity);
    }

    public OrderItem createOrderItem(String orderId, String productId, String quantity) {
        String body = "{\"product_id\": "+productId+",\"quantity\":"+quantity+"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String url = "/v1/orders/"+orderId+"/order_items";
        return (OrderItem) restClient.post(url, body, headers);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject orderItemJson = new JSONObject(json);
        return OrderItem.fromJson(orderItemJson);
    }

    @Override
    protected OrderItem doInBackground(String... params) {
        return this.createOrderItem(params[0], params[1], params[2]);
    }

    @Override
    protected void onPostExecute(OrderItem orderItem) {
        super.onPostExecute(orderItem);
        if (orderItem==null) {
            weakReference.get().showSnackbarSimpleMessage("Cáspitas! Tenemos un problema!");
        } else {
            weakReference.get().afterCreatingOrderItem(orderItem);
        }
    }
}
