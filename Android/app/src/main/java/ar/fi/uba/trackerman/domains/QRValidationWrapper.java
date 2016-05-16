package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.exceptions.BusinessException;

/**
 * Created by guido on 15/05/16.
 */
public class QRValidationWrapper {

    private Order order;
    private Client client;

    public QRValidationWrapper(Order order, Client client) {
        this.order = order;
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public Order getOrder() {
        return order;
    }

    public static QRValidationWrapper fromJson(JSONObject json) {
        Order order;
        Client client;
        try {
            order = Order.fromJson(json.getJSONObject("order"));
            client = Client.fromJson(json.getJSONObject("client"));
        } catch (JSONException e) {
            throw new BusinessException("Error parsing OrderWrapper.",e);
        }
        return new QRValidationWrapper(order, client);
    }
}
