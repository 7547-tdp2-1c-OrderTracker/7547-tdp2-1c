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
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String json=null;
        OrderItem orderItem = null;

        try {
            //---------------------------------------------------------
            URL url = new URL(AppSettings.getServerHost()+"/v1/orders/"+orderId+"/order_items");
            //---------------------------------------------------------;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            String str =  "{\"product_id\": "+productId+",\"quantity\":"+quantity+"}";
            byte[] outputInBytes = str.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();

            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }

            json = buffer.toString();
            try {
                orderItem = validateResponse(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(PostOrderItemsTask.class.getCanonicalName(), "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(GetClientListTask.class.getCanonicalName(), "Error closing stream", e);
                }
            }
        }
        return orderItem;
    }

    private OrderItem validateResponse(String jsonString) throws JSONException, ParseException {
        JSONObject json = new JSONObject(jsonString);
        OrderItem orderItem = null;

        try {
            long id = json.getLong("id");
            long productId = json.getLong("product_id");
            String name = json.getString("name");
            int quantity = json.getInt("quantity");
            double unitPrice = json.getDouble("unit_price");
            String currency = json.getString("currency");
            String brandName = json.getString("brand_name");
            String thumbnail = json.getString("thumbnail");

            orderItem = new OrderItem(id,productId,name,quantity,unitPrice,currency,brandName,thumbnail);
        } catch (Exception e) {
            Log.e("create_order_item_json", "Error parseando la creacion de orden item", e);
        }

        return orderItem;
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
