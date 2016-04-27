package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.exceptions.BusinessException;

/**
 * Created by plucadei on 29/3/16.
 */
public class Client {
    private long id;
    private String name;
    private String lastName;
    private String cuil;
    private String address;
    private String phoneNumber;
    private String email;
    private String sellerType;

    private double lon;
    private double lat;
    private String thumbnail;
    private String avatar;

    public Client(long id) {
        super();
        this.id= id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSellerType() {
        return sellerType;
    }

    public void setSellerType(String sellerType) {
        this.sellerType = sellerType;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setAddress(String address) { this.address = address; }

    public String getAddress() { return address; }

    public long getId() { return id; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getAvatar() { return avatar; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFullName() {
        return this.lastName +", "+ this.name;
    }

    public static Client fromJson(JSONObject json) {
        Client client = null;
        try {
            client = new Client(json.getLong("id"));
            client.setName(json.getString("name"));
            client.setLastName(json.getString("lastname"));
            client.setAddress(json.getString("address"));
            client.setThumbnail(json.getString("thumbnail"));

            client.setCuil(json.getString("cuil"));
            client.setLat(json.getDouble("lat"));
            client.setLon(json.getDouble("lon"));
            client.setEmail(json.getString("email"));
            client.setAvatar(json.getString("avatar"));
            client.setPhoneNumber(json.getString("phone_number"));
        } catch (JSONException e) {
            throw new BusinessException("Error parsing Client.",e);
        }
        return client;
    }
}
