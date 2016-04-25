package ar.fi.uba.trackerman.utils;

/**
 * Created by smpiano on 4/22/16.
 */
public enum OrderStatus {
    DRAFT("draft", "Borrador", "#1565c0") ,
    CANCEL("cancelled", "Cancelada", "#ff0000"),// FIXME luego borrar
    CANCELLED("cancelled", "Cancelada", "#ff0000"),
    CONFIRM("confirm", "Confirmada", "#558b2f"), //FIXME luego borrar
    CONFIRMED("confirmed", "Confirmada", "#558b2f");

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
