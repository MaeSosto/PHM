package Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private ReportDao reportDao;
    private DB reportDB;
    private LiveData<List<Report>> mAllReports;

    public ReportViewModel(Application application){
        super(application);

        reportDB = DB.getDatabase(application);
        reportDao = reportDB.reportDao();
        mAllReports = reportDao.getAllReports();
    }

    public void setReport(Report report){
        new InsertAsyncTask(reportDao).execute(report);
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

}
