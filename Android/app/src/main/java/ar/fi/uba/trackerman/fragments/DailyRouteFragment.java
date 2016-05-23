package ar.fi.uba.trackerman.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import ar.fi.uba.trackerman.activities.RouteActivity;
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
    private View routeIcon;
    private MyPreferences pref;
    private static Client selectedClient;

    public DailyRouteFragment(){
        super();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        Integer item = args.getInt(ITEM_POSITION);
        Integer diff = args.getInt(DIFF);
        pref = new MyPreferences(this.getContext());

        View fragmentView= inflater.inflate(R.layout.fragment_daily_route, container, false);
        clientsList= (ListView)fragmentView.findViewById(R.id.dayAgendaListView);
        ///clientsList.setOnItemClickListener(this);
        registerForContextMenu(clientsList);

        String currentDate = pref.get(this.getContext().getString(R.string.shared_pref_current_schedule_date), "");
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseShortDate(currentDate));
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + diff);
        //pref.save(context.getString(R.string.shared_pref_current_schedule_date), DateUtils.formatShortDate(cal.getTime()));
        String queriedDate = DateUtils.formatShortDate(cal.getTime());


        TextView text= (TextView)fragmentView.findViewById(R.id.daily_route_day);
        //String extra = String.valueOf(diff) + " - " + queriedDate;
        //text.setText(DayOfWeek.byReference(cal.get(Calendar.DAY_OF_WEEK) - 1).toEsp() +" - position="+item+" - diff"+extra);
        text.setText(DayOfWeek.byReference(cal.get(Calendar.DAY_OF_WEEK) - 1).toEsp() + ": " + DateUtils.formatShortDateArg(cal.getTime()));


        final ListView schedulesList= (ListView)fragmentView.findViewById(R.id.dayAgendaListView);

        schedulesListAdapter = new SchedulesListAdapter( getContext(), R.layout.agenda_list_item, new ArrayList<Client>(),this);
        schedulesList.setAdapter(schedulesListAdapter);
        Log.d("daily_route","Date queried = " + queriedDate);
        schedulesListAdapter.solveTask(queriedDate);
        //schedulesList.setOnItemClickListener(this);
        this.emptyView= fragmentView.findViewById(R.id.agenda_row_clients_empty);
        this.routeIcon= fragmentView.findViewById(R.id.viewRoute);
        this.routeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RouteActivity.class);
                MyPreferences pref = new MyPreferences(getContext());
                double[] points= new double[2+schedulesListAdapter.getCount()*2];
                String[] clients= new String[schedulesListAdapter.getCount()];
                points[0]=pref.get("lat",-34.563424d);
                points[1]=pref.get("lon",-58.463874d);
                for(int i=0;i<schedulesListAdapter.getCount();i++){
                    points[2+2*i]=schedulesListAdapter.getItem(i).getLat();
                    points[3+2*i]=schedulesListAdapter.getItem(i).getLon();
                    clients[i]=schedulesListAdapter.getItem(i).getFullName();
                }
                intent.putExtra("LOCATIONS",points);
                intent.putExtra("CLIENTES",clients);
                startActivity(intent);
            }
        });

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
        this.selectedClient = client;

        inflater.inflate(R.menu.menu_day_agenda_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mark_client_as_visited:

                ScheduleDay day = schedulesListAdapter.getCurrentScheduleDay();
                Calendar cal = Calendar.getInstance();
                cal.setTime(day.getDate());

                final String dayOfWeek = String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);
                final EditText edittext = new EditText(getContext());
                final Client clientObj = this.selectedClient;

                new AlertDialog.Builder(this.getContext())
                        .setTitle("Agregar comentario")
                        .setMessage("")
                        .setView(edittext)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String comment = edittext.getText().toString();
                                String clientId = Long.toString(selectedClient.getId());

                                //if (RestClient.isOnline(this.getContext())) new PostVisitTask(this).execute(clientId,dayOfWeek,DateUtils.formatDate(Calendar.getInstance().getTime()),comment);
                                if (RestClient.isOnline(DailyRouteFragment.this.getContext())) {
                                    new PostVisitTask(DailyRouteFragment.this).execute(clientId,dayOfWeek,DateUtils.formatDate(Calendar.getInstance().getTime()),comment);
                                    Toast.makeText(DailyRouteFragment.this.getActivity().getApplicationContext(),clientObj.getFullName() +" marcado como visitado", Toast.LENGTH_LONG).show();
                                }

                            }})
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void afterCreatingVisit(Visit visit) {
        // refresh del fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void showEmptyList(){
        emptyView.setVisibility(View.VISIBLE);
        routeIcon.setVisibility(View.GONE);
    }

    public void showClientList(){
        routeIcon.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
}
