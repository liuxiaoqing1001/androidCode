package com.bignerdranch.android.mulan;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.mulan.helper.DBHelper;

public class RegistActivity extends Activity implements TextWatcher {


    private EditText edit_register, edit_setpassword, edit_resetpassword;
    private Button btn_yes;
    private TextView login;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
        dbHelper = new DBHelper(this);
    }

    protected void init() {
        edit_register = (EditText) findViewById(R.id.et_regist_user);

        edit_setpassword = (EditText) findViewById(R.id.et_regist_password);

        edit_resetpassword = (EditText) findViewById(R.id.et_regist_repassword);

        btn_yes = (Button) findViewById(R.id.bt_regist);

        login=findViewById(R.id.text_tologin);

        //监听内容改变
        edit_register.addTextChangedListener(this);
        edit_setpassword.addTextChangedListener(this);
        edit_resetpassword.addTextChangedListener(this);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAdd();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        // 判断两个输入框是否有内容
        if ((edit_register.getText().toString().length()>0)
                && (edit_setpassword.getText().toString().length()>0)
                && (edit_resetpassword.getText().toString().length()>0)){
            // 按钮可以点击了
            btn_yes.setEnabled(true);
        }else {
            btn_yes.setEnabled(false);
        }
    }

    public void userAdd(){
        if (CheckDataInDBorNot(edit_register.getText().toString())) {
            Toast.makeText(this, "该用户名已被注册，注册失败", Toast.LENGTH_SHORT).show();
        } else {
            if (edit_setpassword.getText().toString().trim().equals(edit_resetpassword.getText().toString())) {
                registUserInfo(edit_register.getText().toString(), edit_setpassword.getText().toString());
                Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "两次输入密码不同，请重新输入！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 利用sql创建嵌入式数据库进行注册访问
     */
    private void registUserInfo(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        db.insert("user", null, values);
        db.close();
    }

    /**
     * 检验用户名是否已经注册
     */
    public boolean CheckDataInDBorNot(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from user where username =?";
        Cursor cursor = db.rawQuery(Query, new String[]{value});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

}
