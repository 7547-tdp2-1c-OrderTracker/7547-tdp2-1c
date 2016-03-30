package ar.fi.uba.trackerman.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;


import ar.fi.uba.trackerman.adapters.ClientsListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.tasks.GetClientTask;
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

        new GetClientTask(clientAdapter).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_clients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
