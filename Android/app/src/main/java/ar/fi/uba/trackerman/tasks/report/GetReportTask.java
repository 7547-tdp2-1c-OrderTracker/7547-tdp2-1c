package ar.fi.uba.trackerman.tasks.report;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.domains.Report;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.AppSettings;

/**
 * Created by smpiano on 15/5/16.
 */
public class GetReportTask extends AbstractTask<String,Void,Report,AppCompatActivity> {

    public GetReportTask(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected Report doInBackground(String... params) {
        String start = params[0];
        String end = params[1];
        Report report = null;
        try {
            report = (Report) restClient.get("/v1/sellers/"+ AppSettings.getSellerId()+"/reports?start_date="+start);
        } catch (BusinessException e) {
            ((ReportReceiver) weakReference.get()).showSnackbarSimpleMessage(e.getMessage());
        }
        return report;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject clientJson = new JSONObject(json);
        return Report.fromJson(clientJson);
    }

    @Override
    protected void onPostExecute(Report report) {
        super.onPostExecute(report);
        if (report != null){
            ((ReportReceiver) weakReference.get()).updateReport(report);
        } else{
            ((ReportReceiver) weakReference.get()).showSnackbarSimpleMessage("No se puede obtener info del reporte");
        }
    }

    public interface ReportReceiver {
        public void updateReport(Report report);
        public void showSnackbarSimpleMessage(String message);
    }

}
