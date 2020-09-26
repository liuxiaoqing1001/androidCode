package com.bignerdranch.android.mulan.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bignerdranch.android.mulan.R;
import com.bignerdranch.android.mulan.entity.Service;
import com.bignerdranch.android.mulan.helper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceActivity extends AppCompatActivity {

    private DBHelper mDBHelper;
    private ListView listView;
    private List<Map<String,String>> list;
    private ArrayList<Service> mServices;
    private TextView tv_service;
    private SimpleAdapter simpleAdapter;
    private int listview_position;
    private Calendar calendar;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        mDBHelper = new DBHelper(this);
        tv_service=findViewById(R.id.tv_service);
        listView = findViewById(R.id.service_list);
        syc();
        tv_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syc();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取 当前 显示的第一个Item  在数据中的位置
                int firstVisiblePosition = parent.getFirstVisiblePosition();
//                // 获取当前显示item  的个数
//                int childCount = parent.getChildCount();
                // 当前点击的Item  在当前显示的ItemL列表 中的位置
                listview_position=position - firstVisiblePosition ;

                return false;
            }
        });
        registerForContextMenu(listView);
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "删除");
                menu.add(0, 1, 1, "修改时间");
            }
        });
    }

    public void syc(){
        mServices = getService();
        list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < mServices.size() ; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("servicename", mServices.get(i).getServicename());
            map.put("username", mServices.get(i).getUsername());
            map.put("address", mServices.get(i).getAddress());
            map.put("times", mServices.get(i).getTimes());
            map.put("types", mServices.get(i).getTypes());
            list.add(map);
        }
        simpleAdapter = new SimpleAdapter(this,list, R.layout.list_item,
                new String[]{"servicename","times"},new int[]{R.id.name,R.id.times});
        listView.setAdapter(simpleAdapter);
    }

    private ArrayList<Service> getService(){
        ArrayList<Service> services = new ArrayList<>();
        Intent in = this.getIntent();
        System.out.println(in.getStringExtra("Username"));
        Cursor cursor = mDBHelper.getReadableDatabase().query("service",null,
                "username=?",new String[]{in.getStringExtra("Username")},
                null,null,null);
        while(cursor.moveToNext()){
            Service service = new Service();
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            service.setServicename(cursor.getString(cursor.getColumnIndex("servicename")));
            service.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            service.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            service.setTimes(cursor.getString(cursor.getColumnIndex("times")));
            service.setTypes(cursor.getString(cursor.getColumnIndex("types")));
            services.add(service);
        }
        return services;
    }

    public void deleteFromService(){
        SQLiteDatabase db =  mDBHelper.getWritableDatabase();
        db.execSQL("delete from service where servicename =? and username=? and address=? and times=? and types=?",
                new String[] {mServices.get(listview_position).getServicename(),
                        mServices.get(listview_position).getUsername(),
                        mServices.get(listview_position).getAddress(),
                        mServices.get(listview_position).getTimes(),
                        mServices.get(listview_position).getTypes()});
        db.close();
        Toast.makeText(ServiceActivity.this, "成功删除预约", Toast.LENGTH_SHORT).show();

    }

    public void updateFromService(){
        SQLiteDatabase db=mDBHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("times",time);
        db.update("service",values,"servicename =? and username=? and address=? and times=? and types=?",
                new String[]{mServices.get(listview_position).getServicename(),
                        mServices.get(listview_position).getUsername(),
                        mServices.get(listview_position).getAddress(),
                        mServices.get(listview_position).getTimes(),
                        mServices.get(listview_position).getTypes()});
        db.close();
        Toast.makeText(ServiceActivity.this, "修改预约时间成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                selectTime();
                break;
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(ServiceActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定删除预定？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFromService();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void selectTime() {
        calendar = Calendar.getInstance();
        DatePickerDialog dpdialog = new DatePickerDialog(ServiceActivity.this,
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

        final TimePickerDialog tpdialog = new TimePickerDialog(ServiceActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR, i);
                calendar.set(Calendar.MINUTE, i1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time=format.format(calendar.getTime());
                updateFromService();
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
}
