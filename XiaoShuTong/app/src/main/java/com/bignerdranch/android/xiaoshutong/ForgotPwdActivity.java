package com.bignerdranch.android.xiaoshutong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPwdActivity extends Activity implements View.OnClickListener{

    private EditText edit_modifyU, edit_modifyPwd, edit_reModifyPwd;
    private Button btn_modify;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        init();
        dbHelper = new DBHelper(this, "XST.db", null, 1);
    }

    protected void init() {
        edit_modifyU=findViewById(R.id.edit_modifyU);
        edit_modifyU.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i)) &&
                                    !Character.toString(source.charAt(i)).equals("_")) {
                                Toast.makeText(ForgotPwdActivity.this, "只能使用'_'、字母、数字、汉字注册！", Toast.LENGTH_SHORT).show();
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });
        edit_modifyU.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_modifyU.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_modifyU.getWindowToken(), 0);
                }
                return false;
            }
        });

        edit_modifyPwd=findViewById(R.id.edit_modifyPwd);
        edit_modifyPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edit_modifyPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String s = v.getText().toString();
                    //设置密码长度有问题，判断editText的输入长度需要重新理解
                    System.out.println(" v: ****** v :"+ s.length());
                    if (s.length() >= 6) {
                        System.out.println(" ****** s :"+ s.length());
                        edit_modifyPwd.clearFocus();
                        InputMethodManager imm =
                                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edit_modifyPwd.getWindowToken(), 0);
                    } else {
                        Toast.makeText(ForgotPwdActivity.this, "密码设置最少为6位！", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        edit_reModifyPwd=findViewById(R.id.edit_reModifyPwd);
        edit_reModifyPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edit_reModifyPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    edit_reModifyPwd.clearFocus();
                    InputMethodManager im =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(edit_reModifyPwd.getWindowToken(), 0);
                }
                return false;
            }
        });
        btn_modify = (Button) findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify:
                if (edit_modifyPwd.getText().toString().trim().equals(edit_reModifyPwd.getText().toString())) {
                    modifyUserInfo(edit_modifyU.getText().toString(),
                    edit_modifyPwd.getText().toString());
                    Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent register_intent = new Intent(ForgotPwdActivity.this,LoginActivity.class);
                    startActivity(register_intent);
                } else {
                    Toast.makeText(this, "两次输入密码不同，请重新输入！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void modifyUserInfo(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);
        db.update("user",values,"username=?",new String[]{username});
        db.close();
    }
}
