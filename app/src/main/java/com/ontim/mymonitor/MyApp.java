package com.ontim.mymonitor;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ontim.mymonitor.dao.DaoMaster;
import com.ontim.mymonitor.dao.DaoSession;

public class MyApp extends Application {

    private static final String TAG = "MyApp";
    public static DaoSession mSession;

    private static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Init Database");
        initDb();
        myApp = this;
    }

    public static MyApp getMyApp() {
        return myApp;
    }

    /**
     * 连接数据库并创建会话
     */
    public void initDb() {
        // 1、获取需要连接的数据库
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "data.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        // 2、创建数据库连接
        DaoMaster daoMaster = new DaoMaster(db);
        // 3、创建数据库会话
        mSession = daoMaster.newSession();
    }

    // 供外接使用
    public DaoSession getDaoSession() {
        return mSession;
    }

}
