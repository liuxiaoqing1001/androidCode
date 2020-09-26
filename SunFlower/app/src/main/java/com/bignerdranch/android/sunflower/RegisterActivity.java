package com.bignerdranch.android.sunflower;

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

import androidx.annotation.Nullable;

import com.bignerdranch.android.sunflower.helper.DBHelper;

public class RegisterActivity extends Activity implements TextWatcher {


    private EditText edit_register, edit_setpassword, edit_resetpassword;
    private Button btn_yes;
    private TextView login;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        dbHelper = new DBHelper(this);
    }

    protected void init() {
//        dbHelper = new DBHelper(this, "sunflower.db", null, 1);
        edit_register = (EditText) findViewById(R.id.et_regist_user);
//        edit_register.setFilters(new InputFilter[]{
//                new InputFilter() {
//                    @Override
//                    public CharSequence filter(CharSequence source, int start, int end,
//                                               Spanned dest, int dstart, int dend) {
//                        for (int i = start; i < end; i++) {
//                            if (!Character.isLetterOrDigit(source.charAt(i)) &&
//                                    !Character.toString(source.charAt(i)).equals("_")) {
//                                Toast.makeText(RegisterActivity.this, "只能使用'_'、字母、数字、汉字注册！", Toast.LENGTH_SHORT).show();
//                                return "";
//                            }
//                        }
//                        return null;
//                    }
//                }
//        });
//        edit_register.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    edit_register.clearFocus();
//                    InputMethodManager imm =
//                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(edit_register.getWindowToken(), 0);
//                }
//                return false;
//            }
//        });
        edit_setpassword = (EditText) findViewById(R.id.et_regist_password);
//        edit_setpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        edit_setpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    String s = v.getText().toString();
//                    //设置密码长度有问题，判断editText的输入长度需要重新理解
//                    System.out.println(" v: ****** v :"+ s.length());
//                    if (s.length() >= 6) {
//                        System.out.println(" ****** s :"+ s.length());
//                        edit_setpassword.clearFocus();
//                        InputMethodManager imm =
//                                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(edit_setpassword.getWindowToken(), 0);
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "密码设置最少为6位！", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                return false;
//            }
//        });
        edit_resetpassword = (EditText) findViewById(R.id.et_regist_repassword);
//        edit_resetpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        edit_resetpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    edit_resetpassword.clearFocus();
//                    InputMethodManager im =
//                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    im.hideSoftInputFromWindow(edit_resetpassword.getWindowToken(), 0);
//                }
//                return false;
//            }
//        });
        btn_yes = (Button) findViewById(R.id.bt_regist);
//        btn_yes.setOnClickListener(this);

        login=findViewById(R.id.text_tologin);
//        login.setOnClickListener(this);

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
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "两次输入密码不同，请重新输入！", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_regist:
//
//                break;
//            case R.id.text_tologin:
//
//                break;
//            default:
//                break;
//        }
//    }


    //利用sql创建嵌入式数据库进行注册访问

    private void registUserInfo(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        db.insert("user", null, values);
        db.close();
    }


     //检验用户名是否已经注册

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


