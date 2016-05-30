package ar.fi.uba.trackerman.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import ar.fi.uba.trackerman.domains.Brand;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.domains.Promotion;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.brand.GetBrandTask;
import ar.fi.uba.trackerman.tasks.order.PostOrderItemsTask;
import ar.fi.uba.trackerman.tasks.product.GetProductTask;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;


public class ProductActivity extends AppCompatActivity implements GetProductTask.ProductReceiver, View.OnClickListener, PostOrderItemsTask.OrderItemCreator {

    private long productId;
    private int quantity;
    private MyPreferences pref = new MyPreferences(this);

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
        if (RestClient.isOnline(this)) new GetProductTask(this).execute(Long.toString(productId));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        long orderId = pref.get(getString(R.string.shared_pref_current_order_id),-1L);

        if(orderId == -1){
            fab.hide();
        } else {
            fab.show();
        }

        this.startCleanUpUI();
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

        // by default hide promotion card
        ((CardView)findViewById(R.id.product_detail_card_promotion)).setVisibility(View.GONE);

        ((TextView)findViewById(R.id.product_detail_product_promotion_percent)).setText("");
        ((TextView)findViewById(R.id.product_detail_product_promotion_dates)).setText("");
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

                                long orderId = pref.get(getString(R.string.shared_pref_current_order_id), -1L);

                                if (orderId >= 0) {
                                    addingItemsToOrder(String.valueOf(orderId), String.valueOf(productId), quantityRequested);
                                } else {
                                    showSnackbarSimpleMessage("No hay un pedido asociado!");
                                }
                            } else {
                                showSnackbarSimpleMessage("Lo siento! "+ ((quantity>0)? "disponemos de " + quantity + " unidades." : "no disponemos de unidades."));
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
        if (RestClient.isOnline(this)) new PostOrderItemsTask(this).execute(orderId, productId, quantity);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fab) {
            if (this.quantity == 0) {
                // si no hay stock
                ShowMessage.showSnackbarSimpleMessage(findViewById(R.id.product_detail_coordinatorLayout), "No tenemos stock del producto!");
            } else {
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
        if (RestClient.isOnline(this)) new GetBrandTask(this).execute(String.valueOf(brandId));

        ((TextView) findViewById(R.id.product_detail_brand)).setText(isContentValid(String.valueOf(brandId)));
        ((TextView) findViewById(R.id.product_detail_stock)).setText(isContentValid(Long.toString(product.getStock())));
        ((TextView) findViewById(R.id.product_detail_price1)).setText(isContentValid(product.getRetailPriceWithCurrency()));
        ((TextView) findViewById(R.id.product_detail_price2)).setText(isContentValid(product.getWholeSalePriceWithCurrency()));

        if (product.hasPromotion()) {
            Promotion promotion = product.getBestPromotion();
            Date promotionBeginDate = promotion.getBeginDate();
            Date promotionEndDate = promotion.getEndDate();
            String promotionBeginDateStr = DateUtils.formatShortDateArg(promotionBeginDate);
            String promotionEndDateStr = DateUtils.formatShortDateArg(promotionEndDate);

            String promotionFullDateStr = promotionBeginDateStr + " / "+ promotionEndDateStr;
            String promotionPercent = isContentValid(Integer.toString(promotion.getPercent()) +" %"+ ((promotion.getMinQuantity() != 0)?"  |  Cant. Mínima: "+ promotion.getMinQuantity():""));

            ((TextView) findViewById(R.id.product_detail_product_promotion_name)).setText(promotion.getName());
            ((TextView) findViewById(R.id.product_detail_product_promotion_percent)).setText(promotionPercent);
            ((TextView) findViewById(R.id.product_detail_product_promotion_dates)).setText(promotionFullDateStr);

            ((CardView)findViewById(R.id.product_detail_card_promotion)).setVisibility(View.VISIBLE);
        }



        ((TextView)findViewById(R.id.product_detail_description)).setText(isContentValid(product.getDescription()));
    }

    public void afterBrandResolve(Brand brand){
        ((TextView) findViewById(R.id.product_detail_brand)).setText(isContentValid(brand.getName()));
    }

    public void afterCreatingOrderItem(OrderItem orderItem) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(Intent.EXTRA_UID, orderItem.getOrderId());
        startActivity(intent);
    }

    @Override
    public View getCurrentView() {
        return this.getWindow().getDecorView();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
