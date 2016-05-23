package ar.fi.uba.trackerman.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ar.fi.uba.trackerman.adapters.OrderItemsListAdapter;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.order.CancellOrderTask;
import ar.fi.uba.trackerman.tasks.order.ConfirmOrderTask;
import ar.fi.uba.trackerman.tasks.order.EmptyOrderTask;
import ar.fi.uba.trackerman.tasks.order.GetOrderTask;
import ar.fi.uba.trackerman.tasks.order.RemoveOrderItemTask;
import ar.fi.uba.trackerman.tasks.order.UpdateOrderItemTask;
import ar.fi.uba.trackerman.utils.ConfirmDialog;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.OrderStatus;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;
import static ar.fi.uba.trackerman.utils.FieldValidator.isValidQuantity;

public class OrderActivity extends AppCompatActivity implements GetOrderTask.OrderReceiver, CancellOrderTask.OrderCanceller, ConfirmOrderTask.OrderConfirmer, RemoveOrderItemTask.OrderItemRemover, UpdateOrderItemTask.OrderItemModifier{

    private long orderId=0;
    private long itemId=0;
    ListView orderItems;
    OrderActivity activity;
    Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderItems= (ListView)findViewById(R.id.order_items_list);
        registerForContextMenu(orderItems);

        Intent intent= getIntent();
        orderId= intent.getLongExtra(Intent.EXTRA_UID, 0);
        if (RestClient.isOnline(this)) new GetOrderTask(this).execute(Long.toString(orderId));
        activity= this;

        this.startCleanUpUI();
        getSupportActionBar().setTitle("Pedido #" + orderId);

