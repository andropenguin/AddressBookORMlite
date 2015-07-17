package com.sarltokyo.addressbookormlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sarltokyo.addressbookormlite.entity.Address;
import com.sarltokyo.addressbookormlite.entity.Person;

import java.sql.SQLException;

/**
 * Created by osabe on 15/07/17.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private final static String TAG = DatabaseHelper.class.getSimpleName();

    private final static String DATABASE_NAME = "addressbook.db";
    private final static int DATABASE_VERSION = 1;

    private Dao<Person, Integer> mPersonDao;
    private Dao<Address, Integer> mAddressDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // エンティティを指定してcreate tableする
            TableUtils.createTable(connectionSource, Person.class);
            TableUtils.createTable(connectionSource, Address.class);
        } catch (SQLException e) {
            Log.e(TAG, "データベースを作成できませんでした", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // 省略
    }

    public Dao<Person, Integer> getPersonDao() throws SQLException {
        if (mPersonDao == null) {
            mPersonDao = getDao(Person.class);
        }
        return mPersonDao;
    }

    public Dao<Address, Integer> getAddressDao() throws SQLException {
        if (mAddressDao == null) {
            mAddressDao = getDao(Address.class);
        }
        return mAddressDao;
    }
}
