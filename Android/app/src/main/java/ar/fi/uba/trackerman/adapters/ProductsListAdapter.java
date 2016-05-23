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
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.product.SearchProductsListTask;
import ar.fi.uba.trackerman.utils.CircleTransform;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

public class ProductsListAdapter extends ArrayAdapter<Product> {

    private long total;
    private long offset;
    private long limit;
    private boolean fetched;
    private String brands; // utlizado para filtrar

    public ProductsListAdapter(Context context, int resource,
                               List<Product> products) {
        super(context, resource, products);
        limit = 0;
        total = 0;
        offset = 0;
        fetched = false;
    }
    public void refresh(){
        this.clear();
        limit = 0;
        offset = 0;
        total = 0;
        fetchMore();
    }

    public void fetchMore(){
        if(offset < total || !fetched){
            fetched = true;
            if (RestClient.isOnline(getContext())) new SearchProductsListTask(this,brands).execute(offset);
        }
    }

    public void addProducts(ProductsSearchResult productsSearchResult){
        if(productsSearchResult!=null) {
            this.addAll(productsSearchResult.getProducts());
            this.offset = this.getCount();
            this.total = productsSearchResult.getTotal();
            this.limit = productsSearchResult.getLimit();
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
            holder.idProduct = (TextView) convertView.findViewById(R.id.product_row_product_id);
            holder.name = (TextView) convertView.findViewById(R.id.product_row_name);
            holder.stock = (TextView) convertView.findViewById(R.id.product_row_product_stock);
            holder.promotion = (TextView) convertView.findViewById(R.id.product_row_product_promotion);
            holder.brand= (TextView) convertView.findViewById(R.id.product_row_brand);
            holder.image = (ImageView) convertView.findViewById(R.id.product_row_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.idProduct.setText(isContentValid(Long.toString(product.getId())));
        holder.name.setText(isContentValid(product.getName()));
        holder.stock.setText(isContentValid(Integer.toString(product.getStock())));
        holder.brand.setText(isContentValid(product.getBrandName()));
        holder.promotion.setText("");
        if (product.hasPromotion()) {
            holder.promotion.setText(isContentValid(Integer.toString(product.getBestPromotion().getPercent()) +" %"));
        }
        //Picasso.with(this.getContext()).load(product.getThumbnail()).into(holder.image);

        if (isContentValid(product.getThumbnail()).isEmpty()) {
            Picasso.with(this.getContext()).load(R.drawable.logo).transform(new CircleTransform()).into(holder.image);
        } else {
            Picasso.with(this.getContext()).load(product.getThumbnail()).transform(new CircleTransform()).into(holder.image);
        }

        return convertView;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    private static class ViewHolder {
        public TextView idProduct;
        public TextView name;
        public TextView stock;
        public TextView promotion;
        public TextView brand;
        public ImageView image;
    }
}
