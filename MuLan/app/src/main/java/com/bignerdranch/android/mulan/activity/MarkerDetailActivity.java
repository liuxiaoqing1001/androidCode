package com.bignerdranch.android.mulan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bignerdranch.android.mulan.R;
import com.bignerdranch.android.mulan.helper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MarkerDetailActivity extends AppCompatActivity {

    private ImageView detail_photo;
    private TextView detail_name;
    private TextView detail_address;
    private TextView detail_phoneNum;
    private Button add;
    private Intent in;
    private DBHelper dbHelper;
    private String tag;
    private String name;
    private String address;
    private String phoneNum;
    private Calendar calendar;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);

        dbHelper = new DBHelper(this);

        detail_photo=findViewById(R.id.detail_photo);
        detail_name=findViewById(R.id.detail_name);
        detail_address=findViewById(R.id.detail_address);
        detail_phoneNum=findViewById(R.id.detail_phoneNum);
        add=findViewById(R.id.bt_add);

        in = this.getIntent();
        tag=in.getStringExtra("tag");
        name=in.getStringExtra("name");
        address=in.getStringExtra("address");
        phoneNum=in.getStringExtra("phoneNum");
        if(tag.equals("scenic")){
            detail_photo.setImageResource(R.drawable.gymnasium);
            add.setText("    添加至我的行程    ");
//            detail_phoneNum.setText("无");
        }else if(tag.equals("hotel")){
            detail_photo.setImageResource(R.drawable.bg6);
            add.setText("    预  约    ");
            detail_phoneNum.setText(phoneNum);
        }else if (tag.equals("restaurant")){
            detail_photo.setImageResource(R.drawable.bg7);
            add.setText("    预  约    ");
            detail_phoneNum.setText(phoneNum);
        }
        detail_name.setText(name);
        detail_address.setText(address);

        detail_phoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //申请拨打电话权限
                if(ContextCompat.checkSelfPermission(MarkerDetailActivity.this,
                        Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MarkerDetailActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    if(tag.equals("scenic")){
                        Toast.makeText(MarkerDetailActivity.this, "无联系电话", Toast.LENGTH_SHORT).show();
                    }else {
                        String phone = phoneNum;
                        //创建打电话的意图
                        Intent intent = new Intent();
                        //设置拨打电话的动作
                        intent.setAction(Intent.ACTION_CALL);
                        //设置拨打电话的号码
                        intent.setData(Uri.parse("tel:" + phone));
                        //开启打电话的意图
                        startActivity(intent);
                    }

                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出框，选择时间某年某月某日某时某分
                dialog();
            }
        });
    }

    //弹出对话框
    public void dialog() {
        calendar = Calendar.getInstance();
        DatePickerDialog dpdialog = new DatePickerDialog(MarkerDetailActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        // 更新EditText控件日期 小于10加0
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final TimePickerDialog tpdialog = new TimePickerDialog(MarkerDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR, i);
                calendar.set(Calendar.MINUTE, i1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time=format.format(calendar.getTime());

                if(tag.equals("scenic")){
                    add_plan();
                }else if(tag.equals("hotel")){
                    add_service();
                }else if (tag.equals("restaurant")){
                    add_service();
                }else {
                    Toast.makeText(MarkerDetailActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                }

            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        dpdialog.show();
        dpdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                tpdialog.show();
            }
        });
    }

    public void add_plan(){
        if (CheckDataInToPlans(in.getStringExtra("Username"),name,address,time)) {
                Toast.makeText(this, "该行程已存在！", Toast.LENGTH_SHORT).show();
        } else {
            addPlan(in.getStringExtra("Username"),name, address, time);
        }
    }

    public boolean CheckDataInToPlans(String username,String name,String address,String time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from plans where planname =? and username=? and address=? and times=?";
        Cursor cursor = db.rawQuery(Query, new String[]{name,username,address,time});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public void addPlan(String username,String name,String address,String time){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("planname", name);
        values.put("username",username);
        values.put("address", address);
        values.put("times", time);
        db.insert("plans", null, values);
        db.close();
        Toast.makeText(MarkerDetailActivity.this, "行程添加成功", Toast.LENGTH_SHORT).show();
    }

    public void add_service(){
        if (CheckDataInToService(in.getStringExtra("Username"),name,address,time,tag)) {
            Toast.makeText(this, "该预定已存在！", Toast.LENGTH_SHORT).show();
        } else {
            addService(in.getStringExtra("Username"),name, address, time,tag);
        }
    }

    public boolean CheckDataInToService(String username,String name,String address,String time,String tag) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from service where servicename =? and username=? and address=? and times=? and types=?";
        Cursor cursor = db.rawQuery(Query, new String[]{name,username,address,time,tag});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public void addService(String username,String name,String address,String time,String tag){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("servicename", name);
        values.put("username",username);
        values.put("address", address);
        values.put("times", time);
        values.put("types",tag);
        db.insert("service", null, values);
        db.close();
        Toast.makeText(MarkerDetailActivity.this, "预定成功", Toast.LENGTH_SHORT).show();
    }

}
