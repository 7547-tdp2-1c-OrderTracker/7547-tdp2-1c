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

import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.domains.ProductsSearchResult;
import ar.fi.uba.trackerman.domains.ScheduleDay;
import ar.fi.uba.trackerman.domains.ScheduleEntry;
import ar.fi.uba.trackerman.domains.SchedulesSearchResult;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.product.SearchProductsListTask;
import ar.fi.uba.trackerman.tasks.schedule.GetScheduleDayListTask;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

public class SchedulesListAdapter extends ArrayAdapter<ScheduleEntry> implements GetScheduleDayListTask.Sheduleable {

    public SchedulesListAdapter(Context context, int resource,
                                List<ScheduleEntry> schedules) {
        super(context, resource, schedules);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScheduleEntry scheduleEntry = this.getItem(position);
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

        holder.clientName.setText(isContentValid(Long.toString(scheduleEntry.getClientId())));

        return convertView;
    }

    @Override
    public void setScheduleDay(ScheduleDay day) {

    }

    private static class ViewHolder {
        public TextView client_id;
        public TextView clientName;
        public TextView clientAddress;
        public TextView distance;
    }

}
