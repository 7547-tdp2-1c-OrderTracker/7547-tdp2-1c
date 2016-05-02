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
import ar.fi.uba.trackerman.domains.ScheduleDay;
import ar.fi.uba.trackerman.domains.Visit;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.visit.PostVisitTask;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.DayOfWeek;
import ar.fi.uba.trackerman.utils.MyPreferences;
import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 1/5/16.
 */
public class DailyRouteFragment extends Fragment implements PostVisitTask.VisitCreator{

    public static final String ITEM_POSITION = "DailyRouteFragment.ITEM_POSITION";
    public static final String DIFF = "DailyRouteFragment.DIFF";
    ListView clientsList;
    private SchedulesListAdapter schedulesListAdapter;
    private View emptyView;

    public DailyRouteFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        Integer item = args.getInt(ITEM_POSITION);
        Integer diff = args.getInt(DIFF);

        View fragmentView= inflater.inflate(R.layout.fragment_daily_route, container, false);
        clientsList= (ListView)fragmentView.findViewById(R.id.dayAgendaListView);
        ///clientsList.setOnItemClickListener(this);
        registerForContextMenu(clientsList);


        MyPreferences pref = new MyPreferences(this.getContext());
        String currentDate = pref.get(this.getContext().getString(R.string.shared_pref_current_schedule_date), "");
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseShortDate(currentDate));
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + diff);
        //pref.save(context.getString(R.string.shared_pref_current_schedule_date), DateUtils.formatShortDate(cal.getTime()));

        String extra = String.valueOf(diff) + " - " + DateUtils.formatShortDate(cal.getTime());

        TextView text= (TextView)fragmentView.findViewById(R.id.daily_route_day);
        text.setText(DayOfWeek.byReference(cal.get(Calendar.DAY_OF_WEEK) - 1).toEsp() +" - position="+item+" - diff"+extra);

        ListView schedulesList= (ListView)fragmentView.findViewById(R.id.dayAgendaListView);

        schedulesListAdapter = new SchedulesListAdapter( getContext(), R.layout.agenda_list_item, new ArrayList<Client>(),this);
        schedulesList.setAdapter(schedulesListAdapter);
        schedulesListAdapter.solveTask(DateUtils.formatShortDate(cal.getTime()));
        //schedulesList.setOnItemClickListener(this);
        this.emptyView= fragmentView.findViewById(R.id.agenda_row_clients_empty);

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

                ScheduleDay day = schedulesListAdapter.getCurrentScheduleDay();
                Calendar cal = Calendar.getInstance();
                cal.setTime(day.getDate());
                String dayOfWeek = String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);

                //FIXME guido tenes que obtener el comentario y cambiar esta linea.
                String comment = "Te visite";
                //FIXME guido obtener el client y poner el clientId
                String clientId = "1";

                if (RestClient.isOnline(this.getContext())) new PostVisitTask(this).execute(clientId,dayOfWeek,DateUtils.formatDate(Calendar.getInstance().getTime()),comment);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void afterCreatingVisit(Visit visit) {
        //TODO glaghi luego de crear la visita refrescar la vista
    }

    public void showEmptyList(){
        emptyView.setVisibility(View.VISIBLE);
    }

    public void showClientList(){
        emptyView.setVisibility(View.GONE);
    }

}
