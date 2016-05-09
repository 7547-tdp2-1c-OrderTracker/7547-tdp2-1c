package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.client.GetClientTask;
import ar.fi.uba.trackerman.tasks.order.GetDraftOrdersTask;
import ar.fi.uba.trackerman.tasks.order.PostOrdersTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;
import static ar.fi.uba.trackerman.utils.FieldValidator.isValidMail;
import static ar.fi.uba.trackerman.utils.FieldValidator.isValidPhone;

public class ClientActivity extends AppCompatActivity implements GetClientTask.ClientReceiver, View.OnClickListener, GetDraftOrdersTask.DraftOrdersValidation{

    private long clientId;
    private List<OrderWrapper> draftOrders;

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
        clientId = intent.getLongExtra(Intent.EXTRA_UID, 0);
        if (RestClient.isOnline(this)) new GetClientTask(this).execute(Long.toString(clientId));

        findViewById(R.id.client_detail_phone).setOnClickListener(this);
        findViewById(R.id.client_detail_phone_number_icon).setOnClickListener(this);
        findViewById(R.id.client_detail_email).setOnClickListener(this);
        findViewById(R.id.client_detail_email_icon).setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        MyPreferences pref = new MyPreferences(this);
        pref.save(getString(R.string.shared_pref_current_order_id), -1L);
        pref.save(getString(R.string.shared_pref_current_order_status), "");
        pref.save(getString(R.string.shared_pref_current_client_id), clientId);

        this.startCleanUpUI();
        if (RestClient.isOnline(this)) new GetDraftOrdersTask(this).execute( String.valueOf(AppSettings.getSellerId()), String.valueOf(clientId) );
    }

    private void startCleanUpUI() {
        ((CollapsingToolbarLayout) findViewById(R.id.client_detail_collapsing_toolbar)).setTitle("");

        ((TextView) findViewById(R.id.client_detail_id)).setText("");
        ((TextView) findViewById(R.id.client_detail_name)).setText("");
        ((TextView) findViewById(R.id.client_detail_company)).setText("");
        ((TextView) findViewById(R.id.client_detail_cuil)).setText("");
        ((TextView) findViewById(R.id.client_detail_address)).setText("");
        ((TextView) findViewById(R.id.client_detail_phone)).setText("");
        ((TextView) findViewById(R.id.client_detail_email)).setText("");
    }

    public void showSnackbarSimpleMessage(String msg){
        ShowMessage.showSnackbarSimpleMessage(this.getCurrentFocus(), msg);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fab) {
            CoordinatorLayout cl = (CoordinatorLayout) findViewById(R.id.client_detail_coordinatorLayout);
            if (this.draftOrders == null) {
                ShowMessage.showSnackbarSimpleMessage(cl, "No se pudo obtener info del pedido");
            } else if (this.draftOrders.size() > 0) {
                // si hay orden, mostrar mensaje diciendo que ya existe una orden "activa"
                ShowMessage.showSnackbarSimpleMessage(cl, "Ya existe un pedido borrador en curso!");
            } else {
                // si no hay orden, crear una nueva
                if (RestClient.isOnline(this)) new PostOrdersTask(this).execute(String.valueOf(AppSettings.getSellerId()), Long.toString(clientId));
            }
        } else if((view.getId() == R.id.client_detail_phone || view.getId() == R.id.client_detail_phone_number_icon)
                && isValidPhone(((TextView) findViewById(R.id.client_detail_phone)).getText())){
            String uri = "tel:" + ((TextView) findViewById(R.id.client_detail_phone)).getText().toString().trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
        if((view.getId() == R.id.client_detail_email || view.getId() == R.id.client_detail_email_icon)
                && isValidMail(((TextView) findViewById(R.id.client_detail_email)).getText())){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",((TextView) findViewById(R.id.client_detail_email)).getText().toString().trim(), null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "[OrderTracker] - Mensaje del vendedor");
            startActivity(Intent.createChooser(intent, "Send email..."));
        }
    }

    public void updateClientInformation(Client client){
        ((CollapsingToolbarLayout) findViewById(R.id.client_detail_collapsing_toolbar)).setTitle(client.getFullName());
        if (isContentValid(client.getAvatar()).isEmpty()) {
            Picasso.with(this).load(R.drawable.logo).into(((ImageView) findViewById(R.id.client_detail_image)));
        } else {
            Picasso.with(this).load(client.getAvatar()).into(((ImageView) findViewById(R.id.client_detail_image)));
        }

        ((TextView) findViewById(R.id.client_detail_id)).setText(isContentValid(Long.toString(client.getId())));
        ((TextView) findViewById(R.id.client_detail_name)).setText(isContentValid(client.getFullName()));
        ((TextView) findViewById(R.id.client_detail_company)).setText(isContentValid(client.getCompany()));
        ((TextView) findViewById(R.id.client_detail_cuil)).setText(isContentValid(client.getCuil()));
        ((TextView) findViewById(R.id.client_detail_address)).setText(isContentValid(client.getAddress()));

        int colorAccent = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);

        TextView phoneField = ((TextView)findViewById(R.id.client_detail_phone));
        phoneField.setText(isContentValid(client.getPhoneNumber()));
        if (isValidPhone(phoneField.getText())) {
            ((ImageView) findViewById(R.id.client_detail_phone_number_icon)).setColorFilter(colorAccent);
            phoneField.setTextColor(colorAccent);
        }

        TextView mailField = ((TextView)findViewById(R.id.client_detail_email));
        mailField.setText(isContentValid(client.getEmail()));
        if (isValidMail(mailField.getText())) {
            ((ImageView)findViewById(R.id.client_detail_email_icon)).setColorFilter(colorAccent);
            mailField.setTextColor(colorAccent);
        }

        String mapURL="https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=400x300&maptype=roadmap&key=AIzaSyB7KkfXSNVvngEQ0LwhvLSt7i1oB4p2RdQ&center="+client.getLat()+','+client.getLon()+"&markers=color:blue%7C"+client.getLat()+','+client.getLon();
        Picasso.with(this).load(mapURL).into(((ImageView) findViewById(R.id.client_detail_map)));
    }

    @Override
    public void setDraftOrders(List<OrderWrapper> orders) {
        this.draftOrders = orders;
    }

    public void afterCreatingOrder(Order orderCreated) {

        MyPreferences pref = new MyPreferences(this);
        pref.save(getString(R.string.shared_pref_current_order_id), orderCreated.getId());
        pref.save(getString(R.string.shared_pref_current_order_status), orderCreated.getStatus());

        Intent intent = new Intent(this, ProductsListActivity.class);
        intent.putExtra(Intent.EXTRA_UID, orderCreated.getId());
        startActivity(intent);
    }
}
