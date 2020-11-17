package com.example.testmenu2.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.testmenu2.Utilities.Converters;

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

    public LiveData<List<Report>> getAllReports(Date date1, Date date2){
        String query = "SELECT * FROM reports";
        String query_final = " ORDER BY report_giorno DESC, report_ora DESC";
        if(date1 == null && date2 == null) return reportDao.getAllReports(new SimpleSQLiteQuery(query+ query_final));
        if(date2 == null) return reportDao.getAllReports(new SimpleSQLiteQuery(query+ " WHERE report_giorno= "+ Converters.DateToLong(date1)+ query_final ));
        else return reportDao.getAllReports(new SimpleSQLiteQuery(query+ " WHERE report_giorno>= "+ Converters.DateToLong(date1)+ " AND report_giorno<="+ Converters.DateToLong(date2)+ query_final ));
    }

    public LiveData<Report> getReport(int reportId){
        return reportDao.getReport(reportId);
    }

    public LiveData<List<Report>> getFilterReports(String S){
        return reportDao.getFilterReports(S);
    }

    public LiveData<Double> getAVGInDate(String value, Date data){
        return reportDao.getAVGInDate(new SimpleSQLiteQuery("SELECT AVG("+value+") FROM reports WHERE report_giorno= "+ Converters.DateToLong(data)+" AND "+ value+" != 0 "));
    }

    public LiveData<List<AVG>> getAVG(String value, Date date1, Date date2){
        String query = "SELECT AVG("+value+") AS media, report_giorno AS giorno FROM reports WHERE "+ value+" != 0 ";
        if(date1 != null && date2 != null) query = query+ " AND report_giorno>= "+ Converters.DateToLong(date1)+ " AND report_giorno<= "+ Converters.DateToLong(date2);
        query = query + " GROUP BY report_giorno ORDER BY report_giorno";
        return  reportDao.getAVG(new SimpleSQLiteQuery(query));
    }

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
}
