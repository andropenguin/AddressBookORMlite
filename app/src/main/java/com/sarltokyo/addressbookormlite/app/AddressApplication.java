package com.sarltokyo.addressbookormlite.app;

import android.app.Application;
import com.sarltokyo.addressbookormlite.dao.DatabaseHelper;

/**
 * Created by osabe on 15/07/17.
 */
public class AddressApplication extends Application {
    private static AddressApplication instance;
    private DatabaseHelper mDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AddressApplication getInstance() {
        return instance;
    }

    public DatabaseHelper getDatabaseHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(this);
        }
        return mDatabaseHelper;
    }
}
