package ar.fi.uba.trackerman.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.ScheduleDay;
import ar.fi.uba.trackerman.fragments.DailyRouteFragment;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.schedule.GetScheduleDayListTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.LocationHelper;
import ar.fi.uba.trackerman.utils.MyPreferences;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;
import static ar.fi.uba.trackerman.utils.FieldValidator.showCoolDistance;

public class SchedulesListAdapter extends ArrayAdapter<Client> {

    private ScheduleDay currentScheduleDay;
    private DailyRouteFragment fragment;

    public SchedulesListAdapter(Context context, int resource,
                                List<Client> clients, DailyRouteFragment fragment) {
        super(context, resource, clients);
        LocationHelper.updatePosition(getContext());
        this.fragment= fragment;
        solveTask(null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Client client = this.getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.agenda_list_item, null);

            holder = new ViewHolder();
            holder.client_id = (TextView) convertView.findViewById(R.id.agenda_row_client_id);
            holder.clientName = (TextView) convertView.findViewById(R.id.agenda_row_client_name);
            holder.clientAddress = (TextView) convertView.findViewById(R.id.agenda_row_client_address);
            holder.distance = (TextView) convertView.findViewById(R.id.agenda_row_client_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.client_id.setText("# "+ isContentValid(Long.toString(client.getId())));
        holder.clientName.setText(isContentValid(client.getFullName()));
        holder.clientAddress.setText(isContentValid(client.getAddress()));
        holder.distance.setText(showCoolDistance(client.getDistance()));

        return convertView;
    }

    public void setScheduleDay(ScheduleDay day) {
        this.currentScheduleDay = day;

        if(day.getClients().size()==0){
            fragment.showEmptyList();
        }else {
            fragment.showClientList();
            this.clear();
            this.addAll(day.getClients());
            this.notifyDataSetChanged();
        }
    }

    public ScheduleDay getCurrentScheduleDay() {
        return currentScheduleDay;
    }

    public void solveTask(String date) {
        MyPreferences pref = new MyPreferences(getContext());
        String lat = pref.get(getContext().getString(R.string.shared_pref_current_location_lat), "");
        String lon = pref.get(getContext().getString(R.string.shared_pref_current_location_lon), "");
        String currentDate = pref.get(getContext().getString(R.string.shared_pref_current_schedule_date), "");
        String dateToSolve = (date==null)?currentDate:date;

        if (RestClient.isOnline(getContext())) new GetScheduleDayListTask(this).execute(dateToSolve, Long.toString(AppSettings.getSellerId()), lat, lon);
    }

    private static class ViewHolder {
        public TextView client_id;
        public TextView clientName;
        public TextView clientAddress;
        public TextView distance;
    }

}