        MyPreferences pref = new MyPreferences(this);
        pref.save(getString(R.string.shared_pref_current_order_id), orderId);
        if (isClosedOrder()) {
            findViewById(R.id.activity_order_add_product).setVisibility(View.INVISIBLE);
            findViewById(R.id.activity_order_confirm).setVisibility(View.INVISIBLE);
        }
    }

    private void startCleanUpUI() {
        ((TextView) findViewById(R.id.order_detail_client_name)).setText("");
        ((TextView) findViewById(R.id.order_detail_order_total_price)).setText("");
        ((TextView) findViewById(R.id.order_detail_order_status)).setText("");
        ((TextView) findViewById(R.id.order_detail_order_id)).setText("");
        ((TextView) findViewById(R.id.order_detail_date)).setText("");
        ((TextView) findViewById(R.id.order_detail_time)).setText("");
    }

    private boolean isClosedOrder() {
        MyPreferences pref = new MyPreferences(this);
        String status = pref.get(getString(R.string.shared_pref_current_order_status), "");
        return (!status.isEmpty() && status.equalsIgnoreCase(OrderStatus.CANCELLED.getStatus()) || status.equalsIgnoreCase(OrderStatus.CONFIRMED.getStatus()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (isClosedOrder()) return;

        //Long press order_items
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        OrderItem item = (OrderItem) orderItems.getAdapter().getItem(info.position);
        menu.setHeaderTitle(item.toString());
        this.itemId = item.getId();
        inflater.inflate(R.menu.menu_orders_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.remove_order_item:
                if (RestClient.isOnline(this)) new RemoveOrderItemTask(this).execute(Long.toString(orderId),Long.toString(itemId));
                return true;
            case R.id.modify_item_quantity:
                showQuantityDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void updateOrderInformation(OrderWrapper orderWrapper) {
        this.currentOrder = orderWrapper.getOrder();

        MyPreferences pref = new MyPreferences(this);
        pref.save(getString(R.string.shared_pref_current_client_id), currentOrder.getClientId());

        ListView orderItems= (ListView)findViewById(R.id.order_items_list);
        orderItems.setAdapter(new OrderItemsListAdapter(this, R.layout.order_item_list_item, currentOrder.getOrderItems()));

        Button btn = (Button) findViewById(R.id.activity_order_confirm);
        btn.setText("Total: "+ isContentValid(currentOrder.getCurrency()) +" "+ currentOrder.getTotalPrice() +"\n\n Confirmar Pedido");


        Date fecha = currentOrder.getDateCreated();
        ((TextView) findViewById(R.id.order_detail_client_name)).setText(isContentValid(orderWrapper.getClient().getFullName()));
        ((TextView) findViewById(R.id.order_detail_order_total_price)).setText(isContentValid(currentOrder.getCurrency()) +" "+ isContentValid(Double.toString(currentOrder.getTotalPrice())));
        ((TextView) findViewById(R.id.order_detail_order_status)).setText(isContentValid(currentOrder.getStatusSpanish()));
        ((TextView) findViewById(R.id.order_detail_order_status)).setTextColor(Color.parseColor(currentOrder.getColor(currentOrder.getStatus())));

        ((TextView) findViewById(R.id.order_detail_order_id)).setText("# " + isContentValid(Long.toString(currentOrder.getId())));
        //((TextView) findViewById(R.id.order_detail_date)).setText(android.text.format.DateFormat.format("yyyy-MM-dd", fecha));
        ((TextView) findViewById(R.id.order_detail_date)).setText(DateUtils.formatShortDateArg(fecha));

        ((TextView) findViewById(R.id.order_detail_time)).setText(android.text.format.DateFormat.format("hh:mm", fecha));
    }

    public void afterOrderCancelled(Order order) {
        Toast.makeText(getApplicationContext(), "El pedido se ha cancelado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MyClientsActivity.class);
        startActivity(intent);
    }

    public void afterOrderConfirmed(Order order) {
        Toast.makeText(getApplicationContext(), "Su pedido ha sido confirmado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MyOrdersActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isClosedOrder()) return true;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order, menu);
        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {

        if(item.getItemId()==R.id.action_cancel) {
            ConfirmDialog confirmDialog = new ConfirmDialog(this) {
                @Override
                public void onConfirm() {
                    if (RestClient.isOnline(OrderActivity.this)) new CancellOrderTask(OrderActivity.this).execute(Long.toString(OrderActivity.this.orderId));
                }
            };
            confirmDialog.show();
        }
        if(item.getItemId()==R.id.action_empty) {
            ConfirmDialog confirmDialog = new ConfirmDialog(this) {
                @Override
                public void onConfirm() {
                    if (RestClient.isOnline(OrderActivity.this)) new EmptyOrderTask(OrderActivity.this).execute(Long.toString(OrderActivity.this.orderId));
                }
            };
            confirmDialog.show();
        }


        return false;
    }

    public void confirmOrder(View view){
        if (currentOrder.getOrderItems().size() > 0) {
            if (RestClient.isOnline(this)) new ConfirmOrderTask(this).execute(Long.toString(this.orderId));
        } else {
            showSnackbarSimpleMessage("Pedido vacío, Agregue productos!");
        }
    }
    public void addItem(View view) {
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
    }

    public void showQuantityDialog() {

        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter.LengthFilter filter= new InputFilter.LengthFilter(4);
        edittext.setFilters(new InputFilter[]{filter});

        new AlertDialog.Builder(this)
                .setTitle("Indicar cantidad")
                .setMessage("")
                .setView(edittext)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String editTextValue = edittext.getText().toString();
                        if (isValidQuantity(editTextValue)) {
                            if (RestClient.isOnline(OrderActivity.this)) new UpdateOrderItemTask(OrderActivity.this).execute(Long.toString(orderId),Long.toString(itemId),editTextValue);
                        } else {
                            showSnackbarSimpleMessage("Valor inválido");
                        }
                }})
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public void afterUpdateOrderItem(String result){
        if("OK".equals(result)){
            showSnackbarSimpleMessage("Cantidad modificada.");
            if (RestClient.isOnline(this)) new GetOrderTask(activity).execute(Long.toString(orderId));
        }
    }

    @Override
    public void updateOrderItem(String result) {
        if("OK".equals(result)){
            showSnackbarSimpleMessage("Producto eliminado.");
            if (RestClient.isOnline(this)) new GetOrderTask(activity).execute(Long.toString(orderId));
        }
    }

    public void showSnackbarSimpleMessage(String msg){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinator_layout_order_view);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
};

