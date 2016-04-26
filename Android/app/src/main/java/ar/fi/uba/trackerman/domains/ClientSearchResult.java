package ar.fi.uba.trackerman.domains;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.exceptions.BusinessException;

/**
 * Created by plucadei on 2/4/16.
 */
public class ClientSearchResult {
    long offset;
    long total;
    List<Client> clients;

    public ClientSearchResult(){
        offset=0;
        total=0;
        clients= new ArrayList<>();
    }

    public void addClient(Client client){
        clients.add(client);
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public long getOffset() {
        return offset;
    }

    public List<Client> getClients() {
        return clients;
    }

    public static ClientSearchResult fromJson(JSONObject json) {
        ClientSearchResult clientSearchResult = null;
        try {
            clientSearchResult = new ClientSearchResult();
            JSONObject pagingJSON = json.getJSONObject("paging");
            clientSearchResult.setTotal(pagingJSON.getLong("total"));
            clientSearchResult.setOffset(pagingJSON.getLong("offset"));
            JSONArray resultJSON = (JSONArray) json.get("results");
            for (int i = 0; i < resultJSON.length(); i++) {
                //client= new Client(row.getLong("id"));
                //client.setName(row.getString("name"));
                //client.setLastName(row.getString("lastname"));
                //client.setAddress(row.getString("address"));
                //client.setThumbnail(row.getString("thumbnail"));
                clientSearchResult.addClient(Client.fromJson(resultJSON.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new BusinessException("Error parsing ClientSearchResult",e);
        }
        return clientSearchResult;
    }
}
