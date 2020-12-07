package com.example.personalhealthmonitor.Database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.example.personalhealthmonitor.Utilities.Converters;
import java.util.Date;
import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private final ReportDao reportDao;
    private final DB reportDB;

    public ReportViewModel(Application application){
        super(application);
        reportDB = DB.getDatabase(application);
        reportDao = reportDB.reportDao();
    }

    public void setReport(Report report){
        new InsertAsyncTask(reportDao).execute(report);
    }

    public void updateReport(Report report){
        new UpdateAsyncTask(reportDao).execute(report);
    }

    public void deleteReport(Report report){
        new DeleteAsyncTask(reportDao).execute(report);
    }

    public LiveData<Report> getReport(int reportId){
        return reportDao.getReport(reportId);
    }

    //RESTITUISCE TUTTI I REPORT/ I REPORT IN UN GIORNO SPECIFICO O IN UN PERIODO SPECIFICO
    public LiveData<List<Report>> getAllReports(Date inizio, Date fine ){
        String query = "SELECT * FROM reports ";
        //prendo tutti i report
        if(inizio == null && fine == null) return reportDao.getAllReports(new SimpleSQLiteQuery(query));
        //prendo i report di un periodo specifico
        if(inizio != null && fine != null) return reportDao.getAllReports(new SimpleSQLiteQuery(query+ "WHERE report_giorno >= "+ Converters.DateToLong(inizio)+ " AND report_giorno <= "+ Converters.DateToLong(fine)));
        //prendo i report di un giorno specifico
        if(inizio != null) return reportDao.getAllReports(new SimpleSQLiteQuery(query+ "WHERE report_giorno = "+ Converters.DateToLong(inizio)));
        return null;
    }

    //RESTITUISCE LA MEDIA DI UN VALORE GENERALE/ IN UN GIORNO/ IN UN PERIODO SPECIFICO
    public Double getAvgVal(String val, Date inizio, Date fine){
        String query = "SELECT AVG("+ val +") FROM reports";
        //prendo la media di un singolo valore in un periodo
        if(inizio != null && fine != null){
            //Log.i("QUERY", query+ " WHERE " + val + " != 0 AND report_giorno >= "+ Converters.DateToLong(inizio)+ " AND report_giorno <= "+ Converters.DateToLong(fine));
            return reportDao.getAVGVal(new SimpleSQLiteQuery( query+ " WHERE " + val + " != 0 AND report_giorno >= "+ Converters.DateToLong(inizio)+ " AND report_giorno <= "+ Converters.DateToLong(fine)));
        }
        //prendo le medie di un singolo valore in un singolo giorno
        else if(inizio != null){
            //Log.i("QUERY", query + " WHERE " + val + " != 0 AND report_giorno = "+Converters.DateToLong(inizio));
            return reportDao.getAVGVal(new SimpleSQLiteQuery(query + " WHERE " + val + " != 0 AND report_giorno = "+Converters.DateToLong(inizio)));
        }
        return null;
    }

    //CONTA I REPORT GENERALI/IN UN GIORNO/ IN UN PERIODO/ O CHE ABBIANO UN VALORE DIVERSO DA 0
    public Integer getCOUNTVal(String value1, String value2, Date inizio, Date fine) {
        String query = "SELECT COUNT(*) FROM reports ";
        //Log.i("Query", "Sono nel viewmodel");
        if (value1 == null) {
            //Conta tutti i report
            if (inizio == null && fine == null)
                return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query));
            if(inizio != null && fine == null) {
                //Log.i("QUERY", query + " WHERE report_giorno = " + Converters.DateToLong(inizio));
                return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query + " WHERE report_giorno = " + Converters.DateToLong(inizio)));
            }
            //conto tutti i report in un periodo
            if (inizio != null && fine != null)
                return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query + " WHERE report_giorno >= " + Converters.DateToLong(inizio) + " AND report_giorno <= " + Converters.DateToLong(fine)));
        } else {
            //Ho implementato queste per controllare i due valori della pressione e glicemia
            if(value1 != null && value2 != null) query = query + "WHERE (" + value1 + " != 0 OR "+ value2 + " != 0 )";
            else query = query + "WHERE " + value1 + " != 0 ";
            if (inizio == null && fine == null)
                return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query));
            //conto i report in un periodo
            if (inizio != null && fine != null)
                return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query + " AND report_giorno >= " + Converters.DateToLong(inizio) + " AND report_giorno <= " + Converters.DateToLong(fine)));
        }
        //prendo le medie di un singolo valore in un singolo giorno
        //else if(inizio != null)  return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query + " WHERE " + val + " != 0 AND report_giorno = "+Converters.DateToLong(inizio)));
        return 0;
    }

    //RESSTITUISCE IL REPORT PIU RECENTE O PIU VECCHIO (SE STRINGA = MAX ALLORA IL PIU RECENTE, SE = MIN IL PIU VECCHIO) SE VAL != 0 ALLORA CONTROLLA CHE UN VALORE SIA DIVERSO DA 0
    public Date getMinMaxDateReport(String max, String val){
        String query = "SELECT "+ max +"(report_giorno) from reports" ;
        if(val != null) return reportDao.getMinMaxDateReport(new SimpleSQLiteQuery(query +" WHERE "+ val+ " != 0"));
        else return reportDao.getMinMaxDateReport(new SimpleSQLiteQuery(query));
    }

    //RESTITUISCE TUTTI I REPORT CON I VALORI INERENTI AL FILTRO/ PRENDE TUTTI I REPORT/IN UN GIORNO/ IN UN PERIODO
    public LiveData<List<Report>> getFilterReports(Date inizio, Date fine, List<Settings> settings){
        String query = "SELECT * FROM reports ";
        String query_final = " ORDER BY report_giorno DESC, report_ora DESC";
        String filter = "";
        if(settings.size() > 0){
            filter = " AND ( ";
            boolean firstOr =false;
            for(int i = 0; i< settings.size(); i++){
                if(firstOr) filter = filter + " OR ";
                firstOr = true;
                filter = filter + settings.get(i).getValore()+" != 0 ";
            }
            filter += " ) ";
            //Log.i("QUERY: ", filter);
        }
        else return reportDao.emptyReportList();
        //prendo tutti i report
        if(inizio == null && fine == null) return reportDao.getAllReports(new SimpleSQLiteQuery(query + query_final));
        //prendo i report di un periodo specifico
        if(inizio != null && fine != null) return reportDao.getAllReports(new SimpleSQLiteQuery(query+ "WHERE report_giorno >= "+ Converters.DateToLong(inizio)+ " AND report_giorno <= "+ Converters.DateToLong(fine) + filter + query_final));
        //prendo i report di un giorno specifico
        if(inizio != null){
            Log.i("QUERY", query+"WHERE report_giorno = "+ Converters.DateToLong(inizio)+ filter);
            return reportDao.getAllReports(new SimpleSQLiteQuery(query+ "WHERE report_giorno = "+ Converters.DateToLong(inizio)+ filter + query_final));
        }
        return null;
    }

    //OPERAZIONI ASYNC
    private static class OperationAsyncTask extends AsyncTask<Report, Void, Void> {

        ReportDao AsyncTaskDao;

        public OperationAsyncTask(ReportDao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            return null;
        }
    }

    //OPERAZIONE DI INSERIMENTO
    private static class InsertAsyncTask extends OperationAsyncTask{

        public InsertAsyncTask(ReportDao reportDao) {
            super(reportDao);
        }

        @Override
        protected Void doInBackground(Report... reports) {
            AsyncTaskDao.addReport(reports[0]);
            return null;
        }
    }

    //OPERAZIONE DI MODIFICA
    private static class UpdateAsyncTask extends AsyncTask<Report, Void, Void> {

        ReportDao mreportDao;

        public UpdateAsyncTask(ReportDao reportDao) {
            this.mreportDao = reportDao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            mreportDao.updateReport(reports[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Report, Void, Void> {
        ReportDao mreportDao;

        public DeleteAsyncTask(ReportDao reportDao) {
            this.mreportDao = reportDao;
        }


        @Override
        protected Void doInBackground(Report... reports) {
            mreportDao.deleteReport(reports[0]);
            return null;
        }
    }
}
