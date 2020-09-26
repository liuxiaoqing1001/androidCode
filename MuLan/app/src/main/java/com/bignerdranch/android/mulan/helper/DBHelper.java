package com.bignerdranch.android.mulan.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "MuLan.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String users_table = "create table user" +
                "(id integer primary key autoincrement, username text," +
                "password text)";
        db.execSQL(users_table);
        String plan_table = "create table plans" +
                "(id integer primary key autoincrement, planname text," +
                "username text,address text,times text)";
        db.execSQL(plan_table);
        String service_table = "create table service" +
                "(id integer primary key autoincrement, servicename text," +
                "username text,address text,times text,types text)";
        db.execSQL(service_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table user add column other string");
        db.execSQL("alter table plans add column other string");
        db.execSQL("alter table service add column other string");
    }
}

