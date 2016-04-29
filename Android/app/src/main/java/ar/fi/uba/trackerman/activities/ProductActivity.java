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
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import ar.fi.uba.trackerman.domains.Brand;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.tasks.brand.GetBrandTask;
import ar.fi.uba.trackerman.tasks.order.GetDraftOrdersTask;
import ar.fi.uba.trackerman.tasks.product.GetProductTask;
import ar.fi.uba.trackerman.tasks.order.PostOrderItemsTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;


public class ProductActivity extends AppCompatActivity implements GetProductTask.ProductReceiver, View.OnClickListener, GetDraftOrdersTask.DraftOrdersValidation, PostOrderItemsTask.OrderItemCreator {

    private long productId;
    private int quantity;
    private List<Order> draftOrders;

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

        Intent intent = getIntent();
        productId = intent.getLongExtra(Intent.EXTRA_UID, 0);
        new GetProductTask(this).execute(Long.toString(productId));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        long orderId = (new MyPreferences(ProductActivity.this)).get(getString(R.string.shared_pref_current_order_id),-1L);

        if(orderId == -1){
            fab.hide();
        } else {
            fab.show();
        }

        this.startCleanUpUI();

        //Preguntamos por las ordenes
        new GetDraftOrdersTask(this).execute(String.valueOf(AppSettings.getVendorId()));
    }


    private void startCleanUpUI() {
        ((CollapsingToolbarLayout) findViewById(R.id.product_detail_collapsing_toolbar)).setTitle("");

        ((TextView) findViewById(R.id.product_detail_id)).setText("");
        ((TextView) findViewById(R.id.product_detail_name)).setText("");
        ((TextView) findViewById(R.id.product_detail_brand)).setText("");
        ((TextView) findViewById(R.id.product_detail_stock)).setText("");
        ((TextView) findViewById(R.id.product_detail_price1)).setText("");
        ((TextView) findViewById(R.id.product_detail_price2)).setText("");
        ((TextView)findViewById(R.id.product_detail_description)).setText("");
    }

    public void showSnackbarSimpleMessage(String msg){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.product_detail_coordinatorLayout);
        ShowMessage.showSnackbarSimpleMessage(coordinatorLayout, msg);
    }

    public boolean isValidQuantity(String txt) {
        if (txt.isEmpty()) return false;
        return Integer.parseInt(txt) > 0;
    }

    public boolean isValidStock(String txt) {
        return Integer.parseInt(txt) <= this.quantity;
    }

    public void showQuantityDialog() {

        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter.LengthFilter filter= new InputFilter.LengthFilter(4);
        edittext.setFilters(new InputFilter[] { filter});

        new AlertDialog.Builder(this)
                .setTitle("Indicar cantidad")
                .setMessage("")
                .setView(edittext)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String quantityRequested = edittext.getText().toString();

                        if (isValidQuantity(quantityRequested)) {
                            if (isValidStock(quantityRequested)) {

                                long orderId = (new MyPreferences(ProductActivity.this)).get(getString(R.string.shared_pref_current_order_id), -1L);

                                if (orderId >= 0) {
                                    addingItemsToOrder(String.valueOf(orderId), String.valueOf(productId), quantityRequested);
                                } else {
                                    showSnackbarSimpleMessage("No hay un pedido asociado!");
                                }
                            } else {
                                showSnackbarSimpleMessage("Lo siento! disponemos de " + quantity + " unidades");
                            }
                        } else {
                            showSnackbarSimpleMessage("El valor es inválido!");
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();

    }


    private void addingItemsToOrder(String orderId, String productId, String quantity) {
        new PostOrderItemsTask(this).execute(orderId, productId, quantity);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fab) {
            CoordinatorLayout cl = (CoordinatorLayout) findViewById(R.id.product_detail_coordinatorLayout);
            if (this.draftOrders == null) {
                ShowMessage.showSnackbarSimpleMessage(cl, "No se pudo obtener info del pedido");
            } else if (this.draftOrders.size() == 0) {
                // si no hay orden, Avisar
                ShowMessage.showSnackbarSimpleMessage(cl, "No hay pedido en curso. Cree uno desde el cliente!");
            } else if (this.quantity == 0) {
                // si no hay stock
                ShowMessage.showSnackbarSimpleMessage(cl, "No tenemos stock del producto!");
            } else {
                // si hay orden, mostrar mensaje diciendo que ya existe una orden "activa"
                showQuantityDialog();
            }
        }
    }

    public void updateProductInformation(Product product) {
        this.quantity = product.getStock();

        ((CollapsingToolbarLayout) findViewById(R.id.product_detail_collapsing_toolbar)).setTitle(product.getName());
        Picasso.with(this).load(product.getPicture()).into(((ImageView) findViewById(R.id.product_detail_image)));

        ((TextView) findViewById(R.id.product_detail_id)).setText(isContentValid(Long.toString(product.getId())));
        ((TextView) findViewById(R.id.product_detail_name)).setText(isContentValid(product.getName()));

        Long brandId = product.getBrandId();
        new GetBrandTask(this).execute(String.valueOf(brandId));

        ((TextView) findViewById(R.id.product_detail_brand)).setText(isContentValid(String.valueOf(brandId)));
        ((TextView) findViewById(R.id.product_detail_stock)).setText(isContentValid(Long.toString(product.getStock())));
        ((TextView) findViewById(R.id.product_detail_price1)).setText(isContentValid(product.getRetailPriceWithCurrency()));
        ((TextView) findViewById(R.id.product_detail_price2)).setText(isContentValid(product.getWholeSalePriceWithCurrency()));

        ((TextView)findViewById(R.id.product_detail_description)).setText(isContentValid(product.getDescription()));
    }

    public void afterBrandResolve(Brand brand){
        ((TextView) findViewById(R.id.product_detail_brand)).setText(isContentValid(brand.getName()));
    }

    @Override
    public void setDraftOrders(List<Order> orders) {
        this.draftOrders = orders;
    }

    public void afterCreatingOrderItem(OrderItem orderItem) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(Intent.EXTRA_UID, orderItem.getOrderId());
        startActivity(intent);
    }

    @Override
    public View getCurrentView() {
        return this.getCurrentFocus();
    }
}
