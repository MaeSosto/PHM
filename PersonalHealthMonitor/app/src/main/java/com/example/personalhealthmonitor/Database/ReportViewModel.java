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
