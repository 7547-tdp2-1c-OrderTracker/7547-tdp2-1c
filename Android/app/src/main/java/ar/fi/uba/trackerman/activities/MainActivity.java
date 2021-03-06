package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.domains.Report;
import ar.fi.uba.trackerman.domains.Seller;
import ar.fi.uba.trackerman.server.LocationService;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.order.GetDraftOrdersTask;
import ar.fi.uba.trackerman.tasks.report.GetReportTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.CircleTransform;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.MyPreferenceHelper;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;
import fi.uba.ar.soldme.services.RegistrationIntentService;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

public class MainActivity extends AppCompatActivity implements
        GetDraftOrdersTask.DraftOrdersValidation, GetReportTask.ReportReceiver {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final MyPreferenceHelper helper = new MyPreferenceHelper(MainActivity.this);
        final MyPreferences pref = new MyPreferences(this);
        if (helper.getSeller() == null || pref.get(getString(R.string.shared_pref_current_token),"").isEmpty()) {
            openLoginActivity(drawerLayout);
            return;
        }

        pref.save(getString(R.string.shared_pref_current_order_id), -1L);
        pref.save(getString(R.string.shared_pref_current_order_status), "");
        pref.save(getString(R.string.shared_pref_current_schedule_date), DateUtils.formatShortDate(Calendar.getInstance().getTime()));

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            // FIXME:
            // Es para poner el nombre del vendedor en el menu lateral. Lo pone con delay.
            // Si lo pongo directamente en el onCreate, explota por null
            @Override
            public void onDrawerOpened(View drawerView) {
                Seller seller = helper.getSeller();

                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                TextView sellerName = ((TextView) navigationView.findViewById(R.id.nav_header_main_vendor_name));
                TextView sellerEmail = ((TextView) navigationView.findViewById(R.id.nav_header_main_vendor_email));

                if (sellerName != null) {
                    String fullSellerName = seller.getFullName() + "  (#"+seller.getId()+")";
                    sellerName.setText(fullSellerName);
                    sellerEmail.setText(seller.getEmail());
                    String avatar = isContentValid(seller.getAvatar());
                    ImageView mainAvatar = (ImageView) findViewById(R.id.menu_header_logo);
                    if (avatar != null && !avatar.isEmpty()) {
                        Picasso.with(MainActivity.this).load(avatar).transform(new CircleTransform()).into(mainAvatar);
                    } else {
                        Picasso.with(MainActivity.this).load(R.drawable.icon).into(mainAvatar);
                    }
                }
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        //setupNavigationDrawerContent(navigationView);

        this.startCleanUpUI();

//        ((TextView) navigationView.findViewById(R.id.nav_header_main_vendor_name)).setText("Vendedor #" + AppSettings.getSellerId());

        // -----
        //Tomo los ultimos datos, si no tengo nada tomo los hardcoded.
        String lat = pref.get(getString(R.string.shared_pref_current_location_lat), AppSettings.getGpsLat());
        String lon = pref.get(getString(R.string.shared_pref_current_location_lon), AppSettings.getGpsLon());
        if (lat.equals(AppSettings.getGpsLat()) && lon.equals(AppSettings.getGpsLon())) {
            debugCardMessage(null);
        } else {
            Location l = new Location("");
            l.reset();
            l.setLatitude(Double.valueOf(lat));
            l.setLongitude(Double.valueOf(lon));
            debugCardMessage(l);
        }



        LocationService ls = new LocationService(this);
        ls.config(new LocationService.MyLocation() {
            @Override
            public void processLocation(Location loc) {
                debugCardMessage(loc);
                pref.save(getString(R.string.shared_pref_current_location_lat), String.valueOf(loc.getLatitude()));
                pref.save(getString(R.string.shared_pref_current_location_lon), String.valueOf(loc.getLongitude()));
            }
        });

        if (RestClient.isOnline(this)) {
            new GetDraftOrdersTask(this).execute(String.valueOf(helper.getSeller().getId()));

            //new GetReportTask(this).execute("2015-01-01","2017-01-01");
            Calendar today = Calendar.getInstance();
            String start = DateUtils.formatShortDate(today.getTime());
            today.set(Calendar.DATE, today.get(Calendar.DATE)+1);
            String end = DateUtils.formatShortDate(today.getTime());
            new GetReportTask(this).execute(start,end);
        }

        registerDevice();
    }

    public void updateReport(Report report){
        String msgDinero = "";
        if (!report.getMoneyReports().isEmpty()) {
            msgDinero += "Dinero recaudado = ";
            for(Report.MoneyReport mr : report.getMoneyReports()) msgDinero += mr;
        } else {
            msgDinero += "No hay dinero recaudado :(";
        }

        ((TextView) findViewById(R.id.fragment_main_report_atendidos)).setText("Visitas realizadas = " + report.getVisits());
        ((TextView) findViewById(R.id.fragment_main_report_vendidos)).setText("Pedidos Confirmados = " + report.getConfirmedOrders());
        ((TextView) findViewById(R.id.fragment_main_report_fuera_de_ruta)).setText("Visitas fuera de ruta = " + report.getOutOfRouteVisits());
        ((TextView) findViewById(R.id.fragment_main_report_dinero)).setText(msgDinero);
    }

    public void showSnackbarSimpleMessage(String message){
        ShowMessage.showSnackbarSimpleMessage(this.getCurrentFocus(),message);
    }

    private void debugCardMessage(Location loc) {
        String position = "POS HARD";
        if (loc != null) {
            position = "POS lat="+loc.getLatitude()+" lon="+ loc.getLongitude();
        }
        ((TextView) findViewById(R.id.fragment_main_vendor_name)).setText(position);
    }

    private void startCleanUpUI() {
        ((TextView)findViewById(R.id.dashboard_draft_orders)).setText("");
    }

    public void openDetailOrder(View view) {
        openMyOrdersActivity(null);
    }

    @Override
    public void setDraftOrders(List<OrderWrapper> orders) {
        TextView message = (TextView) findViewById(R.id.dashboard_draft_orders);
        if (orders == null) {
            message.setText("Carámbas! Server durmiendo -.-");
        } else {
            if (orders.size() > 0) {
                if (orders.size() == 1) message.setText("Tienes un pedido activo!");
                else message.setText("Tienes "+ orders.size() +" pedidos activos!");
            } else {
                message.setText("No hay pedidos activos");
            }
        }
    }

    public void openMyClientsActivity(View view) {
        Intent intent = new Intent(this, MyClientsActivity.class);
        startActivity(intent);
    }

    public void openMyOrdersActivity(View view) {
        Intent intent = new Intent(this, MyOrdersActivity.class);
        startActivity(intent);
    }

    public void openMyDayAgendaActivity(View view) {
        Intent intent = new Intent(this, MyDayAgendaActivity.class);
        startActivity(intent);
    }

    public void openMyWeekAgendaActivity(View view) {
        Intent intent = new Intent(this, MyWeekAgendaActivity.class);
        startActivity(intent);
    }

    public void openScanQRCodeActivity(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    public void openProductsActivity(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
    }

    public void openLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openLogoutActivity(View view) {
        Toast.makeText(getApplicationContext(), "Los Héroes no abandonan!", Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
    }

    public void openOrdersActivity(View view) {
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

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.nav_clients:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openMyClientsActivity(null);
                                return true;
                            case R.id.nav_products:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openProductsActivity(null);
                                return true;
                            case R.id.nav_orders:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openMyOrdersActivity(null);
                                return true;
                            case R.id.nav_day_agenda:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openMyDayAgendaActivity(null);
                                return true;
                            case R.id.nav_week_agenda:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openMyWeekAgendaActivity(null);
                                return true;
                            case R.id.nav_scan_codigo:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openScanQRCodeActivity(null);
                                return true;
                            case R.id.nav_login:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openLoginActivity(null);
                                return true;
                            case R.id.nav_logout:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openLogoutActivity(null);
                                return true;
                        }
                        return true;
                    }
                });
    }

    private void registerDevice(){
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
