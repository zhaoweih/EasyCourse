package com.zhaoweihao.architechturesample.course;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhaoweihao.architechturesample.R;

public class CourseManagerActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt_activity_course_manager_NotiList,bt_activity_course_manager_StudentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_manager);
        initViews();
        getSupportActionBar().setTitle("课程相关信息");
    }
    public void initViews(){

        bt_activity_course_manager_NotiList=(Button)findViewById(R.id.bt_activity_course_manager_NotiList);
        bt_activity_course_manager_StudentList=(Button)findViewById(R.id.bt_activity_course_manager_StudentList);

        bt_activity_course_manager_NotiList.setOnClickListener(this);
        bt_activity_course_manager_StudentList.setOnClickListener(this);
        setSupportActionBar(findViewById(R.id.toolbar1));
    }
    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        switch (view.getId()) {
            case R.id.bt_activity_course_manager_StudentList:
              Intent intent2=new Intent(CourseManagerActivity.this,QuerySelectCourseActivity.class);
              intent2.putExtra("courseId", intent.getIntExtra("courseId",0));
              startActivity(intent2);
              break;
            case R.id. bt_activity_course_manager_NotiList:
                Intent intent3=new Intent(CourseManagerActivity.this,QueryNotiActivity.class);
                intent3.putExtra("courseId", intent.getIntExtra("courseId",0));
                startActivity(intent3);
                break;
        }
    }
}
