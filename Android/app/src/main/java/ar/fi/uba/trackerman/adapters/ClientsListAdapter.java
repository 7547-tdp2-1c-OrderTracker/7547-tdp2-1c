package ar.fi.uba.trackerman.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ar.fi.uba.trackerman.domains.Client;
import fi.uba.ar.soldme.R;

public class ClientsListAdapter extends ArrayAdapter<Client> {

    public ClientsListAdapter(Context context, int resource,
                           List<Client> clients) {
        super(context, resource, clients);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Client client = this.getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_client_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.client_row_name);
            holder.address = (TextView) convertView.findViewById(R.id.client_row_address);
            holder.image = (ImageView) convertView.findViewById(R.id.client_row_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(client.getLastName()+", "+client.getName());
        // TODO: add AddressLine attribute
        holder.address.setText(client.getEmail());
        Picasso.with(this.getContext()).load(client.getThumbnail()).into(holder.image);

        return convertView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView address;
        public ImageView image;
    }
}
