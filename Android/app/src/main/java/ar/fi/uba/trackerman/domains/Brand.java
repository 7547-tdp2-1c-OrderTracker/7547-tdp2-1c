package ar.fi.uba.trackerman.domains;

/**
 * Created by plucadei on 10/4/16.
 */
public class Brand {
    private String name;
    private String picture;

    public Brand(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }
}
