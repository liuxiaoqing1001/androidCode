package com.bignerdranch.android.sunflower.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bignerdranch.android.sunflower.R;
import com.bignerdranch.android.sunflower.helper.DBHelper;
import com.bignerdranch.android.sunflower.entity.Course;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    //星期几
    private RelativeLayout weekDay;

    private Toolbar toolbar;

    //SQLite Helper类
    private DBHelper mDBHelper ;

    int currentCNum = 0;
    int maxCNum = 0;

    private Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mDBHelper = new DBHelper(this);

        //工具条
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //从数据库读取数据
        initData();
    }

    //从数据库加载数据
    private void initData() {
        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
        SQLiteDatabase db =  mDBHelper.getWritableDatabase();
        in = this.getIntent();
        String sql = "SELECT * FROM course WHERE username =?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(in.getStringExtra("Username"))});
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setcId(cursor.getInt(cursor.getColumnIndex("cId")));
//                course.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                course.setWeekStart(cursor.getInt(cursor.getColumnIndex("weekStart")));
                course.setWeekEnd(cursor.getInt(cursor.getColumnIndex("weekEnd")));
                course.setWeekType(cursor.getInt(cursor.getColumnIndex("weekType")));
                course.setWeek(cursor.getInt(cursor.getColumnIndex("week")));
                course.setLessonStart(cursor.getInt(cursor.getColumnIndex("lessonStart")));
                course.setLessonEnd(cursor.getInt(cursor.getColumnIndex("lessonEnd")));
                course.setcName(cursor.getString(cursor.getColumnIndex("cName")));
                course.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                course.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                coursesList.add(course);
            } while(cursor.moveToNext());
        }
        cursor.close();

        //使用从数据库读取出来的课程信息来加载课程表视图
        for (Course course : coursesList) {
            createLeftView(course);
            createCourseView(course);
        }
    }

    //保存数据到数据库
    private void saveData(Course course) {
        SQLiteDatabase sqLiteDatabase =  mDBHelper.getWritableDatabase();
        in = this.getIntent();
        sqLiteDatabase.execSQL
                ("insert into course(username ,weekStart ,weekEnd ,weekType ,week ,lessonStart ,lessonEnd ,cName ,teacher ,place ) " +
                                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[] {in.getStringExtra("Username"),
                        course.getWeekStart()+"", course.getWeekEnd()+"", course.getWeekType()+"", course.getWeek()+"",
                        course.getLessonStart()+"", course.getLessonEnd()+"",course.getcName(),course.getTeacher(),course.getPlace()}
                );
    }

    //创建"第几节数"视图
    private void createLeftView(Course course) {
        int endNum = course.getLessonEnd();
        if (endNum > maxCNum) {
            for (int i = 0; i < endNum- maxCNum; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
                view.setLayoutParams(params);

                TextView text = view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCNum));

                LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
                leftViewLayout.addView(view);
            }
            maxCNum = endNum;
        }
    }

    //创建单个课程视图
    private void createCourseView(final Course course) {
        int week = course.getWeek();
        if ((week < 1 || week > 7) || course.getLessonStart() > course.getLessonEnd()){
            Toast.makeText(this, "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        } else {
            int weekId = 0;
            switch (week) {
                case 1: weekId = R.id.monday; break;
                case 2: weekId = R.id.tuesday; break;
                case 3: weekId = R.id.wednesday; break;
                case 4: weekId = R.id.thursday; break;
                case 5: weekId = R.id.friday; break;
                case 6: weekId = R.id.saturday; break;
                case 7: weekId = R.id.weekday; break;
            }
            weekDay = findViewById(weekId);

            int height = 180;
            final View v = LayoutInflater.from(this).inflate(R.layout.course_card, null); //加载单个课程布局
            v.setY(height * (course.getLessonStart()-1)); //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getLessonEnd()-course.getLessonStart()+1)*height - 8); //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            int weekType=course.getWeekType();
            String type="";
            if(weekType==2){
                type="单";
            }else if(weekType==3){
                type="双";
            }
            text.setText(course.getcName()+ "\n\n" + course.getTeacher()+ "\n" +course.getWeekStart()+ "-"+course.getWeekEnd()
                    +type+ "\n" + course.getPlace()); //显示课程名
            weekDay.addView(v);
            //长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.setVisibility(View.GONE);//先隐藏
                    weekDay.removeView(v);//再移除课程视图
                    SQLiteDatabase sqLiteDatabase =  mDBHelper.getWritableDatabase();
                    sqLiteDatabase.execSQL("delete from course where cName = ?", new String[] {course.getcName()});
                    return true;
                }
            });
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu_course_add:
                Intent intent = new Intent(this, AddCourseActivity.class);
                intent.putExtra("Username", in.getStringExtra("Username"));
                startActivityForResult(intent, 0);
                break;
//            case R.id.menu_about:
//                Intent intent1 = new Intent(this, AboutActivity.class);
//                startActivity(intent1);
//                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            //创建课程表左边视图(节数)
            createLeftView(course);
            //创建课程表视图
            createCourseView(course);
            //存储数据到数据库
            saveData(course);
        }
    }
}