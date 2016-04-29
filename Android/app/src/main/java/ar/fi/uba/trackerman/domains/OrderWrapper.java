package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.exceptions.BusinessException;

/**
 * Created by smpiano on 4/29/16.
 */
public class OrderWrapper {
    private Order order;
    private Client client;

    public OrderWrapper(Order order, Client client) {
        this.order = order;
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public Order getOrder() {
        return order;
    }

    public static OrderWrapper fromJson(JSONObject json) {
        Order order;
        Client client;
        try {
            order = Order.fromJson(json);
            client = Client.fromJson(json.getJSONObject("client"));
        } catch (JSONException e) {
            throw new BusinessException("Error parsing OrderWrapper.",e);
        }
        return new OrderWrapper(order, client);
    }
}
