package com.zhaoweihao.architechturesample.course;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.quiz.QuizActivity;
import com.zhaoweihao.architechturesample.vote.ShowActivity;

import org.litepal.crud.DataSupport;

public class CourseManagerActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt_activity_course_manager_NotiList,bt_activity_course_manager_StudentList;
    private int courseId;
    private Intent intent;
    Boolean isTeacherFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_manager);
        initViews();
        getSupportActionBar().setTitle("课程相关信息");
        courseId = getIntent().getIntExtra("courseId", 0);
    }
    public void initViews(){
        isTeacherFlag=true;
        if(DataSupport.findLast(User.class).getTeacherId()==null) isTeacherFlag=false;

        bt_activity_course_manager_NotiList= findViewById(R.id.bt_activity_course_manager_NotiList);
        bt_activity_course_manager_StudentList= findViewById(R.id.bt_activity_course_manager_StudentList);
        bt_activity_course_manager_NotiList.setOnClickListener(this);
        bt_activity_course_manager_StudentList.setOnClickListener(this);

        findViewById(R.id.bt_activity_course_manager_QuizList).setOnClickListener(this);
        findViewById(R.id.bt_activity_course_manager_SendTopic).setOnClickListener(this);
        findViewById(R.id.bt_activity_course_manager_QueryTopic).setOnClickListener(this);
        if(isTeacherFlag){
            findViewById(R.id.bt_activity_course_manager_SendTopic).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.bt_activity_course_manager_SendTopic).setVisibility(View.GONE);
        }
        findViewById(R.id.btn_to_vote).setOnClickListener(this);
        setSupportActionBar(findViewById(R.id.toolbar1));
    }
    @Override
    public void onClick(View view) {
        intent = null;
        switch (view.getId()) {
            case R.id.bt_activity_course_manager_StudentList:
              intent=new Intent(CourseManagerActivity.this,QuerySelectCourseActivity.class);
              intent.putExtra("courseId", courseId);
              startActivity(intent);
              break;
            case R.id. bt_activity_course_manager_NotiList:
                intent=new Intent(CourseManagerActivity.this,QueryNotiActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
                break;
            case R.id.bt_activity_course_manager_QuizList:
                intent = new Intent(CourseManagerActivity.this, QuizActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
                break;
            case R.id.bt_activity_course_manager_SendTopic:
                intent = new Intent(CourseManagerActivity.this, SendTopic.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
                break;
            case R.id.bt_activity_course_manager_QueryTopic:
                intent = new Intent(CourseManagerActivity.this, QueryTopicActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
                break;
            case R.id.btn_to_vote:
                intent = new Intent(CourseManagerActivity.this, ShowActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
                break;
                default:
        }
    }
}
