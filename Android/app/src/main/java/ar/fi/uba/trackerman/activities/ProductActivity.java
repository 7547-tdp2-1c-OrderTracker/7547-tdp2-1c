package ar.fi.uba.trackerman.activities;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.tasks.GetProductTask;
import fi.uba.ar.soldme.R;


public class ProductActivity extends AppCompatActivity implements GetProductTask.ProductReciver, View.OnClickListener{

    private long productId;

    public ProductActivity(){
        super();
        productId=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        productId= intent.getLongExtra(Intent.EXTRA_UID, 0);
        new GetProductTask(this).execute(Long.toString(productId));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        intent.putExtra(Intent.EXTRA_UID,productId);
        startActivity(intent);
    }

    public void updateProductInformation(Product product) {
        ((CollapsingToolbarLayout) findViewById(R.id.product_detail_collapsing_toolbar)).setTitle(product.getName());
        Picasso.with(this).load(product.getPicture()).into(((ImageView) findViewById(R.id.product_detail_image)));

        ((TextView) findViewById(R.id.product_detail_id)).setText(Long.toString(product.getId()));
        ((TextView) findViewById(R.id.product_detail_name)).setText(product.getName());
        ((TextView) findViewById(R.id.product_detail_brand)).setText(product.getBrand());
        ((TextView) findViewById(R.id.product_detail_stock)).setText(Long.toString(product.getStock()));
        ((TextView) findViewById(R.id.product_detail_description)).setText(product.getDescription());
    }

}
