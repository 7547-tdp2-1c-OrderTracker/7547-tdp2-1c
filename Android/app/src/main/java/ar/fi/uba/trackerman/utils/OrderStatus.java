package ar.fi.uba.trackerman.utils;

/**
 * Created by smpiano on 4/22/16.
 */
public enum OrderStatus {
    DRAFT("draft", "Borrador", "#1565c0") ,
    CANCEL("cancelled", "Cancelada", "#ff0000"),// deprecada por CANCELLED
    CANCELLED("cancelled", "Cancelada", "#ff0000"),
    CONFIRM("confirm", "Confirmada", "#558b2f"), //deprecada por CONFIRMED
    CONFIRMED("confirmed", "Confirmada", "#558b2f"),
    DELIVERED("delivered", "Entregado", "#E9F50A"),
    INTRANSIT("intransit", "EnTransito", "#B60AF5"),
    PREPARED("prepared", "Preparado", "#F5C60A");

    private String status;
    private String translate;
    private String color;

    private OrderStatus(String status, String translate, String color) {
        this.status = status;
        this.translate = translate;
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public String getTranslate() {
        return translate;
    }

    public String getColor() {
        return color;
    }
}
