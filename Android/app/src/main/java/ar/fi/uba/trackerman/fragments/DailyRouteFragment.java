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

import ar.fi.uba.trackerman.adapters.SchedulesListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 1/5/16.
 */
public class DailyRouteFragment extends Fragment{

    public static String DAY_ARG="DailyRouteFragment.DAY_ARG";
    private String day;
    ListView clientsList;

    public DailyRouteFragment(){
        super();
    }

    public void setDay(String day){
        this.day=day;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        day = args.getString(DAY_ARG);
        View fragmentView= inflater.inflate(R.layout.fragment_daily_route, container, false);
        clientsList= (ListView)fragmentView.findViewById(R.id.dayAgendaListView);
        ///clientsList.setOnItemClickListener(this);
        registerForContextMenu(clientsList);


        TextView text= (TextView)fragmentView.findViewById(R.id.daily_route_day);
        text.setText(day);

        ListView schedulesList= (ListView)fragmentView.findViewById(R.id.dayAgendaListView);

        SchedulesListAdapter schedulesListAdapter = new SchedulesListAdapter( getContext(), R.layout.agenda_list_item, new ArrayList<Client>());
        schedulesList.setAdapter(schedulesListAdapter);
        //schedulesList.setOnItemClickListener(this);


        ProgressBar bar= new ProgressBar(getContext());
        bar.setIndeterminate(true);
        schedulesList.setEmptyView(bar);
        //schedulesListAdapter.refresh();

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
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
