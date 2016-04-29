package ar.fi.uba.trackerman.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import ar.fi.uba.trackerman.tasks.order.CancellOrderTask;
import ar.fi.uba.trackerman.tasks.order.ConfirmOrderTask;
import ar.fi.uba.trackerman.tasks.order.EmptyOrderTask;
import ar.fi.uba.trackerman.tasks.order.GetOrderTask;
import ar.fi.uba.trackerman.tasks.order.RemoveOrderItemTask;
import ar.fi.uba.trackerman.tasks.order.UpdateOrderItemTask;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;
import static ar.fi.uba.trackerman.utils.FieldValidator.isValidQuantity;

import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.OrderStatus;
import fi.uba.ar.soldme.R;

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
        GetOrderTask task = new GetOrderTask(this);
        task.execute(Long.toString(orderId));
        activity= this;

        this.startCleanUpUI();
        getSupportActionBar().setTitle("Pedido #"+ orderId);

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
                RemoveOrderItemTask task= new RemoveOrderItemTask(this);
                task.execute(Long.toString(orderId),Long.toString(itemId));
                return true;
            case R.id.modify_item_quantity:
                showQuantityDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void updateOrderInformation(Order order) {
        this.currentOrder = order;

        MyPreferences pref = new MyPreferences(this);
        pref.save(getString(R.string.shared_pref_current_client_id), currentOrder.getClientId());

        ListView orderItems= (ListView)findViewById(R.id.order_items_list);
        orderItems.setAdapter(new OrderItemsListAdapter(this, R.layout.order_item_list_item, order.getOrderItems()));

        Button btn = (Button) findViewById(R.id.activity_order_confirm);
        btn.setText("Total: "+ order.getCurrency() +" "+ order.getTotalPrice() +"\n\n Confirmar Pedido");


        Date fecha = order.getDateCreated();
        ((TextView) findViewById(R.id.order_detail_client_name)).setText(""); //TODO: completar aca con el nombre del cliente
        ((TextView) findViewById(R.id.order_detail_order_total_price)).setText(isContentValid(order.getCurrency() +" "+ Double.toString(order.getTotalPrice())));
        ((TextView) findViewById(R.id.order_detail_order_status)).setText(isContentValid(order.getStatusSpanish()));
        ((TextView) findViewById(R.id.order_detail_order_status)).setTextColor(Color.parseColor(order.getColor(order.getStatus())));

        ((TextView) findViewById(R.id.order_detail_order_id)).setText("# "+ isContentValid(Long.toString(order.getId())));
        ((TextView) findViewById(R.id.order_detail_date)).setText(android.text.format.DateFormat.format("yyyy-MM-dd", fecha));
        ((TextView) findViewById(R.id.order_detail_time)).setText(android.text.format.DateFormat.format("hh:mm", fecha));
    }

    public void afterOrderCancelled(Order order) {
        Intent intent = new Intent(this, MyClientsActivity.class);
        startActivity(intent);
    }

    public void afterOrderConfirmed(Order order) {
        Toast.makeText(getApplicationContext(), "Su pedido ha sido confirmado", Toast.LENGTH_LONG).show();
        // showSnackbarSimpleMessage("Su pedido ha sido confirmado");
        try {Thread.sleep(2500);} catch (Exception e) { }
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_cancel) {
            CancellOrderTask task= new CancellOrderTask(this);
            task.execute(Long.toString(this.orderId));
        }
        if(item.getItemId()==R.id.action_empty) {
            EmptyOrderTask task= new EmptyOrderTask(this);
            task.execute(Long.toString(this.orderId));
        }
        return false;
    }

    public void confirmOrder(View view){
        if (currentOrder.getOrderItems().size() > 0) {
            ConfirmOrderTask task = new ConfirmOrderTask(this);
            task.execute(Long.toString(this.orderId));
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
                            UpdateOrderItemTask task = new UpdateOrderItemTask(OrderActivity.this);
                            task.execute(Long.toString(orderId),Long.toString(itemId),editTextValue);
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
        if("FAIL".equals(result)){
            showSnackbarSimpleMessage("Fallo al modificar la cantidad.");
        }else{
            showSnackbarSimpleMessage("Cantidad modificada.");
            GetOrderTask task= new GetOrderTask(activity);
            task.execute(Long.toString(orderId));
        }
    }

    @Override
    public void updateOrderItem(String result) {
        if("FAIL".equals(result)){
            showSnackbarSimpleMessage("Fallo al eliminar el producto.");
        }else {
            showSnackbarSimpleMessage("Producto eliminado.");
            GetOrderTask task = new GetOrderTask(activity);
            task.execute(Long.toString(orderId));
        }
    }

    public void showSnackbarSimpleMessage(String msg){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinator_layout_order_view);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
};

