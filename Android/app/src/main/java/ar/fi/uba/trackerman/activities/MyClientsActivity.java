package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


import ar.fi.uba.trackerman.adapters.ClientsListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.tasks.GetClientListTask;
import fi.uba.ar.soldme.R;

public class MyClientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_my_clients);
        setSupportActionBar(toolbar);

        ListView lista= (ListView)findViewById(R.id.listView);
        ArrayAdapter<Client> clientAdapter;
        clientAdapter = new ClientsListAdapter( this, R.layout.list_client_item, new ArrayList<Client>());
        lista.setAdapter(clientAdapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client client= (Client)parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ClientDetailActivity.class);
                intent.putExtra(Intent.EXTRA_UID,client.getId());
                startActivity(intent);
            }
        });

        new GetClientListTask(clientAdapter).execute();
    }
}
