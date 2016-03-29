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

    public Client(String name, String lastName, String cuit, String email, String lon, String lat){
        this.name=name;
        this.lastName=lastName;
        this.cuit=cuit;
        this.email=email;
        this.lon= lon;
        this.lat= lat;
    }
    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCuit() {
        return cuit;
    }

    public String getEmail() {
        return email;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }
}
