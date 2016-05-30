package ar.fi.uba.trackerman.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
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

import java.util.ArrayList;

import ar.fi.uba.trackerman.activities.ProductActivity;
import ar.fi.uba.trackerman.adapters.ProductsListAdapter;
import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.domains.Visit;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.order.PostOrderItemsTask;
import ar.fi.uba.trackerman.tasks.visit.PostVisitTask;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isValidQuantity;

/**
 * Created by plucadei on 3/4/16.
 */
public class ProductsListFragment extends Fragment implements AdapterView.OnItemClickListener, PostOrderItemsTask.OrderItemCreator {

    private String brands;
    private ProductsListAdapter productsAdapter;
    private long productId;
    private int productStock;
    ListView productsList;
    private MyPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        brands=null;
        pref = new MyPreferences(this.getActivity());
        View fragmentView= inflater.inflate(R.layout.fragment_products_list, container, false);
        productsList= (ListView)fragmentView.findViewById(R.id.productsListView);
        Activity activity= getActivity();

        productsAdapter = new ProductsListAdapter( getContext(), R.layout.products_list_item, new ArrayList<Product>());
        productsAdapter.setBrands(brands);
        productsList.setAdapter(productsAdapter);
        productsList.setOnItemClickListener(this);

        if (isLongPressValid()) {
            registerForContextMenu(productsList);
        }

        ProgressBar bar= new ProgressBar(getContext());
        bar.setIndeterminate(true);
        productsList.setEmptyView(bar);
        productsAdapter.refresh();
        return fragmentView;
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    private boolean isLongPressValid() {
        if (getActivity()==null) return false;
        long orderId = pref.get(getActivity().getString(R.string.shared_pref_current_order_id), -1L);
        return (orderId >= 0);
    }

    public void setBrands(String brands){
        productsAdapter.setBrands(brands);
        productsAdapter.refresh();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)menuInfo;

        Product product= (Product)productsList.getAdapter().getItem(info.position);
        menu.setHeaderTitle(product.getName());
        this.productId = product.getId();
        this.productStock = product.getStock();

        inflater.inflate(R.menu.menu_products_context, menu);
    }

    private boolean isValidStock(String quantityRequested) {
        return Integer.parseInt(quantityRequested) <= this.productStock;
    }

    public void showQuantityDialog() {

        final EditText edittext = new EditText(getContext());
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter.LengthFilter filter= new InputFilter.LengthFilter(4);
        edittext.setFilters(new InputFilter[]{filter});

        new AlertDialog.Builder(this.getContext())
                .setTitle("Indicar cantidad")
                .setMessage("")
                .setView(edittext)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String quantityRequested = edittext.getText().toString();
                        CoordinatorLayout cl = (CoordinatorLayout) getActivity().findViewById(R.id.cordinator_layout_products_list);

                        if (isValidQuantity(quantityRequested)) {
                            if (isValidStock(quantityRequested)) {
                                long orderId = pref.get(getString(R.string.shared_pref_current_order_id), -1L);

                                if (orderId >= 0) {
                                    if (RestClient.isOnline(getContext())) new PostOrderItemsTask(ProductsListFragment.this).execute(String.valueOf(orderId), String.valueOf(productId), String.valueOf(quantityRequested));
                                } else {
                                    ShowMessage.showSnackbarSimpleMessage(cl, "No hay un pedido asociado!");
                                }
                            } else {
                                ShowMessage.showSnackbarSimpleMessage(cl, "Lo siento! "+((productStock>0)? "disponemos de " + productStock + " unidades." : "no disponemos de unidades."));
                            }
                        } else {
                            ShowMessage.showSnackbarSimpleMessage(cl, "El valor es inválido!");
                        }

                    }})
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    @Override
    public void afterCreatingOrderItem(OrderItem orderItemCreated) {
        String msg = "Item/s Agregado/s al pedido!";
        if (getView() != null) {
            ShowMessage.showSnackbarSimpleMessage(getView(),msg);
        } else {
            ShowMessage.toastMessage(getContext(),msg);
        }
    }

    @Override
    public View getCurrentView() {
        return this.getView();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_item_to_order:
                showQuantityDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product product= (Product)parent.getItemAtPosition(position);
        Intent intent = new Intent(getContext(), ProductActivity.class);
        intent.putExtra(Intent.EXTRA_UID,product.getId());
        startActivity(intent);
    }
}
