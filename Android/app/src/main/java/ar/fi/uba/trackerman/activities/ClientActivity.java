package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.tasks.GetClientTask;
import fi.uba.ar.soldme.R;

public class ClientActivity extends AppCompatActivity implements GetClientTask.ClientReciver, View.OnClickListener{

    private long clientId;

    public ClientActivity(){
        super();
        clientId=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        clientId= intent.getLongExtra(Intent.EXTRA_UID, 0);
        new GetClientTask(this).execute(Long.toString(clientId));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        intent.putExtra(Intent.EXTRA_UID,clientId);
        startActivity(intent);
    }

    public void updateClientInformation(Client client){
            ((CollapsingToolbarLayout) findViewById(R.id.client_detail_collapsing_toolbar)).setTitle(client.getLastName()+", "+client.getName());
            Picasso.with(this).load(client.getAvatar()).into(((ImageView) findViewById(R.id.client_detail_image)));
            ((TextView)findViewById(R.id.client_detail_id)).setText(Long.toString(client.getId()));
            //((TextView)findViewById(R.id.client_detail_lastname)).setText(client.getLastName());
            ((TextView)findViewById(R.id.client_detail_name)).setText(client.getLastName() +", "+client.getName());
            ((TextView)findViewById(R.id.client_detail_cuil)).setText(client.getCuil());
            ((TextView)findViewById(R.id.client_detail_address)).setText(client.getAddress());
            ((TextView)findViewById(R.id.client_detail_phone)).setText(client.getPhoneNumber());
            ((TextView)findViewById(R.id.client_detail_email)).setText(client.getEmail());
            String mapURL="https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=400x300&maptype=roadmap&key=AIzaSyB7KkfXSNVvngEQ0LwhvLSt7i1oB4p2RdQ&center="+client.getLat()+','+client.getLon()+"&markers=color:blue%7C"+client.getLat()+','+client.getLon();
            Picasso.with(this).load(mapURL).into(((ImageView) findViewById(R.id.client_detail_map)));
    }
}
