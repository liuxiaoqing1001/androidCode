package com.bignerdranch.android.sunflower.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bignerdranch.android.sunflower.R;
import com.bignerdranch.android.sunflower.helper.DBHelper;
import com.bignerdranch.android.sunflower.entity.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecLibActivity extends AppCompatActivity {

    DBHelper mDBHelper;
    ListView listView;
    List<Map<String,String>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_lib);

        mDBHelper = new DBHelper(this);
        ArrayList<Word> words = getWords();
        listView = findViewById(R.id.word_list);
        list = new ArrayList<Map<String, String>>();
        for (int i = 0;i < words.size() ;i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id",words.get(i).id+".");
            map.put("word", words.get(i).word);
            map.put("means", words.get(i).means);
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list, R.layout.list_item,
                new String[]{"id","word","means"},new int[]{R.id.id,R.id.word,R.id.means});

        listView.setAdapter(simpleAdapter);

    }

    private ArrayList<Word> getWords(){
        ArrayList<Word> words = new ArrayList<>();
        Intent in = this.getIntent();
        System.out.println(in.getStringExtra("Username"));
        Cursor cursor = mDBHelper.getReadableDatabase().query("words",null,
                "username=?",new String[]{in.getStringExtra("Username")},
                null,null,null);
//        int i = 1;
        while(cursor.moveToNext()){
            Word word = new Word();
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            word.id = cursor.getString(cursor.getColumnIndex("id"));
            word.word = cursor.getString(cursor.getColumnIndex("word"));
            word.means = cursor.getString(cursor.getColumnIndex("means"));
            words.add(word);
//            i++;
        }
        return words;
    }
}
