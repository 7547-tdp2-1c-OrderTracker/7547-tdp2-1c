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
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.DateUtils;

public class PostOrdersTask extends AbstractTask<String,Void,Boolean,ClientActivity>{

    public PostOrdersTask(ClientActivity activity) {
        super(activity);
    }

    public Boolean createOrder(String vendorId, String clientId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String json=null;
        Boolean isOrderCreated = false;

        try {
            //---------------------------------------------------------
            URL url = new URL(AppSettings.getServerHost()+"/v1/orders");
            //---------------------------------------------------------;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            String str =  "{\"client_id\": "+clientId+",\"vendor_id\":"+vendorId+"}";
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
                isOrderCreated = isJsonCorrect(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(PostOrdersTask.class.getCanonicalName(), "Error ", e);
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
        return isOrderCreated;
    }

    private Boolean isJsonCorrect(String jsonString) throws JSONException, ParseException {
        JSONObject json = new JSONObject(jsonString);
        Boolean isCorrect = false;
        Order order = null;

        try {
            order= new Order(json.getLong("id"),json.getLong("client_id"));

            String deliveryDate = json.getString("delivery_date");
            if (deliveryDate != null && !"null".equalsIgnoreCase(deliveryDate)) order.setDeliveryDate(DateUtils.parseDate(deliveryDate));

            order.setStatus(json.getString("status"));
            try{
                order.setTotalPrice(json.getDouble("total_price"));
            } catch (JSONException e) {
                //do nothing. just because it's atomic double
            }
            order.setVendorId(json.getLong("vendor_id"));

            isCorrect = true;
        } catch (Exception e) {
            Log.e("parse_create_order_json","Error parseando la creacion de la orden",e);
        }

        return isCorrect;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return this.createOrder(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(Boolean isOrderCorrect) {
        super.onPostExecute(isOrderCorrect);
        weakReference.get().afterCreatingOrder(isOrderCorrect);
    }
}
