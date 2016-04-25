package ar.fi.uba.trackerman.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrdersSearchResult;
import ar.fi.uba.trackerman.tasks.order.GetOrdersListTask;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

public class OrdersListAdapter extends ArrayAdapter<Order> {

    private long total;
    private long offset;
    private boolean fetching;

    public OrdersListAdapter(Context context, int resource,
                             List<Order> orders) {
        super(context, resource, orders);
        total=1;
        offset=0;
        fetching=false;
    }

    public void refresh(){
        this.clear();
        offset=0;
        total=1;
        fetchMore();
    }

    public void fetchMore(){
        if(offset<total && !fetching){
            fetching=true;
            GetOrdersListTask asyncTask= new GetOrdersListTask(this);
            asyncTask.execute(offset);
        }
    }

    public void addOrders(OrdersSearchResult ordersSearchResult){
        if(ordersSearchResult!=null) {
            this.addAll(ordersSearchResult.getOrders());
            this.offset = this.getCount();
            this.total = ordersSearchResult.getTotal();
            fetching = false;
        }else{
            Log.w(this.getClass().getCanonicalName(), "Something when wrong getting clients.");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = this.getItem(position);
        ViewHolder holder;
        if(position==this.getCount()-1){
            // fetchMore();
        }

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_order_item, null);

            holder = new ViewHolder();
            holder.clientName = (TextView) convertView.findViewById(R.id.order_row_client_name);
            holder.orderTotalPrice = (TextView) convertView.findViewById(R.id.order_row_order_total_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.clientName.setText("# "+ isContentValid(Long.toString(order.getId())));
        holder.orderTotalPrice.setText(isContentValid(order.getStatus()));

        return convertView;
    }

    private static class ViewHolder {
        public TextView clientName;
        public TextView orderTotalPrice;
    }
}
