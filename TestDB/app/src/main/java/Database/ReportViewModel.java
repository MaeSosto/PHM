package Database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;

public class ReportViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private ReportDao reportDao;
    private DB reportDB;

    public ReportViewModel(Application application){
        super(application);

        //Prendo le istanze del database e del dao
        reportDB = DB.getDatabase(application);
        reportDao = reportDB.reportDao();
    }

    public void insert(Report report){
        new InsertAsyncTask(reportDao).execute(report);
    }

    @Override
    protected void onCleared(){
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyed");
    }

    private class OperationsAsyncTask extends AsyncTask<Report, Void, Void> {

        ReportDao mAsyncTaskDao;

        OperationsAsyncTask(ReportDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(ReportDao mReportDao) {
            super(mReportDao);
        }

        @Override
        protected Void doInBackground(Report... reports) {
            mAsyncTaskDao.insert(reports[0]);
            return null;
        }
    }
}
