package ar.fi.uba.trackerman.utils;

/**
 * Created by smpiano on 4/22/16.
 */
public enum OrderStatus {
    DRAFT("draft"),
    CANCELLED("cancelled"),
    CONFIRMED("confirmed");

    private String status;

    private OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
