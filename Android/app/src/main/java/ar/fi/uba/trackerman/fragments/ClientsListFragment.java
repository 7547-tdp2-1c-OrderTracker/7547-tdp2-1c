package ar.fi.uba.trackerman.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.adapters.ClientsListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 29/3/16.
 */
public class ClientsListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public ClientsListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView= inflater.inflate(R.layout.fragment_clients_list, container, false);
        ListView clientsList= (ListView)fragmentView.findViewById(R.id.clientListView);

        ClientsListAdapter clientsAdapter = new ClientsListAdapter( getActivity(), getContext(), R.layout.list_client_item, new ArrayList<Client>());
        clientsList.setAdapter(clientsAdapter);
        clientsList.setOnItemClickListener(this);

        ProgressBar bar= new ProgressBar(getContext());
        bar.setIndeterminate(true);
        clientsList.setEmptyView(bar);
        clientsAdapter.refresh();
        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Client client= (Client)parent.getItemAtPosition(position);
        Intent intent = new Intent(getContext(), ClientActivity.class);
        intent.putExtra(Intent.EXTRA_UID,client.getId());
        startActivity(intent);
    }
}
