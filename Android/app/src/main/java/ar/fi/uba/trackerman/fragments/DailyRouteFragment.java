package ar.fi.uba.trackerman.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import ar.fi.uba.trackerman.adapters.SchedulesListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.Visit;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.visit.PostVisitTask;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.MyPreferences;
import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 1/5/16.
 */
public class DailyRouteFragment extends Fragment implements PostVisitTask.VisitCreator{

    public static final String ITEM_POSITION = "DailyRouteFragment.ITEM_POSITION";
    public static String DAY_ARG = "DailyRouteFragment.DAY_ARG";
    private String day;
    ListView clientsList;

    public DailyRouteFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        day = args.getString(DAY_ARG);
        Integer item = args.getInt(ITEM_POSITION);

        MyPreferences pref = new MyPreferences(this.getContext());
        String currentDate = pref.get(getString(R.string.shared_pref_current_schedule_date), "");
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseShortDate(currentDate));

        //cal.get(Calendar.DAY_OF_WEEK)-2;



        View fragmentView= inflater.inflate(R.layout.fragment_daily_route, container, false);
        clientsList= (ListView)fragmentView.findViewById(R.id.dayAgendaListView);
        ///clientsList.setOnItemClickListener(this);
        registerForContextMenu(clientsList);


        TextView text= (TextView)fragmentView.findViewById(R.id.daily_route_day);
        text.setText(day+" - position="+item);

        ListView schedulesList= (ListView)fragmentView.findViewById(R.id.dayAgendaListView);

        SchedulesListAdapter schedulesListAdapter = new SchedulesListAdapter( getContext(), R.layout.agenda_list_item, new ArrayList<Client>());
        schedulesList.setAdapter(schedulesListAdapter);
        //schedulesList.setOnItemClickListener(this);


        ProgressBar bar= new ProgressBar(getContext());
        bar.setIndeterminate(true);
        schedulesList.setEmptyView(bar);

        return fragmentView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)menuInfo;

        Client client= (Client) clientsList.getAdapter().getItem(info.position);
        menu.setHeaderTitle(client.getFullName());

        inflater.inflate(R.menu.menu_day_agenda_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mark_client_as_visited:
                // FIXME: Aqui se deberia marcar como visitado
                Toast.makeText(this.getActivity().getApplicationContext(), "Marcar como vistado !!!", Toast.LENGTH_LONG).show();

                //if (RestClient.isOnline(this.getContext())) new PostVisitTask(this).execute(scheduleEntryId,DateUtils.formatDate(Calendar.getInstance().getTime()),comment);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void afterCreatingVisit(Visit visit) {
        //TODO glaghi luego de crear la visita refrescar la vista
    }
}
