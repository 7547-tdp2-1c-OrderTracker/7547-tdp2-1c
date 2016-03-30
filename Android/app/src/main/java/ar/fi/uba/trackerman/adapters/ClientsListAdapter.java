package ar.fi.uba.trackerman.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ar.fi.uba.trackerman.domains.Client;
import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 30/3/16.
 */
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
            holder.lastName = (TextView) convertView.findViewById(R.id.client_row_lastname);
            holder.image = (ImageView) convertView.findViewById(R.id.client_row_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(client.getName());
        holder.lastName.setText(client.getLastName());
        return convertView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView lastName;
        public ImageView image;
    }
}
