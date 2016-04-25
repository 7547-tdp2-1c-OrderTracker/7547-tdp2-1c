package ar.fi.uba.trackerman.activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ar.fi.uba.trackerman.adapters.OrderItemsListAdapter;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.tasks.order.CancellOrderTask;
import ar.fi.uba.trackerman.tasks.order.ConfirmOrderTask;
import ar.fi.uba.trackerman.tasks.order.EmptyOrderTask;
import ar.fi.uba.trackerman.tasks.order.GetOrderTask;
import ar.fi.uba.trackerman.tasks.order.RemoveOrderItemTask;
import ar.fi.uba.trackerman.tasks.order.UpdateOrderItemTask;
import fi.uba.ar.soldme.R;

public class OrderActivity extends AppCompatActivity implements  GetOrderTask.OrderReciver, CancellOrderTask.OrderCanceller, ConfirmOrderTask.OrderConfirmer, EmptyOrderTask.OrderCleaner, RemoveOrderItemTask.OrderItemRemover, UpdateOrderItemTask.OrderItemModifier{

    private long orderId=0;
    private long itemId=0;
    ListView orderItems;
    OrderActivity activity;
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
        GetOrderTask task= new GetOrderTask(this);
        task.execute(Long.toString(orderId));
        activity= this;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo)menuInfo;

            OrderItem item= (OrderItem)orderItems.getAdapter().getItem(info.position);
            menu.setHeaderTitle(item.toString());
            this.itemId=item.getId();

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
        ListView orderItems= (ListView)findViewById(R.id.order_items_list);
        orderItems.setAdapter(new OrderItemsListAdapter(this, R.layout.order_item_list_item, order.getOrderItems()));
        TextView total= (TextView)findViewById(R.id.order_total);
        total.setText("Total: " + order.getTotalPrice() + " $");
        TextView cliente= (TextView)findViewById(R.id.order_client);
        cliente.setText("Cliente: #" + order.getClientId());
    }

    public void afterOrderCancelled(Order order) {
        Intent intent = new Intent(this, MyClientsActivity.class);
        startActivity(intent);
    }

    public void afterOrderConfirmed(Order order) {
        showSnackbarSimpleMessage("Su pedido ha sido confirmado");
        try {Thread.sleep(5000);} catch (Exception e) {}
        Intent intent = new Intent(this, MyClientsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        ConfirmOrderTask task= new ConfirmOrderTask(this);
        task.execute(Long.toString(this.orderId));
    }

    public boolean isValidQuantity(String txt) {
        if (txt.isEmpty()) return false;
        if (Integer.parseInt(txt.toString()) == 0) return false;
        return true;
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

