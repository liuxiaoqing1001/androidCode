package com.bignerdranch.android.xiaoshutong.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolHelper {
    public static List<Map<String,String>> CursorToList(Cursor cursor){
        List<Map<String,String>> list=new ArrayList<>();
        int colums = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            Map<String,String> map=new HashMap<>();
            for (int i = 0; i < colums; i++) {
                String columname = cursor.getColumnName(i);
                String columvalue = cursor.getString(cursor.getColumnIndex(columname));
                map.put(columname,columvalue);
                if (columvalue == null) {
                    columvalue = "";
                }
            }
            list.add(map);
        }
        return list;
    }

    public static Cursor loadDB(Context context, String sql) {
        SQLiteRelease sqLiteRelease = new SQLiteRelease(context);
        SQLiteDatabase db = sqLiteRelease.OpenDataBase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
    public static boolean excuteDB(Context context, String sql) {
        SQLiteRelease sqLiteRelease = new SQLiteRelease(context);
        SQLiteDatabase db = sqLiteRelease.OpenDataBase();
        db.execSQL(sql);
        return true;
    }


}

