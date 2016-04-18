package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import ar.fi.uba.trackerman.adapters.OrderItemsListAdapter;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.tasks.CancellOrderTask;
import ar.fi.uba.trackerman.tasks.ConfirmOrderTask;
import ar.fi.uba.trackerman.tasks.GetOrderTask;
import fi.uba.ar.soldme.R;

public class OrderActivity extends AppCompatActivity implements  GetOrderTask.OrderReciver, CancellOrderTask.OrderCanceller, ConfirmOrderTask.OrderConfirmer{

    private long orderId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent= getIntent();
        orderId= intent.getLongExtra(Intent.EXTRA_UID, 0);
        GetOrderTask task= new GetOrderTask(this);
        task.execute(Long.toString(orderId));
    }

    @Override
    public void updateOrderInformation(Order order) {
        ListView orderItems= (ListView)findViewById(R.id.order_items_list);
        orderItems.setAdapter(new OrderItemsListAdapter(this,R.layout.order_item_list_item,order.getOrderItems()));
        TextView total= (TextView)findViewById(R.id.order_total);
        total.setText("Total: " + order.getTotalPrice() + " $");
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
            CancellOrderTask task= new CancellOrderTask(this);
            task.execute(Long.toString(this.orderId));
        }
        return false;
    }

    public void confirmOrder(View view){
        ConfirmOrderTask task= new ConfirmOrderTask(this);
        task.execute(Long.toString(this.orderId));
    }
}
