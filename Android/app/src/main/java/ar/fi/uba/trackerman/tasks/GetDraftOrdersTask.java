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
import java.util.Date;
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
        return (List<Order>) restClient.get("/v1/orders?status=draft&vendor_id="+vendorId);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject ordersList = new JSONObject(json);
        JSONArray resultJSON = (JSONArray) ordersList.get("results");
        List<Order> orders= new ArrayList<Order>();
        Order order;
        for (int i = 0; i < resultJSON.length(); i++) {
            JSONObject orderJson = resultJSON.getJSONObject(i);
            long id=orderJson.getLong("id");
            long vendorId= orderJson.getLong("vendor_id");
            long clientId= orderJson.getLong("client_id");
            String dateCreatedStr = orderJson.getString("date_created");
            Date dateCreated = null;
            if (dateCreatedStr != null && !"null".equalsIgnoreCase(dateCreatedStr)) dateCreated = DateUtils.parseDate(dateCreatedStr);
            double total_price= orderJson.getDouble("total_price");
            // TODO: DESCOMENTAR ESTO!!!
            String currency= "ARS";//orderJson.getString("currency");
            String status= orderJson.getString("status");
            order= new Order(id,clientId,vendorId,dateCreated,status,total_price,currency);
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