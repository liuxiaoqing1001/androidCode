package com.bignerdranch.android.sunflower.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.sunflower.R;
import com.bignerdranch.android.sunflower.helper.DBHelper;
import com.bignerdranch.android.sunflower.entity.Word;

import java.util.ArrayList;

public class RecListenActivity extends AppCompatActivity implements View.OnClickListener{

    DBHelper mDBHelper;
    TextView tv_word, tv_means;
    Button bt_easy, bt_forget, bt_next;
    ArrayList<Word> words;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_listen);

        tv_word = findViewById(R.id.recite_tv_word);
        tv_means = findViewById(R.id.recite_tv_means);
        bt_easy = findViewById(R.id.recite_btn_easy);
        bt_forget = findViewById(R.id.recite_btn_forget);
        bt_next = findViewById(R.id.recite_btn_next);

        mDBHelper = new DBHelper(this);
        bt_next.setOnClickListener(this);
        bt_forget.setOnClickListener(this);
        bt_easy.setOnClickListener(this);

        words = getWords();
        if(words.size()==0){
            Toast.makeText(this,"您当前没有任何单词库存！",Toast.LENGTH_SHORT).show();
        }else {
            String word = null;
            word = words.get(i).word;
            tv_word.setText(word);
        }


    }

    private ArrayList<Word> getWords(){
        ArrayList<Word> words = new ArrayList<>();
        Intent in = this.getIntent();
        System.out.println(in.getStringExtra("Username"));
        Cursor cursor = mDBHelper.getReadableDatabase().query("words",null,
                "username=?",new String[]{in.getStringExtra("Username")},
                null,null,null);
        while(cursor.moveToNext()){
            Word word = new Word();
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            word.word = cursor.getString(cursor.getColumnIndex("word"));
            word.means = cursor.getString(cursor.getColumnIndex("means"));
            words.add(word);
        }
        return words;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recite_btn_easy:
                if(i!=0){
                    i--;
                    tv_word.setText(words.get(i).word);
                    tv_means.setText("");
                    System.out.println("mmmm"+i);
                }else {
                    Toast.makeText(this,"这已经是第一个单词了！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.recite_btn_next:
                System.out.println(words.size());
                if(i>=words.size()-1){
                    Toast.makeText(this,"已经全部过一遍了！",Toast.LENGTH_SHORT).show();
                }else {
                    System.out.println(i);
                    i++;
                    tv_word.setText(words.get(i).word);
                    tv_means.setText("");
                    System.out.println("..........."+i);
                }
                break;
            case R.id.recite_btn_forget:
                if(words.size()==0){
                    Toast.makeText(this,"请问有单词吗？！",Toast.LENGTH_SHORT).show();
                }else {
                    tv_means.setText(words.get(i).means);
                }
                break;
        }
    }
}
