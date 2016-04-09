package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.tasks.GetClientTask;
import fi.uba.ar.soldme.R;

public class ClientActivity extends AppCompatActivity implements GetClientTask.ClientReciver{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        long clientId= intent.getLongExtra(Intent.EXTRA_UID, 0);
        new GetClientTask(this).execute(Long.toString(clientId));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.create_order_comment), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void updateClientInformation(Client client){
            Picasso.with(this).load(client.getAvatar()).into(((ImageView) findViewById(R.id.client_detail_image)));
            ((TextView)findViewById(R.id.client_detail_id)).setText(Long.toString(client.getId()));
            ((TextView)findViewById(R.id.client_detail_lastname)).setText(client.getLastName());
            ((TextView)findViewById(R.id.client_detail_name)).setText(client.getName());
            ((TextView)findViewById(R.id.client_detail_cuil)).setText(client.getCuil());
            ((TextView)findViewById(R.id.client_detail_address)).setText(client.getAddress());
            ((TextView)findViewById(R.id.client_detail_phone)).setText(client.getPhoneNumber());
            ((TextView)findViewById(R.id.client_detail_email)).setText(client.getEmail());
    }
}
