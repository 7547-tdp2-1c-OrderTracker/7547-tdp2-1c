package ar.fi.uba.trackerman.adapters;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
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
import ar.fi.uba.trackerman.server.LocationService;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.client.GetClientListTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.CircleTransform;
import ar.fi.uba.trackerman.utils.LocationHelper;
import ar.fi.uba.trackerman.utils.MyPreferences;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;
import static ar.fi.uba.trackerman.utils.FieldValidator.showCoolDistance;

public class ClientsListAdapter extends ArrayAdapter<Client> implements GetClientListTask.ClientsListAggregator {

    private boolean firstRefresed;
    private Location loc;
    private long total;
    private long offset;
    private boolean fetching;
    private Activity activity;
    private MyPreferences pref = new MyPreferences(getContext());

    public ClientsListAdapter(Activity activity, Context context, int resource,
                           List<Client> clients) {
        super(context, resource, clients);
        this.activity = activity;
        total=1;
        offset=0;
        fetching=false;
        firstRefresed = false;
        LocationHelper.updatePosition(this.getContext());
    }

    public Activity getActivity() {
        return activity;
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
            solveTask();
            LocationService ls = new LocationService(this.getContext());
            ls.config(new LocationService.MyLocation() {
                @Override
                public void processLocation(Location loc) {
                    ClientsListAdapter.this.loc = loc;
                    if (!ClientsListAdapter.this.firstRefresed) {
                        //solveTask();
                        ClientsListAdapter.this.firstRefresed = true;
                    }
                }
            });

        }
    }

    private void solveTask() {
        if (RestClient.isOnline(getContext())) {
            GetClientListTask listClients = new GetClientListTask(ClientsListAdapter.this);
            if (loc != null) {
                listClients.execute(String.valueOf(offset), String.valueOf(loc.getLatitude()), String.valueOf(loc.getLongitude()));
            } else {
                String lat = pref.get(getContext().getString(R.string.shared_pref_current_location_lat), AppSettings.getGpsLat());
                String lon = pref.get(getContext().getString(R.string.shared_pref_current_location_lon), AppSettings.getGpsLon());
                if (lat.isEmpty() && lon.isEmpty()) listClients.execute(String.valueOf(offset));
                else listClients.execute(String.valueOf(offset), lat, lon);
            }
        }
    }

    @Override
    public void addClients(ClientSearchResult clientSearchResult) {
        if(clientSearchResult!=null) {
            //this.clear();
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
            holder.client_id = (TextView) convertView.findViewById(R.id.client_row_client_id);
            holder.name = (TextView) convertView.findViewById(R.id.client_row_name);
            holder.company = (TextView) convertView.findViewById(R.id.client_row_company);
            holder.distance = (TextView) convertView.findViewById(R.id.client_row_client_distance);
            holder.address = (TextView) convertView.findViewById(R.id.client_row_address);
            holder.image = (ImageView) convertView.findViewById(R.id.client_row_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.client_id.setText("# "+ isContentValid(Long.toString(client.getId())));
        holder.name.setText(isContentValid(client.getLastName())+", "+isContentValid(client.getName()));
        holder.company.setText(isContentValid(client.getCompany()));
        holder.distance.setText(showCoolDistance(getContext(),client.getDistance()));
        holder.address.setText(isContentValid(client.getAddress()));
        if (isContentValid(client.getThumbnail()).isEmpty()) {
            Picasso.with(this.getContext()).load(R.drawable.logo).transform(new CircleTransform()).into(holder.image);
        } else {
            Picasso.with(this.getContext()).load(client.getThumbnail()).transform(new CircleTransform()).into(holder.image);
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView address;
        public TextView company;
        public TextView client_id;
        public TextView distance;
        public ImageView image;
    }
}
