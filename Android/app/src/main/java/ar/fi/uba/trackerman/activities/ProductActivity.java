package ar.fi.uba.trackerman.activities;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.tasks.GetProductTask;
import fi.uba.ar.soldme.R;


public class ProductActivity extends AppCompatActivity implements GetProductTask.ProductReciver, View.OnClickListener{

    private long productId;
    private int quantity;

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

    public void showSnackbarSimpleMessage(String msg){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.product_detail_coordinatorLayout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public boolean isValidQuantity(String txt) {
        if (txt.isEmpty()) return false;
        if (Integer.parseInt(txt.toString()) == 0) return false;
        return true;
    }

    public boolean isValidStock(String txt) {
        if (isValidQuantity(txt)) {
            if (Integer.parseInt(txt.toString()) <= this.quantity) return true;
        }
        return false;
    }

    public void showQuantityDialog() {

        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(this)
                .setTitle("Indicar cantidad")
                .setMessage("")
                .setView(edittext)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String editTextValue = edittext.getText().toString();

                        if (isValidQuantity(editTextValue)) {
                            if (isValidStock(editTextValue)){

                                // TODO: agregar el item al pedido usando la cantidad

                                String msg = "Cantidad:"+ editTextValue + "Agregar producto al carro y mostrar pedido";
                                showSnackbarSimpleMessage(msg);
                            } else {
                                showSnackbarSimpleMessage("Stock inválido");
                            }
                        } else {
                            showSnackbarSimpleMessage("Valor inválido");
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.fab) {
            showQuantityDialog();
        }

    }

    public void updateProductInformation(Product product) {

        this.quantity = product.getStock();

        ((CollapsingToolbarLayout) findViewById(R.id.product_detail_collapsing_toolbar)).setTitle(product.getName());
        Picasso.with(this).load(product.getPicture()).into(((ImageView) findViewById(R.id.product_detail_image)));

        ((TextView) findViewById(R.id.product_detail_id)).setText(Long.toString(product.getId()));
        ((TextView) findViewById(R.id.product_detail_name)).setText(product.getName());
        ((TextView) findViewById(R.id.product_detail_brand)).setText(product.getBrand());
        ((TextView) findViewById(R.id.product_detail_stock)).setText(Long.toString(product.getStock()));
        ((TextView) findViewById(R.id.product_detail_price)).setText(product.getPriceWithCurrency());
        ((TextView) findViewById(R.id.product_detail_description)).setText(product.getDescription());

    }

}
