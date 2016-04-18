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

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderItem;

/**
 * Created by plucadei on 31/3/16.
 */
public class EmptyOrderTask extends AbstractTask<String,Void,Order> {
    private WeakReference<OrderCleaner> weekCleanerReference;

    public EmptyOrderTask(OrderCleaner reciver) {
        weekCleanerReference = new WeakReference<OrderCleaner>(reciver);
    }

    @Override
    protected Order doInBackground(String... params) {

        String orderId= params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String orderJsonStr;
        Order order=null;
        try {
            URL url = new URL(SERVER_HOST+"/v1/orders/"+orderId);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            String str =  "{\"status\": \"cancelled\"}";
            byte[] outputInBytes = str.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return order;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return order;
            }
            orderJsonStr = buffer.toString();
            try {
                return parseOrderJson(orderJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(EmptyOrderTask.class.getCanonicalName(), "Error ", e);
            return order;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(this.getClass().getCanonicalName(), "Error closing stream", e);
                }
            }
        }
        return order;
    }

    private Order parseOrderJson(String orderJsonStr) throws JSONException{
        Order order;
        JSONObject orderJson = new JSONObject(orderJsonStr);
        long id=orderJson.getLong("id");
        long vendorId= orderJson.getLong("vendor_id");
        long clientId= orderJson.getLong("client_id");
        String dateCreated= "HOY";orderJson.getString("delivery_date");
        double total_price= orderJson.getDouble("total_price");
        // TODO: DESCOMENTAR ESTO!!!
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
            String brand= row.getString("brand");
            String picture= row.getString("picture");
            OrderItem item= new OrderItem(orderItemId,product_id,name,quantity,price,currencyItem,brand,picture);
            order.addOrderItem(item);
        }
        return order;
    }

    @Override
    protected void onPostExecute(Order order) {
        OrderCleaner reciver= weekCleanerReference.get();
        if(reciver!=null){
            reciver.updateOrderInformation(order);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }

    public interface OrderCleaner {
        public void updateOrderInformation(Order order);
    }
}
