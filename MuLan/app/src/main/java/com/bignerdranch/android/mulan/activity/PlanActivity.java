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
import android.view.Menu;
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
import com.bignerdranch.android.mulan.entity.Plans;
import com.bignerdranch.android.mulan.helper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanActivity extends AppCompatActivity {

    private DBHelper mDBHelper;
    private ListView listView;
    private TextView tv_plans;
    private List<Map<String,String>> list;
    private SimpleAdapter simpleAdapter;
    private ArrayList<Plans> plans;
    private int listview_position;
    private Calendar calendar;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        mDBHelper = new DBHelper(this);
        tv_plans=findViewById(R.id.tv_plans);
        listView = findViewById(R.id.plan_list);
        syc();
        tv_plans.setOnClickListener(new View.OnClickListener() {
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
        plans = getPlans();
        list = new ArrayList<Map<String, String>>();
        for (int i = 0;i < plans.size() ;i++) {
            Map<String, String> map = new HashMap<>();
            map.put("planname",plans.get(i).getPlanname());
            map.put("username",plans.get(i).getUsername());
            map.put("address",plans.get(i).getAddress());
            map.put("times", plans.get(i).getTimes());
            list.add(map);
        }
        simpleAdapter = new SimpleAdapter(this,list, R.layout.list_item,
                new String[]{"planname","times"},new int[]{R.id.name,R.id.times});

        listView.setAdapter(simpleAdapter);
    }

    private ArrayList<Plans> getPlans(){
        ArrayList<Plans> plans = new ArrayList<>();
        Intent in = this.getIntent();
        System.out.println(in.getStringExtra("Username"));
        Cursor cursor = mDBHelper.getReadableDatabase().query("plans",null,
                "username=?",new String[]{in.getStringExtra("Username")},
                null,null,null);
        while(cursor.moveToNext()){
            Plans plan = new Plans();
            //利用getColumnIndex：String 来获取列的下标，再根据下标获取cursor的值
            plan.setPlanname(cursor.getString(cursor.getColumnIndex("planname")));
            plan.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            plan.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            plan.setTimes(cursor.getString(cursor.getColumnIndex("times")));
            plans.add(plan);
        }
        return plans;
    }

    public void deleteFromPlans(){
        SQLiteDatabase db =  mDBHelper.getWritableDatabase();
        db.execSQL("delete from plans where planname =? and username=? and address=? and times=?",
                new String[] {plans.get(listview_position).getPlanname(),
                        plans.get(listview_position).getUsername(),
                        plans.get(listview_position).getAddress(),
                        plans.get(listview_position).getTimes()});
        db.close();
        Toast.makeText(PlanActivity.this, "行程删除成功", Toast.LENGTH_SHORT).show();
    }

    public void updateFromPlans(){
        SQLiteDatabase db=mDBHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("times",time);
        db.update("plans",values,"planname =? and username=? and address=? and times=?",
                new String[]{plans.get(listview_position).getPlanname(),
                        plans.get(listview_position).getUsername(),
                        plans.get(listview_position).getAddress(),
                        plans.get(listview_position).getTimes()});
        db.close();
        Toast.makeText(PlanActivity.this, "行程时间修改成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                selectTime();
                break;
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(PlanActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定删除行程？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFromPlans();
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
        DatePickerDialog dpdialog = new DatePickerDialog(PlanActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        // TODO Auto-generated method stub
                        // 更新EditText控件日期 小于10加0
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        final TimePickerDialog tpdialog = new TimePickerDialog(PlanActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR, i);
                calendar.set(Calendar.MINUTE, i1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time=format.format(calendar.getTime());
                updateFromPlans();
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
