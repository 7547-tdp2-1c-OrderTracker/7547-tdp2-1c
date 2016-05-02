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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.domains.ProductsSearchResult;
import ar.fi.uba.trackerman.domains.ScheduleDay;
import ar.fi.uba.trackerman.domains.ScheduleEntry;
import ar.fi.uba.trackerman.domains.SchedulesSearchResult;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.product.SearchProductsListTask;
import ar.fi.uba.trackerman.tasks.schedule.GetScheduleDayListTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

public class SchedulesListAdapter extends ArrayAdapter<Client> implements GetScheduleDayListTask.Sheduleable {

    public SchedulesListAdapter(Context context, int resource,
                                List<Client> clients) {
        super(context, resource, clients);
        GetScheduleDayListTask scheduleListTask = new GetScheduleDayListTask(this);

        Calendar today = Calendar.getInstance();
        // String todayStr = today.get(Calendar.YEAR) +"-"+today.get(Calendar.MONTH) +"-"+today.get(Calendar.DATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(today.getTime());
        scheduleListTask.execute(todayStr,Long.toString(AppSettings.getSellerId()));
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
            holder.clientName = (TextView) convertView.findViewById(R.id.agenda_row_client_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.clientName.setText(isContentValid(Long.toString(client.getId())));

        return convertView;
    }

    @Override
    public void setScheduleDay(ScheduleDay day) {
        this.addAll(day.getClients());
    }

    private static class ViewHolder {
        public TextView client_id;
        public TextView clientName;
        public TextView clientAddress;
        public TextView distance;
    }

}
