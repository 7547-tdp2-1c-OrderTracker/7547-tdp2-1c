package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.FieldValidator;

/**
 * Created by plucadei on 29/3/16.
 */
public class Client {

    private long id;
    private String name;
    private String lastName;
    private String company;
    private String cuil;
    private String address;
    private String phoneNumber;
    private String email;
    private String sellerType;

    private double lon;
    private double lat;
    private String thumbnail;
    private String avatar;


    private Date dateCreated;
    private Date lastModified;
    private double distance;
    private Date visited;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
        if ((!this.name.isEmpty()) && (!this.name.isEmpty())) {
            return this.lastName +", "+ this.name;
        } else {
            if (!this.name.isEmpty()){
                return this.name;
            } else {
                return this.lastName;
            }
        }
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Date getVisited() {
        return visited;
    }

    public void setVisited(Date visited) {
        this.visited = visited;
    }

    public static Client fromJson(JSONObject json) {
        Client client = null;
        try {
            client = new Client(json.getLong("id"));
            client.setName(json.getString("name"));
            client.setLastName(json.getString("lastname"));
            client.setCompany(json.getString("company"));
            client.setAddress(json.getString("address"));
            client.setThumbnail(json.getString("thumbnail"));

            client.setCuil(json.getString("cuil"));
            if (!json.isNull("lat")) client.setLat(json.getDouble("lat"));
            if (!json.isNull("lon")) client.setLon(json.getDouble("lon"));
            client.setEmail(json.getString("email"));
            client.setAvatar(json.getString("avatar"));
            client.setPhoneNumber(json.getString("phone_number"));

            String dateCreatedStr = json.getString("date_created");
            Date dateCreated = null;
            if (FieldValidator.isValid(dateCreatedStr)) dateCreated = DateUtils.parseDate(dateCreatedStr);
            client.setDateCreated(dateCreated);
            String lastModifiedStr = json.getString("last_modified");
            Date lastModified = null;
            if (FieldValidator.isValid(lastModifiedStr)) lastModified = DateUtils.parseDate(lastModifiedStr);
            client.setLastModified(lastModified);
            if (json.toString().contains("distance")) client.setDistance(json.getDouble("distance"));
            if (json.toString().contains("visited")){
                String visitedStr = json.getString("visited");
                Date visited = null;
                if (FieldValidator.isValid(visitedStr)) visited = DateUtils.parseDate(visitedStr);
                client.setVisited(visited);
            }


        } catch (JSONException e) {
            throw new BusinessException("Error parsing Client.",e);
        }
        return client;
    }
}
