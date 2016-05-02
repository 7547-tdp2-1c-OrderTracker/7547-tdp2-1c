package ar.fi.uba.trackerman.utils;

/**
 * Created by smpiano on 5/2/16.
 */
public enum DayOfWeek {
    SUNDAY(0, "Sunday", "Domingo"),
    MONDAY(1, "Monday", "Lunes"),
    TUESDAY(2, "Tuesday", "Martes"),
    WEDNESDAY(3 ,"Wednesday", "Miércoles"),
    THURSDAY(4, "Thursday", "Jueves"),
    FRIDAY(5, "Friday", "Viernes"),
    SATURDAY(6, "Saturday", "Sábado");

    private Integer reference;
    private String eng;
    private String esp;

    private DayOfWeek(Integer reference, String eng, String esp) {
        this.reference = reference;
        this.eng = eng;
        this.esp = esp;
    }

    public Integer getReference() {
        return reference;
    }

    public String toEng() {
        return eng;
    }

    public String toEsp() {
        return esp;
    }

    public static DayOfWeek byReference(Integer reference) {
        DayOfWeek current = null;
        for(DayOfWeek d : DayOfWeek.values()) {
            if (d.getReference().compareTo(reference) == 0) current = d;
        }
        return current;
    }

    public static boolean isWorkingDay(Integer reference){
        return  (DayOfWeek.byReference(reference) == DayOfWeek.MONDAY
                || DayOfWeek.byReference(reference) == DayOfWeek.TUESDAY
                || DayOfWeek.byReference(reference) == DayOfWeek.WEDNESDAY
                || DayOfWeek.byReference(reference) == DayOfWeek.THURSDAY
                || DayOfWeek.byReference(reference) == DayOfWeek.FRIDAY);
    }
}
