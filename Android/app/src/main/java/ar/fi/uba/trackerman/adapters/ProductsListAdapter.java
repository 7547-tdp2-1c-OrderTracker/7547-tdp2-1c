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
import ar.fi.uba.trackerman.tasks.SearchProductsListTask;
import ar.fi.uba.trackerman.utils.CircleTransform;
import fi.uba.ar.soldme.R;

public class ProductsListAdapter extends ArrayAdapter<Product> {
    private long total;
    private long offset;
    private boolean fetching;

    public ProductsListAdapter(Context context, int resource,
                               List<Product> products) {
        super(context, resource, products);
        total=1;
        offset=0;
        fetching=false;
    }
    public void refresh(){
        this.clear();
        offset=0;
        total=1;
        fetchMore();
    }

    public void fetchMore(){
        if(offset<total && !fetching){
            fetching=true;
            SearchProductsListTask asyncTask= new SearchProductsListTask(this);
            asyncTask.execute(offset);
        }
    }

    public void addProducts(ProductsSearchResult productsSearchResult){
        if(productsSearchResult!=null) {
            this.addAll(productsSearchResult.getProducts());
            this.offset = this.getCount();
            this.total = productsSearchResult.getTotal();
            fetching = false;
        }else{
            Log.w(this.getClass().getCanonicalName(), "Something when wrong getting products.");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = this.getItem(position);
        ViewHolder holder;
        if(position==this.getCount()-1){
            fetchMore();
        }

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.products_list_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.product_row_name);
            holder.brand= (TextView) convertView.findViewById(R.id.product_row_brand);
            holder.image = (ImageView) convertView.findViewById(R.id.product_row_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(product.getName());
        holder.brand.setText(product.getBrand());
        Picasso.with(this.getContext()).load(product.getThumbnail()).into(holder.image);

        return convertView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView brand;
        public ImageView image;
    }
}
