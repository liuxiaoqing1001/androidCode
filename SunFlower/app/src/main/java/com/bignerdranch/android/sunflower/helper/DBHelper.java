package com.bignerdranch.android.sunflower.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "sunflower.db";

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
        String words_table = "create table words" +
                "(id integer primary key autoincrement, username text," +
                "word text, means text)";
        db.execSQL(words_table);
        String course_table = "create table course" +
                "(cId integer primary key autoincrement, username text,weekStart integer," +
                "weekEnd integer,weekType integer,week integer,lessonStart integer," +
                "lessonEnd integer,cName text,teacher text,place text)";
        db.execSQL(course_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table user add column other string");
        db.execSQL("alter table words add column other string");
        db.execSQL("alter table course add column other string");
    }
}

