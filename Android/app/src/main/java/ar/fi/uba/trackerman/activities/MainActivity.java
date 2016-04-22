package ar.fi.uba.trackerman.activities;

import android.content.Intent;
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

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.tasks.GetDraftOrdersTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import fi.uba.ar.soldme.R;

public class MainActivity extends AppCompatActivity implements
        GetDraftOrdersTask.DraftOrdersValidation,
        NavigationView.OnNavigationItemSelectedListener {

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        String[] options={getString(R.string.clients),getString(R.string.products)};

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, options));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    openMyClientsActivity(null);
                } else if (position == 1) {
                    openProductsActivity(null);
                } else {
                    openOrdersActivity(null);
                }
            }
        });
        */

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    public void openProductsActivity(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
    }

    public void openOrdersActivity(View view) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(Intent.EXTRA_UID,14l);
        startActivity(intent);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            openMyClientsActivity(null);
        } else if (id == R.id.nav_gallery) {
            openProductsActivity(null);
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(getApplicationContext(), "sin implementar", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(getApplicationContext(), "sin implementar", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
