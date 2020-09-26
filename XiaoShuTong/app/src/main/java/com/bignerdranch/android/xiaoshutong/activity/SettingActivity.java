package com.bignerdranch.android.xiaoshutong.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.xiaoshutong.DBHelper;
import com.bignerdranch.android.xiaoshutong.ForgotPwdActivity;
import com.bignerdranch.android.xiaoshutong.LoginActivity;
import com.bignerdranch.android.xiaoshutong.R;
import com.bignerdranch.android.xiaoshutong.RegisterActivity;

public class SettingActivity extends Activity implements View.OnClickListener{

    private TextView text_switch_user;
    private TextView text_modify_pwd;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        //ActionBar工具栏设置
//        Toolbar toolbar = findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text_switch_user=findViewById(R.id.text_switch_user);
        text_modify_pwd=findViewById(R.id.text_modify_pwd);

        text_switch_user.setOnClickListener(this);
        text_modify_pwd.setOnClickListener(this);

        dbHelper = new DBHelper(this, "XST.db", null, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_switch_user:
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.text_modify_pwd:
                Intent intent1 = new Intent(SettingActivity.this, ForgotPwdActivity.class);
                startActivity(intent1);
                break;
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}