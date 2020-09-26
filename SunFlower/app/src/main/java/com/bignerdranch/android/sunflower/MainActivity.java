package com.bignerdranch.android.sunflower;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //跳过倒计时提示5秒
    private int recLen = 5;
    private Button mBtn;
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //flag 必须要设置的变量
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        initView();
        //等待时间一秒，停顿时间一秒
        timer.schedule(task, 1000, 1000);

         //正常情况下不点击跳过
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                //跳转到首界面
//                Intent intent =new Intent(MainActivity.this, LoginActivity.class) ;
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
            //延迟5S后发送handler信息
        }, 5000);

    }

    private void initView() {
        //跳过
        mBtn = findViewById(R.id.btn_skip);
        //监听跳过
        mBtn.setOnClickListener(this);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recLen--;
                    //在控件上显示距离跳转的剩余时间
                    mBtn.setText("跳过 " + recLen);
                    if (recLen < 0) {
                        timer.cancel();
                        //倒计时到0隐藏字体
                        mBtn.setVisibility(View.GONE);
                    }
                }
            });
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip:
                //发生点击事件时直接跳转到首界面
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                break;
            default:
                break;
        }
    }
}