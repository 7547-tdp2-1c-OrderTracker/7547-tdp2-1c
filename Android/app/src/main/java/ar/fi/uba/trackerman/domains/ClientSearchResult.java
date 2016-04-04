package ar.fi.uba.trackerman.domains;

import java.util.ArrayList;
import java.util.List;

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
}
