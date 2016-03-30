package ar.fi.uba.trackerman.domains;

/**
 * Created by plucadei on 29/3/16.
 */
public class Client {
    private String name;
    private String lastName;
    private String cuit;
    private String email;
    private String lon;
    private String lat;
    private String thumbnail;

    public Client() {
        this.name = name;
        this.lastName = lastName;
        this.cuit = cuit;
        this.email = email;
        this.lon = lon;
        this.lat = lat;
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

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
