package ar.fi.uba.trackerman.domains;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.FieldValidator;

/**
 * Created by smpiano on 5/1/16.
 */
public class Semaphore {
    private Date date;
    private int dayOfWeek;
    private int red;
    private int green;
    private int yellow;

    public Semaphore(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getYellow() {
        return yellow;
    }

    public void setYellow(int yellow) {
        this.yellow = yellow;
    }

    public static Semaphore fromJson(JSONObject json) {
        Semaphore semaphore = null;
        try {
            semaphore = new Semaphore(json.getInt("day_of_week"));

            String dateStr = json.getString("date");
            Date date = null;
            if (FieldValidator.isValid(dateStr)) date = DateUtils.parseDate(dateStr);
            semaphore.setDate(date);
            semaphore.setRed(json.getInt("red"));
            semaphore.setGreen(json.getInt("green"));
            semaphore.setYellow(json.getInt("yellow"));

        } catch (JSONException e) {
            throw new BusinessException("Error parsing Semaphore.",e);
        }
        return semaphore;
    }
}
