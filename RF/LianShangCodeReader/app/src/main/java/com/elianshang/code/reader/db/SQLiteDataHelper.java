package com.elianshang.code.reader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteDataHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    public static final String DATABASE_NAME = "lsh123_code_reader.db";

    /**
     * 数据库版本
     * 1.0 ---------------------------- 1
     */
    private static final int DATABASE_VERSION = 1;

    public SQLiteDataHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
