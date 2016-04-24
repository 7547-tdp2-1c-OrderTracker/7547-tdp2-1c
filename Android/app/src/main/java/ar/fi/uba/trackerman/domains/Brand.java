package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.exceptions.OrderTrackerException;

/**
 * Created by plucadei on 10/4/16.
 */
public class Brand {
    private long id;
    private String name;
    private String code;
    private String picture;

    public Brand(long id, String name, String code, String picture) {
        this.id= id;
        this.name = name;
        this.code = code;
        this.picture = picture;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return this.code;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Brand){
            Brand brand= (Brand) object;
            return this.id==brand.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int)(id % Integer.MAX_VALUE);
    }

    public static Brand fromJson(JSONObject json) {
        try {
            return new Brand(json.getLong("id"),json.getString("name"),json.getString("code"),json.getString("picture"));
        } catch (JSONException e) {
            throw new OrderTrackerException("Error parsing Brand.",e);
        }
    }
}
