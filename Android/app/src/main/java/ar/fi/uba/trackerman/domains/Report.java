package ar.fi.uba.trackerman.domains;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.FieldValidator;

/**
 * Created by smpiano on 5/15/16.
 */
public class Report {
/**
 * {
 "range": {
 "start": "2015-01-01T00:00:00.000Z",
 "end": "2017-01-01T00:00:00.000Z"
 },
 "totals": {
 "visits": 1,
 "out_of_route_visits": 2,
 "amount": [
 {
 "currency": "ARS",
 "total": 2894
 }
 ],
 "confirmed_orders": 2
 }
 }
 */
    private Date start;
    private Date end;
    private int visits;
    private int outOfRouteVisits;
    private List<MoneyReport> moneyReports;
    private int confirmedOrders;

    public Report(Date start, Date end) {
        this.start = start;
        this.end = end;
        this.moneyReports = new ArrayList<MoneyReport>();
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getOutOfRouteVisits() {
        return outOfRouteVisits;
    }

    public void setOutOfRouteVisits(int outOfRouteVisits) {
        this.outOfRouteVisits = outOfRouteVisits;
    }

    public List<MoneyReport> getMoneyReports() {
        return moneyReports;
    }

    public int getConfirmedOrders() {
        return confirmedOrders;
    }

    public void setConfirmedOrders(int confirmedOrders) {
        this.confirmedOrders = confirmedOrders;
    }

    public static Report fromJson(JSONObject json) {
        try {
            JSONObject range = json.getJSONObject("range");
            String startStr = range.getString("start");
            Date start = null;
            if (FieldValidator.isValid(startStr)) start = DateUtils.parseDate(startStr);
            String endStr = range.isNull("end")?null:range.getString("end");
            Date end = null;
            if (FieldValidator.isValid(endStr)) end = DateUtils.parseDate(endStr);

            Report report = new Report(start, end);
            JSONObject totals = json.getJSONObject("totals");
            report.setVisits(totals.getInt("visits"));
            report.setOutOfRouteVisits(totals.getInt("out_of_route_visits"));
            report.setConfirmedOrders(totals.getInt("confirmed_orders"));

            JSONArray amount = totals.getJSONArray("amount");
            for(int i = 0; i<amount.length(); i++) {
                JSONObject moneyReport = amount.getJSONObject(i);
                report.getMoneyReports().add(MoneyReport.fromJson(moneyReport));
            }
            return report;
        } catch (JSONException e) {
            throw new BusinessException("Error parsing Report.",e);
        }
    }


    public static class MoneyReport {
        private String currency;
        private double total;

        public MoneyReport(String currency, double total) {
            this.currency = currency;
            this.total = total;
        }

        public String getCurrency() {
            return currency;
        }

        public double getTotal() {
            return total;
        }

        public static MoneyReport fromJson(JSONObject json) {
            try {
                return new MoneyReport(json.getString("currency"),json.getDouble("total"));
            } catch (JSONException e) {
                throw new BusinessException("Error parsing MoneyReport.",e);
            }
        }

        @Override
        public String toString() {
            return getCurrency()+" "+getTotal();
        }
    }

}
