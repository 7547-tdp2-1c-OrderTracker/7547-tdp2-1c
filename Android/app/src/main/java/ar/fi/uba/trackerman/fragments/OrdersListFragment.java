package ar.fi.uba.trackerman.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.adapters.OrdersListAdapter;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.utils.MyPreferences;
import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 29/3/16.
 */
public class OrdersListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public OrdersListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView= inflater.inflate(R.layout.fragment_orders_list, container, false);
        ListView ordersList= (ListView)fragmentView.findViewById(R.id.orderListView);

        OrdersListAdapter ordersAdapter = new OrdersListAdapter( getContext(), R.layout.list_order_item, new ArrayList<Order>());
        ordersList.setAdapter(ordersAdapter);
        ordersList.setOnItemClickListener(this);

        ProgressBar bar= new ProgressBar(getContext());
        bar.setIndeterminate(true);
        ordersList.setEmptyView(bar);
        ordersAdapter.refresh();
        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Order order= (Order)parent.getItemAtPosition(position);

        MyPreferences pref = new MyPreferences(this.getActivity());
        pref.save(getString(R.string.shared_pref_current_order_id), order.getId());
        pref.save(getString(R.string.shared_pref_current_order_status), order.getStatus());

        Intent intent = new Intent(getContext(), OrderActivity.class);
        intent.putExtra(Intent.EXTRA_UID,order.getId());
        startActivity(intent);
    }
}
