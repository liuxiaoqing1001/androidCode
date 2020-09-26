package com.bignerdranch.android.sunflower.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.sunflower.R;
import com.bignerdranch.android.sunflower.entity.Course;

public class AddCourseActivity extends AppCompatActivity {

    private EditText course_name;
    private EditText course_weekStart;
    private EditText course_weekEnd;
    private EditText course_weekType;
    private EditText course_week;
    private EditText classes_lessonStart;
    private EditText classes_lessonEnd;
    private EditText teacher_name;
    private EditText class_place;
    private Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setFinishOnTouchOutside(false);
        Intent in =this.getIntent();
        System.out.println("......."+in.getStringExtra("Username"));

        course_name=findViewById(R.id.course_name);
        course_weekStart=findViewById(R.id.course_weekStart);
        course_weekEnd=findViewById(R.id.course_weekEnd);
        course_weekType=findViewById(R.id.course_weekType);
        course_week=findViewById(R.id.course_week);
        classes_lessonStart=findViewById(R.id.classes_lessonStart);
        classes_lessonEnd=findViewById(R.id.classes_lessonEnd);
        teacher_name=findViewById(R.id.teacher_name);
        class_place=findViewById(R.id.class_place);

        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String courseName=course_name.getText().toString();
                String courseWeekStart=course_weekStart.getText().toString();
                String courseWeekEnd=course_weekEnd.getText().toString();
                String courseWeekType=course_weekType.getText().toString();
                String courseWeek=course_week.getText().toString();
                String classesLessonStart=classes_lessonStart.getText().toString();
                String classesLessonEnd=classes_lessonEnd.getText().toString();
                String teacherName=teacher_name.getText().toString();
                String classPlace=class_place.getText().toString();

                if (courseName.equals("") || courseWeekStart.equals("") || courseWeekEnd.equals("")
                        || courseWeekType.equals("") || courseWeek.equals("")
                        || classesLessonStart.equals("") || classesLessonEnd.equals("")
                        || teacherName.equals("") || classPlace.equals("") ) {
                    Toast.makeText(AddCourseActivity.this, "课程信息未填写", Toast.LENGTH_SHORT).show();
                } else {
                    Course course = new Course(in.getStringExtra("Username"), Integer.valueOf(courseWeekStart),
                            Integer.valueOf(courseWeekEnd), Integer.valueOf(courseWeekType),
                            Integer.valueOf(courseWeek), Integer.valueOf(classesLessonStart),
                            Integer.valueOf(classesLessonEnd),courseName,teacherName,classPlace);
                    Intent intent = new Intent(AddCourseActivity.this, CourseActivity.class);
                    intent.putExtra("course", course);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
