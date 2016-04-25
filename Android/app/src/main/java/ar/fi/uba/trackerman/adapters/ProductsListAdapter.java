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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.fi.uba.trackerman.domains.Brand;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.domains.ProductsSearchResult;
import ar.fi.uba.trackerman.tasks.brand.GetBrandsListTask;
import ar.fi.uba.trackerman.tasks.product.SearchProductsListTask;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

public class ProductsListAdapter extends ArrayAdapter<Product> implements GetBrandsListTask.BrandsListAggregator {

    private long total;
    private long offset;
    private boolean fetching;
    private String brands; // utlizado para filtrar
    private Map<Long, Brand> allBrands; // todas las marcas

    public ProductsListAdapter(Context context, int resource,
                               List<Product> products) {
        super(context, resource, products);
        total=1;
        offset=0;
        fetching=false;
        this.allBrands = new HashMap<Long, Brand>();
        new GetBrandsListTask(this).execute();
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
            SearchProductsListTask asyncTask= new SearchProductsListTask(this,brands);
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

        holder.name.setText(isContentValid(product.getName()));
        holder.brand.setText(isContentValid(this.allBrands.get(product.getBrandId()).getName()));
        Picasso.with(this.getContext()).load(product.getThumbnail()).into(holder.image);

        return convertView;
    }

    @Override
    public void addBrands(List<Brand> brands) {
        for(Brand b : brands) {
            this.allBrands.put(b.getId(),b);
        }
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView brand;
        public ImageView image;
    }
}
