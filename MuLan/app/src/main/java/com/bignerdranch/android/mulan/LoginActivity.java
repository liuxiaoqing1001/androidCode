package com.bignerdranch.android.mulan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.mulan.helper.DBHelper;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private EditText user;
    private EditText password;
    //    private TextView forgotPwd;
    private TextView register;
    private Button loginBtn;
    private ImageView rightArm;
    private ImageView leftArm;
    private ImageView leftHand;
    private ImageView rightHand;
    private DBHelper dbHelper;
//    private static final String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews(){
        user = findViewById(R.id.et_user);
        password = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.bt_login);
        leftArm = findViewById(R.id.iv_left_arm);
        rightArm = findViewById(R.id.iv_right_arm);
        leftHand = findViewById(R.id.iv_left_hand);
        rightHand = findViewById(R.id.iv_right_hand);
//        forgotPwd=findViewById(R.id.text_forgot_pwd);
        register=findViewById(R.id.text_register);

        dbHelper = new DBHelper(this);

        //监听内容改变
        user.addTextChangedListener(this);
        password.addTextChangedListener(this);

        //监听EditText的焦点变化
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == true){
                    close();
                }else {
                    open();
                }
            }
        });

        //登录按钮被点击
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readUserInfo();
            }
        });

//        forgotPwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
//                startActivity(intent);
//            }
//        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
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
        if ((user.getText().toString().length()>0) && (password.getText().toString().length()>0)){
            // 按钮可以点击了
            loginBtn.setEnabled(true);
        }else {
            loginBtn.setEnabled(false);
        }
    }

    /**
     * 读取UserData.db中的用户信息
     * */
    protected void readUserInfo() {
        if (user.getText().toString().trim().equals("") | password.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请输入账号和密码！", Toast.LENGTH_SHORT).show();
        } else {
            if (login(user.getText().toString(), password.getText().toString())) {
                Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("Username", user.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 验证登录信息
     * */
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from user where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }


    private void close(){
        RotateAnimation rAnim = new RotateAnimation(0, 170,
                Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0f);
        rAnim.setDuration(500);
        rAnim.setFillAfter(true);
        leftArm.startAnimation(rAnim);
        RotateAnimation rAnim1 = new RotateAnimation(0, -170,
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f);
        rAnim1.setDuration(500);
        rAnim1.setFillAfter(true);
        rightArm.startAnimation(rAnim1);
        TranslateAnimation tAnim = (TranslateAnimation)
                AnimationUtils.loadAnimation(this,R.anim.hand_down );
        leftHand.startAnimation(tAnim);
        rightHand.startAnimation(tAnim);
    }

    private void open(){
        RotateAnimation rAnim = new RotateAnimation(170, 0,
                Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0f);
        rAnim.setDuration(500);
        rAnim.setFillAfter(true);
        leftArm.startAnimation(rAnim);
        RotateAnimation rAnim1 = new RotateAnimation(-170, 0,
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f);
        rAnim1.setDuration(500);
        rAnim1.setFillAfter(true);
        rightArm.startAnimation(rAnim1);
        TranslateAnimation tAnim = (TranslateAnimation)
                AnimationUtils.loadAnimation(this,R.anim.hand_up );
        leftHand.startAnimation(tAnim);
        rightHand.startAnimation(tAnim);
    }
}
