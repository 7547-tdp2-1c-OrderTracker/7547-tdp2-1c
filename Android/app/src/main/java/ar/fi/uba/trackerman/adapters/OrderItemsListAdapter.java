package ar.fi.uba.trackerman.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.ClientSearchResult;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.tasks.GetClientListTask;
import ar.fi.uba.trackerman.utils.CircleTransform;
import fi.uba.ar.soldme.R;

public class OrderItemsListAdapter extends ArrayAdapter<OrderItem> {

    public OrderItemsListAdapter(Context context, int resource,
                                 List<OrderItem> orderItems) {
        super(context, resource, orderItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderItem orderItem = this.getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.order_item_list_item, null);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_row_picture);
            holder.name = (TextView) convertView.findViewById(R.id.item_row_name);
            holder.brand = (TextView) convertView.findViewById(R.id.item_row_brand);
            holder.quantity = (TextView) convertView.findViewById(R.id.item_row_quantity);
            holder.price = (TextView) convertView.findViewById(R.id.item_row_price);
            holder.total = (TextView) convertView.findViewById(R.id.item_row_total);
            holder.itemCurrency = (TextView) convertView.findViewById(R.id.item_row_currency);
            holder.totalCurrency = (TextView) convertView.findViewById(R.id.item_row_total_currency);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(this.getContext()).load(orderItem.getThumbnail()).into(holder.image);
        holder.name.setText(orderItem.getName());
        holder.brand.setText(orderItem.getBrandName());
        holder.quantity.setText(Integer.toString(orderItem.getQuantity()));
        holder.price.setText(Double.toString(orderItem.getUnitPrice()));

        //holder.total.setText(Double.toString(order.getTotalPrice()));

        holder.itemCurrency.setText(orderItem.getCurrency());

        return convertView;
    }

    private static class ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView brand;
        public TextView quantity;
        public TextView price;
        public TextView total;
        public TextView itemCurrency;
        public TextView totalCurrency;

    }
}
