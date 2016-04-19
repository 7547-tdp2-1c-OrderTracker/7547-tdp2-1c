package ar.fi.uba.trackerman.domains;

/**
 * Created by plucadei on 10/4/16.
 */
public class Brand {
    private long id;
    private String name;
    private String picture;

    public Brand(long id, String name, String picture) {
        this.id= id;
        this.name = name;
        this.picture = picture;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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
}
