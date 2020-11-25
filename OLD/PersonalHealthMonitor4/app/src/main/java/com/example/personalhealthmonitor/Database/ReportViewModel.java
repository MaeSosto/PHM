package com.example.personalhealthmonitor.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;


import com.example.personalhealthmonitor.Utilities.Converters;

import java.util.Date;
import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private ReportDao reportDao;
    private DB reportDB;

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

    public Report getReport(int reportId){
        return reportDao.getReport(reportId);
    }

    public LiveData<List<Report>> getAllReports(Date inizio, Date fine ){
        String query = "SELECT * FROM reports ";
        //prendo tutti i report
        if(inizio == null && fine == null) return reportDao.getAllReports(new SimpleSQLiteQuery(query));
        //prendo i report di un periodo specifico
        if(inizio != null && fine != null) return reportDao.getAllReports(new SimpleSQLiteQuery(query+ "WHERE report_giorno >= "+ Converters.DateToLong(inizio)+ " AND report_giorno <= "+ Converters.DateToLong(fine)));
        //prendo i report di un giorno specifico
        if(inizio != null) return reportDao.getAllReports(new SimpleSQLiteQuery(query+ "WHERE report_giorno == "+ Converters.DateToLong(inizio)));
        return null;
    }

    public LiveData<Double> getAvgVal(String val, Date inizio, Date fine){
        String query = "SELECT AVG("+ val +") FROM reports";
        //prendo la media di un singolo valore in un periodo
        if(inizio != null && fine != null) return reportDao.getAVGVal(new SimpleSQLiteQuery( query+ " WHERE " + val + " != 0 AND report_giorno >= "+ Converters.DateToLong(inizio)+ " AND report_giorno <= "+ Converters.DateToLong(fine)));
        //prendo le medie di un singolo valore in un singolo giorno
        else if(inizio != null)  return reportDao.getAVGVal(new SimpleSQLiteQuery(query + " WHERE " + val + " != 0 AND report_giorno = "+Converters.DateToLong(inizio)));
        return null;
    }

    public Integer getCOUNTVal(String value1, String value2, Date inizio, Date fine) {
        String query = "SELECT COUNT(*) FROM reports ";
        if (value1 == null) {
            //Conta tutti i report
            if (inizio == null && fine == null)
                return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query));
            //conto i report in un periodo
            if (inizio != null && fine != null)
                return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query + " WHERE report_giorno >= " + Converters.DateToLong(inizio) + " AND report_giorno <= " + Converters.DateToLong(fine)));
        } else {
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


    //public LiveData<List<Report>> getReportValInPeriod(String val, int giornoInizio, int meseInizio, int annoInizio, int giornoFine, int meseFine, int annoFine){
    //    String query = "SELECT * FROM reports ";
    //    boolean where = false;
    //    if(val != null) {
    //        query = query + " WHERE " + val + " != 0";
    //        where = true;
    //    }
    //    //una settimana specifica
    //    if(giornoInizio != 0 && giornoFine != 0 && meseInizio != 0 && meseFine != 0 && annoInizio != 0 && annoFine != 0){
    //        if (where) query = query + " AND ";
    //        else query = query + " WHERE ";
    //        return reportDao.getReportValInPeriod(new SimpleSQLiteQuery(query + "report_giorno <= "+ giornoFine + " AND report_giorno >= "+ giornoInizio + " AND report_mese <= "+ meseFine+ " AND report_mese >= "+ meseInizio + " AND report_anno <= "+ annoFine + " AND report_anno >= "+ annoInizio));
    //    }
    //    //un mese specifico
    //    if(meseInizio != 0 && meseFine != 0 && annoInizio != 0 && annoFine != 0){
    //        if (where) query = query + " AND ";
    //        else query = query + " WHERE ";
    //        return reportDao.getReportValInPeriod(new SimpleSQLiteQuery(query + " report_mese <= "+ meseFine+ " AND report_mese >= "+ meseInizio + " AND report_anno <= "+ annoFine + " AND report_anno >= "+ annoInizio));
    //    }
    //    //un anno specifico
    //    if(annoInizio != 0 && annoFine != 0){
    //        if (where) query = query + " AND ";
    //        else query = query + " WHERE ";
    //        return reportDao.getReportValInPeriod(new SimpleSQLiteQuery(query + " AND report_anno <= "+ annoFine + " AND report_anno >= "+ annoInizio));
    //    }
    //    //tutto
    //    return reportDao.getReportValInPeriod(new SimpleSQLiteQuery(query));
    //}

    //OPERAZIONI ASYNC
    private class OperationAsyncTask extends AsyncTask<Report, Void, Void> {

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
    private class InsertAsyncTask extends OperationAsyncTask{

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
    private class UpdateAsyncTask extends AsyncTask<Report, Void, Void> {

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

    private class DeleteAsyncTask extends AsyncTask<Report, Void, Void> {
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

    private class CountAsyncTask extends AsyncTask<Report, Void, Integer> {

        String value;
        Date inizio;
        Date fine;

        public CountAsyncTask(String value, Date inizio, Date fine) {
            this.value = value;
            this.inizio = inizio;
            this.fine = fine;
        }

        @Override
        protected Integer doInBackground(Report... reports) {
            String query = "SELECT COUNT(*) FROM reports ";
            if(value == null) {
                //Conta tutti i report
                if (inizio == null && fine == null) {
                    return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query));

                }
                //conto i report in un periodo
                if (inizio != null && fine != null)
                    return reportDao.getCOUNTVal(new SimpleSQLiteQuery(query + " WHERE report_giorno >= " + Converters.DateToLong(inizio) + " AND report_giorno <= " + Converters.DateToLong(fine)));
            }
            else {
                query = query+ "WHERE "+ value+ " != 0 ";
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
    }
}
