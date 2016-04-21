package ar.fi.uba.trackerman.tasks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.utils.DateUtils;

/**
 * Created by plucadei on 31/3/16.
 */
public class ConfirmOrderTask extends AbstractTask<String,Void,Order,OrderActivity> {

    public ConfirmOrderTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected Order doInBackground(String... params) {
        String orderId= params[0];
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        return (Order) restClient.put("/v1/orders/"+orderId,"{\"status\": \"confirm\"}",headers);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        Order order;
        JSONObject orderJson = new JSONObject(json);
        long id=orderJson.getLong("id");
        long vendorId= orderJson.getLong("vendor_id");
        long clientId= orderJson.getLong("client_id");

        String dateCreatedStr = orderJson.getString("date_created");
        Date dateCreated = null;
        if (dateCreatedStr != null && !"null".equalsIgnoreCase(dateCreatedStr)) dateCreated = DateUtils.parseDate(dateCreatedStr);

        double total_price= orderJson.getDouble("total_price");
        String currency= orderJson.getString("currency");
        String status= orderJson.getString("status");
        order= new Order(id,clientId,vendorId,dateCreated,status,total_price,currency);
        JSONArray itemsJson= orderJson.getJSONArray("order_items");
        for (int i = 0; i < itemsJson.length(); i++) {
            JSONObject row = itemsJson.getJSONObject(i);
            long orderItemId = row.getLong("id");
            long product_id = row.getLong("product_id");
            String name= row.getString("name");
            int quantity= row.getInt("quantity");
            double price= row.getDouble("unit_price");
            String currencyItem= row.getString("currency");
            String brand= row.getString("brand_name");
            String picture= row.getString("thumbnail");
            OrderItem item= new OrderItem(orderItemId,product_id,name,quantity,price,currencyItem,brand,picture);
            order.addOrderItem(item);
        }
        return order;
    }

    @Override
    protected void onPostExecute(Order order) {
        OrderConfirmer reciver= weakReference.get();
        if(reciver!=null){
            reciver.afterOrderConfirmed(order);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }

    public interface OrderConfirmer {
        public void afterOrderConfirmed(Order order);
    }
}
