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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.server.LocationService;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.order.GetDraftOrdersTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.MyPreferences;
import fi.uba.ar.soldme.R;

public class MainActivity extends AppCompatActivity implements
        GetDraftOrdersTask.DraftOrdersValidation {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        MyPreferences pref = new MyPreferences(this);
        pref.save(getString(R.string.shared_pref_current_vendor_id), AppSettings.getSellerId());
        pref.save(getString(R.string.shared_pref_current_order_id), -1L);
        pref.save(getString(R.string.shared_pref_current_order_status), "");
        pref.save(getString(R.string.shared_pref_current_client_id), -1L);

        setupNavigationDrawerContent(navigationView);

        this.startCleanUpUI();

        ((TextView) findViewById(R.id.fragment_main_vendor_name)).setText("Vendedor #" + AppSettings.getSellerId() + ". Inter=" + RestClient.isOnline(this));
        LocationService ls = new LocationService(this);
        ls.config(new LocationService.MyLocation() {
            @Override
            public void processLocation(Location loc) {
                ((TextView) findViewById(R.id.fragment_main_vendor_name)).setText("Vendedor #" + AppSettings.getSellerId() + ". Inter=" + RestClient.isOnline(MainActivity.this) + ". POS lat=" + loc.getLatitude()+" lon=" + loc.getLongitude());
            }
        });

        if (RestClient.isOnline(this)) new GetDraftOrdersTask(this).execute(String.valueOf(AppSettings.getSellerId()));
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
        if (orders.size() > 0) {
            if (orders.size() == 1) message.setText("Tienes un pedido activo!");
            else message.setText("Tienes "+ orders.size() +" pedidos activos!");
        } else {
            message.setText("No hay pedidos activos");
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

    public void openProductsActivity(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
    }

    public void openLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openLogoutActivity(View view) {
        Toast.makeText(getApplicationContext(), "sin implementar", Toast.LENGTH_LONG).show();
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
}
