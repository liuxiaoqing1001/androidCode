package com.bignerdranch.android.sunflower.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.sunflower.R;
import com.bignerdranch.android.sunflower.helper.DBHelper;
import com.bignerdranch.android.sunflower.entity.Basic;
import com.bignerdranch.android.sunflower.entity.Word;
import com.bignerdranch.android.sunflower.utils.JsonUitl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecRequreActivity extends AppCompatActivity implements View.OnClickListener{

    //日志打印
    private String TAG = "RecRequreActivity";

    //1.界面初始化
    private EditText editText;
    private TextView textView;
    private ImageView mImageView;
    private Button addLib;
    private DBHelper dbHelper;


    //2.创建handler
    private Handler handler = new Handler(){
        //4.handler获取消息进行处理
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //1.获取对象
            Word word = (Word) msg.obj;
            //2.获取数据
            Basic basic = word.getBasic();
            String explains = null;
            explains = basic.getStrings(basic.getExplains());
            //3.在界面显示
            textView.setText(explains);
        }
    };


    // @param savedInstanceState

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_requre);

        editText = (EditText) findViewById(R.id.rec_sv);
        textView = (TextView) findViewById(R.id.rec_tv_info);
        mImageView=findViewById(R.id.rec_img_search);
        addLib=findViewById(R.id.bt_add_lib);

        mImageView.setOnClickListener(this);
        addLib.setOnClickListener(this);
        dbHelper = new DBHelper(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rec_img_search:
                //调用上网查词方法
                queryWord(editText.getText().toString());
                break;
            case R.id.bt_add_lib:
                Add_lib();
                break;
            default:
                break;
        }

    }

    public void Add_lib(){
        if (editText.getText().toString().trim().equals("")) {

            Toast.makeText(this, "请输入单词！", Toast.LENGTH_SHORT).show();
        } else {
            if (CheckDataInDBorNot(editText.getText().toString())) {
                Toast.makeText(this, "该单词已存在！", Toast.LENGTH_SHORT).show();
            } else {
                if(textView.getText().toString().equals("")){
                    Toast.makeText(this, "翻译为空，需要点一下搜索图标！", Toast.LENGTH_SHORT).show();
                }else {
                    Intent in = this.getIntent();
                    addWords(in.getStringExtra("Username"),editText.getText().toString(), textView.getText().toString());
                    Toast.makeText(this, "录入成功！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean CheckDataInDBorNot(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from words where word =?";
        Cursor cursor = db.rawQuery(Query, new String[]{value});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private void addWords(String username,String word, String means) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("word", word);
        values.put("means", means);
        db.insert("words", null, values);
        db.close();
    }

    private String queryMeans(String word,String means){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String Query = "Select means from words where word =?";
        db.rawQuery(Query, new String[]{word});
        return means;
    }

    private void modifyWordsMeans(String word,String means){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("means", means);
        db.update("words",values,"words=?",new String[]{word});

    }

//
//     //查询按钮的监听方法
//     //@param view
//
//    public void query(View view){
//        String word = editText.getText().toString();
//        System.out.println("\n"+word+"\n");
//        //调用上网查词方法
//        queryWord(word);
//    }

//    实现上网查词
//     1.创建OkClient和Request对象
//     2.创建Call对象
//     3.重写Call对象的enCall方法，异步GET请求
//     1.获取响应数据
//     2.封装成json对象
//     3.转为java对象
//     4.发送message给handler
//     @param s 要查询的单词

    public void queryWord(String s){
        String url = "https://fanyi.youdao.com/openapi.do?keyfrom=youdianbao&key=1661829537&type=data&doctype=json&version=1.1&q="+s;
        //1.创建OkClient和Request对象
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        //2.创建Call对象
        Call call = okHttpClient.newCall(request);
        //3.重写Call对象的enqueue方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+"请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //1.获取响应数据
                String str = response.body().string();
                Log.d(TAG, "onResponse: " + str);
                try {
                    //2.封装成json对象
                    JSONObject jsonObject = new JSONObject(str);
                    //3.转为java对象
                    Word word = (Word) JsonUitl.stringToObject(jsonObject.toString(),Word.class);
                    //4.创建message,包裹信息
                    Message message = new Message();
                    message.obj = word;
                    //5.发送message给handler
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}