package ar.fi.uba.trackerman.utils;

/**
 * Created by smpiano on 5/2/16.
 */
public enum DayOfWeek {
    MONDAY(0, "Monday"),
    TUESDAY(1, "Tuesday"),
    WEDNESDAY(2 ,"Wednesday"),
    THURSDAY(3, "Thursday"),
    FRIDAY(4, "Friday"),
    SATURDAY(5, "Saturday"),
    SUNDAY(6, "Sunday");

    private Integer reference;
    private String eng;

    private DayOfWeek(Integer reference, String eng) {
        this.reference = reference;
        this.eng = eng;
    }

    public Integer getReference() {
        return reference;
    }

    public String toEng() {
        return eng;
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
