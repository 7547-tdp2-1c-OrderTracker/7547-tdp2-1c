package ar.fi.uba.trackerman.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.fragments.ProductsListFragment;
import fi.uba.ar.soldme.R;

public class ProductsListActivity extends AppCompatActivity {
    private String brands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        if (savedInstanceState!=null){
            brands=savedInstanceState.getString("EXTRA_BRANDS",null);
        }else {
            brands=null;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_products, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_filter) {
            Intent intent = new Intent(this, ProductsFilterActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                brands=data.getStringExtra("EXTRA_BRANDS");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                brands=null;
            }
        }
        ProductsListFragment fragment=(ProductsListFragment) getSupportFragmentManager().findFragmentById(R.id.products_list_fragment);
        fragment.setBrands(brands);
    }
}
