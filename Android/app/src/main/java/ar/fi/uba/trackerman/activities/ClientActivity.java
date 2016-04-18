package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.tasks.GetClientTask;
import ar.fi.uba.trackerman.tasks.OrdersTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.ShowMessage;
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

        View phoneView=findViewById(R.id.client_detail_phone);

        phoneView.setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fab) {

            // validar si existe una orden
            // si no hay orden, crear una nueva
            // si hay orden, mostrar mensaje diciendo que ya existe una orden "activa"
            OrdersTask ot = new OrdersTask();
            String vendor = String.valueOf(AppSettings.getVendorId());
            List orders = ot.getDraftOrders(vendor);
            if (orders == null) {
                // mostrar SnackBar
                CoordinatorLayout cl  = (CoordinatorLayout) findViewById(R.id.client_detail_coordinatorLayout);
                ShowMessage.showSnackbarSimpleMessage(cl, "No se puede iniciar un nuevo pedido 111");
            } else {

                CoordinatorLayout cl  = (CoordinatorLayout) findViewById(R.id.client_detail_coordinatorLayout);
                ShowMessage.showSnackbarSimpleMessage(cl, "No se puede iniciar un nuevo pedido 2222");

//                Intent intent = new Intent(this, ProductsListActivity.class);
  //              intent.putExtra(Intent.EXTRA_UID, clientId);
    //            startActivity(intent);
            }

        }else
        if(view.getId()==R.id.client_detail_phone){
            String uri = "tel:" + ((TextView)view).getText().toString().trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
    }

    public void updateClientInformation(Client client){
            ((CollapsingToolbarLayout) findViewById(R.id.client_detail_collapsing_toolbar)).setTitle(client.getFullName());
            Picasso.with(this).load(client.getAvatar()).into(((ImageView) findViewById(R.id.client_detail_image)));

            ((TextView)findViewById(R.id.client_detail_id)).setText(Long.toString(client.getId()));
            ((TextView)findViewById(R.id.client_detail_name)).setText(client.getFullName());
            ((TextView)findViewById(R.id.client_detail_cuil)).setText(client.getCuil());
            ((TextView)findViewById(R.id.client_detail_address)).setText(client.getAddress());
            ((TextView)findViewById(R.id.client_detail_phone)).setText(client.getPhoneNumber());
            ((TextView)findViewById(R.id.client_detail_email)).setText(client.getEmail());
            String mapURL="https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=400x300&maptype=roadmap&key=AIzaSyB7KkfXSNVvngEQ0LwhvLSt7i1oB4p2RdQ&center="+client.getLat()+','+client.getLon()+"&markers=color:blue%7C"+client.getLat()+','+client.getLon();
            Picasso.with(this).load(mapURL).into(((ImageView) findViewById(R.id.client_detail_map)));
    }
}
