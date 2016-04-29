package ar.fi.uba.trackerman.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.domains.Brand;
import ar.fi.uba.trackerman.tasks.brand.GetBrandsListTask;
import ar.fi.uba.trackerman.utils.CircleTransform;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

/**
 * Created by plucadei on 10/4/16.
 */
public class BrandsListAdapter extends ArrayAdapter<Brand> implements AdapterView.OnItemClickListener,GetBrandsListTask.BrandsListAggregator{

    List<Long> selected;

    public BrandsListAdapter(Context context, int resource,
                             List<Brand> brands) {
        super(context, resource, resource, brands);
        selected= new ArrayList<>();
    }

    public void refresh(){
        this.clear();
        fetch();
    }

    public void fetch(){
        GetBrandsListTask asyncTask= new GetBrandsListTask(this);
        asyncTask.execute();
    }

    @Override
    public void addBrands(List<Brand> brands){
        if(brands!=null) {
            this.addAll(brands);
        }else{
            Log.w(this.getClass().getCanonicalName(), "Something when wrong getting brands.");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Brand brand = this.getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.brand_list_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.brand_row_name);
            holder.image = (ImageView) convertView.findViewById(R.id.brand_row_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(isContentValid(brand.getName()));
        Picasso.with(this.getContext()).load(brand.getPicture()).transform(new CircleTransform()).into(holder.image);
        if(selected.contains(brand.getId())){
            convertView.setBackgroundResource(R.color.colorAccent);
        }else {
            convertView.setBackgroundResource(android.R.color.transparent);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Brand brand=this.getItem(position);
        if(selected.contains(brand.getId())){
            selected.remove(brand.getId());
            view.setBackgroundResource(android.R.color.transparent);
        }else{
            selected.add(brand.getId());
            view.setBackgroundResource(R.color.colorAccent);
        }
    }

    public List<Long> getSelected(){
        return selected;
    }

    private static class ViewHolder {
        public TextView name;
        public ImageView image;
    }

}
