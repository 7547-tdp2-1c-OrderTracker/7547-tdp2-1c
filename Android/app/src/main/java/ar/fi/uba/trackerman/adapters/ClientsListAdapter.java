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

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.ClientSearchResult;
import ar.fi.uba.trackerman.tasks.client.GetClientListTask;
import ar.fi.uba.trackerman.utils.CircleTransform;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

public class ClientsListAdapter extends ArrayAdapter<Client> {

    private long total;
    private long offset;
    private boolean fetching;

    public ClientsListAdapter(Context context, int resource,
                           List<Client> clients) {
        super(context, resource, clients);
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
            GetClientListTask asyncTask= new GetClientListTask(this);
            asyncTask.execute(offset);
        }
    }

    public void addClients(ClientSearchResult clientSearchResult){
        if(clientSearchResult!=null) {
            this.addAll(clientSearchResult.getClients());
            this.offset = this.getCount();
            this.total = clientSearchResult.getTotal();
            fetching = false;
        }else{
            Log.w(this.getClass().getCanonicalName(), "Something when wrong getting clients.");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Client client = this.getItem(position);
        ViewHolder holder;
        if(position==this.getCount()-1){
            fetchMore();
        }

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

        holder.name.setText(isContentValid(client.getLastName())+", "+isContentValid(client.getName()));
        holder.address.setText(isContentValid(client.getAddress()));
        Picasso.with(this.getContext()).load(client.getThumbnail()).transform(new CircleTransform()).into(holder.image);

        return convertView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView address;
        public ImageView image;
    }
}
