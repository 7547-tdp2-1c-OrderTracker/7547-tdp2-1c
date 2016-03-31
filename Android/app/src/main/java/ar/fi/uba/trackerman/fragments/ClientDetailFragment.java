package ar.fi.uba.trackerman.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ar.fi.uba.trackerman.domains.Client;
import fi.uba.ar.soldme.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ClientDetailFragment extends Fragment {

    public ClientDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.fragment_client_detail, container, false);
    }

    public void updateClientInformation(Client client){
        Picasso.with(this.getContext()).load(client.getAvatar()).into(((ImageView)getView().findViewById(R.id.client_detail_avatar)));
        ((TextView)getView().findViewById(R.id.client_detail_id)).setText(Long.toString(client.getId()));
        ((TextView)getView().findViewById(R.id.client_detail_lastname)).setText(client.getLastName());
        ((TextView)getView().findViewById(R.id.client_detail_name)).setText(client.getName());
        ((TextView)getView().findViewById(R.id.client_detail_cuil)).setText(client.getCuil());
        ((TextView)getView().findViewById(R.id.client_detail_address)).setText(client.getAddress());
        ((TextView)getView().findViewById(R.id.client_detail_phone)).setText(client.getPhoneNumber());
        ((TextView)getView().findViewById(R.id.client_detail_email)).setText(client.getEmail());
    }
}
