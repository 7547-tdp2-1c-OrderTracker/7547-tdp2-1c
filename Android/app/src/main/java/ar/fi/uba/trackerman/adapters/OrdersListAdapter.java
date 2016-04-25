package ar.fi.uba.trackerman.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrdersSearchResult;
import ar.fi.uba.trackerman.tasks.order.GetOrdersListTask;
import ar.fi.uba.trackerman.utils.DateUtils;
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

            holder.orderId = (TextView) convertView.findViewById(R.id.order_row_order_id);
            holder.clientName = (TextView) convertView.findViewById(R.id.order_row_client_name);
            holder.status = (TextView) convertView.findViewById(R.id.order_row_status);
            holder.orderDate = (TextView) convertView.findViewById(R.id.order_row_date);
            holder.orderTime = (TextView) convertView.findViewById(R.id.order_row_time);
            holder.orderTotalPrice = (TextView) convertView.findViewById(R.id.order_row_order_total_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Date fecha = order.getDateCreated();

        holder.clientName.setText("Apellido, Nombre");
        holder.orderTotalPrice.setText(Double.toString(order.getTotalPrice()));
        holder.status.setText(isContentValid(order.getStatusSpanish()));
        holder.status.setTextColor(Color.parseColor(order.getColor(order.getStatus())));

        holder.orderId.setText("# "+ isContentValid(Long.toString(order.getId())));
        holder.orderDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd", fecha));
        holder.orderTime.setText(android.text.format.DateFormat.format("hh:mm", fecha));

        return convertView;
    }

    private static class ViewHolder {
        public TextView clientName;
        public TextView orderTotalPrice;
        public TextView status;
        public TextView orderId;
        public TextView orderDate;
        public TextView orderTime;

    }
}
