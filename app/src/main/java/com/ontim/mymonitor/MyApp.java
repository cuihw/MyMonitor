package com.ontim.mymonitor;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

import com.ontim.mymonitor.Util.SharedPreferencesUtil;
import com.ontim.mymonitor.dao.DaoMaster;
import com.ontim.mymonitor.dao.DaoSession;

public class MyApp extends Application implements CameraXConfig.Provider {

    private static final String TAG = "MyApp";
    public static DaoSession mSession;

    private static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Init Database");
        initDb();
        SharedPreferencesUtil.initSharedPerferences(getApplicationContext());
        myApp = this;
    }

    public static MyApp getMyApp() {
        return myApp;
    }

    /**
     * init db
     */
    public void initDb() {
        // 1、create a database.
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "data.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        // 2、create the data connection.
        DaoMaster daoMaster = new DaoMaster(db);
        // 3、create the database session.
        mSession = daoMaster.newSession();
    }

    // mothed: getDaoSession
    public DaoSession getDaoSession() {
        return mSession;
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
                .setMinimumLoggingLevel(Log.INFO).build();
    }
}
