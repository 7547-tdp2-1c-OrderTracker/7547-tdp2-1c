package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.tasks.GetDraftOrdersTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import fi.uba.ar.soldme.R;

public class MainActivity extends AppCompatActivity implements
        GetDraftOrdersTask.DraftOrdersValidation {

    DrawerLayout drawerLayout;
    private Order order;

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

        setupNavigationDrawerContent(navigationView);

        new GetDraftOrdersTask(this).execute(String.valueOf(AppSettings.getVendorId()));
    }

    public void openDetailOrder(View view) {
        if (order != null) {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra(Intent.EXTRA_UID,this.order.getId());
            startActivity(intent);
        }
    }

    @Override
    public void setDraftOrders(List<Order> orders) {
        TextView message = (TextView) findViewById(R.id.client_detail_address);
        if (orders.size() > 0) {
            this.order = orders.get(0);
            message.setText("Tienes un pedido Activo!");
        } else {
            this.order = null;
            message.setText("No hay pedido en curso");
        }
    }

    public void openMyClientsActivity(View view) {
        Intent intent = new Intent(this, MyClientsActivity.class);
        startActivity(intent);
    }

    public void openMyOrdersActivity(View view) {
        Toast.makeText(getApplicationContext(), "sin implementar", Toast.LENGTH_LONG).show();
    }

    public void openMyCalendarActivity(View view) {
        Toast.makeText(getApplicationContext(), "sin implementar", Toast.LENGTH_LONG).show();
    }

    public void openProductsActivity(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
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
                            case R.id.nav_calendar_day:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                openMyCalendarActivity(null);
                                return true;
                        }
                        return true;
                    }
                });
    }
}
