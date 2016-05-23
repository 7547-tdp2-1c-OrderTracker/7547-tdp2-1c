package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ar.fi.uba.trackerman.exceptions.BusinessException;

/**
 * Created by guido on 15/05/16.
 */
public class Seller {

    private long id;
    private String name;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String avatar;
    private Date dateCreated;
    private Date lastModified;

    public Seller(long id) {
        this.id= id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public static Seller fromJson(JSONObject json) {
        Seller seller = null;
        try {
            seller = new Seller(json.getLong("id"));

            seller.setName(json.getString("name"));
            seller.setLastName(json.getString("lastname"));

            seller.setEmail(json.getString("email"));
            seller.setAvatar(json.getString("avatar"));
            seller.setPhoneNumber(json.getString("phone_number"));

        } catch (JSONException e) {
            throw new BusinessException("Error parsing Seller.", e);
        }
        return seller;
    }
}
