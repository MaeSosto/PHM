package com.example.personalhealthmonitor.Database;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

import static com.example.personalhealthmonitor.MainActivity.KEY_NOTIFICATION;

public class SettingsViewModel extends AndroidViewModel {

    private SettingsDao settingsDao;
    private DB settingsDB;
    private LiveData<List<Settings>> mAllSettings;

    public SettingsViewModel(Application application){
        super(application);

        settingsDB = DB.getDatabase(application);
        settingsDao = settingsDB.settingsDao();
        mAllSettings = settingsDao.getAllSetting();
    }

    public void insertSettings(Settings Settings){
        new InsertAsyncTask(settingsDao).execute(Settings);
    }

    public void updateSettings(Settings Settings){
        new UpdateAsyncTask(settingsDao).execute(Settings);
    }

    public void deleteSettings(Settings Settings){
        new DeleteAsyncTask(settingsDao).execute(Settings);
    }

    public LiveData<List<Settings>> getAllSettings(){
        return settingsDao.getAllSetting();
    }

    public LiveData<List<Settings>> getmAllSettingsFilter(int filtro){
        String query = "SELECT * FROM settings WHERE settings_importanza >= "+ filtro;
        return settingsDao.getmAllSettingsFilter(new SimpleSQLiteQuery(query));
    }



    //OPERAZIONI ASYNC
    private class OperationAsyncTask extends AsyncTask<Settings, Void, Void> {

        SettingsDao AsyncTaskDao;

        public OperationAsyncTask(SettingsDao asyncTaskDao) {
            AsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Settings... Settingss) {
            return null;
        }
    }

    //OPERAZIONE DI INSERIMENTO
    private class InsertAsyncTask extends OperationAsyncTask{

        public InsertAsyncTask(SettingsDao SettingsDao) {
            super(SettingsDao);
        }

        @Override
        protected Void doInBackground(Settings... Settingss) {
            AsyncTaskDao.addSettings(Settingss[0]);
            return null;
        }
    }

    //OPERAZIONE DI MODIFICA
    private class UpdateAsyncTask extends AsyncTask<Settings, Void, Void> {

        SettingsDao mSettingsDao;

        public UpdateAsyncTask(SettingsDao SettingsDao) {
            this.mSettingsDao = SettingsDao;
        }

        @Override
        protected Void doInBackground(Settings... Settingss) {
            settingsDao.updateSettings(Settingss[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Settings, Void, Void> {
        SettingsDao mSettingsDao;

        public DeleteAsyncTask(SettingsDao SettingsDao) {
            this.mSettingsDao = SettingsDao;
        }


        @Override
        protected Void doInBackground(Settings... Settingss) {
            settingsDao.deleteSettings(Settingss[0]);
            return null;
        }
    }
}
