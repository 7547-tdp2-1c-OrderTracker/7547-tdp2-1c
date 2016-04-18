package ar.fi.uba.trackerman.tasks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.DateUtils;


public class GetDraftOrdersTask extends AbstractTask<String,Void,List<Order>,GetDraftOrdersTask.DraftOrdersValidation>{

    public GetDraftOrdersTask(DraftOrdersValidation validation) {
        super(validation);
    }

    public List<Order> getDraftOrders(String vendorId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String json=null;
        List<Order> orders=null;

        try {
            //---------------------------------------------------------
            URL url = new URL(AppSettings.getServerHost()+"/v1/orders?status=draft&vendor_id="+vendorId);
            //---------------------------------------------------------;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
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
                orders = parseJson(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(GetDraftOrdersTask.class.getCanonicalName(), "Error ", e);
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
        return orders;
    }

    private List<Order> parseJson(String jsonString) throws JSONException, ParseException {
        JSONObject json = new JSONObject(jsonString);
        JSONArray resultJSON = (JSONArray) json.get("results");
        List<Order> orders= new ArrayList<Order>();
        Order order;
        for (int i = 0; i < resultJSON.length(); i++) {
            JSONObject row = resultJSON.getJSONObject(i);
            order= new Order(row.getLong("id"),row.getLong("client_id"));

            String deliveryDate = row.getString("delivery_date");
            if (deliveryDate != null && !"null".equalsIgnoreCase(deliveryDate)) order.setDeliveryDate(DateUtils.parseDate(deliveryDate));

            String dateCreated = row.getString("date_created");
            if (dateCreated != null && !"null".equalsIgnoreCase(dateCreated)) order.setDateCreated(DateUtils.parseDate(dateCreated));

            order.setStatus(row.getString("status"));
            try{
                order.setTotalPrice(row.getDouble("total_price"));
            } catch (JSONException e) {
                //do nothing. just because it's atomic double
            }
            order.setVendorId(row.getLong("vendor_id"));
            orders.add(order);
        }
        return orders;
    }

    @Override
    protected List<Order> doInBackground(String... strings) {
        return this.getDraftOrders(strings[0]);
    }

    @Override
    protected void onPostExecute(List<Order> orders) {
        super.onPostExecute(orders);
        weakReference.get().setDraftOrders(orders);
    }

    public interface DraftOrdersValidation {
        public void setDraftOrders(List<Order> orders);
    }
}
