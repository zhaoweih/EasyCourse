package com.zhaoweihao.architechturesample.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

public class UserInformation extends AppCompatActivity implements View.OnClickListener {
    private TextView  tv_year0, tv_degree0,tv_classnum0,tv_studentnum0,tv_teachernum0;
    private TextView tv_school, tv_faculty, tv_year, tv_position, tv_degree,tv_sex,tv_studentid,tv_teacherid,tv_name,tv_classnum;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        initView();
    }
    public void initView() {

        back = (ImageView) findViewById(R.id.iv_userinformationtitle);
        User user3 = DataSupport.findLast(User.class);

        tv_year0 = (TextView) findViewById(R.id.tv_userinformationyear0);
        tv_degree0 = (TextView) findViewById(R.id.tv_userinformationdegree0);
        tv_classnum0 = (TextView) findViewById(R.id.tv_userinformationclassnum0);
        tv_studentnum0 = (TextView) findViewById(R.id.tv_userinformationstudentnum0);
        tv_teachernum0=(TextView) findViewById(R.id.tv_userinformationteachernum0);

        tv_school = (TextView) findViewById(R.id.tv_userinformationschool);
        tv_faculty = (TextView) findViewById(R.id.tv_userinformationfaculty);
        tv_year = (TextView) findViewById(R.id.tv_userinformationyear);
        tv_position = (TextView) findViewById(R.id.tv_userinformationposition);
        tv_degree = (TextView) findViewById(R.id.tv_userinformationdegree);
        tv_studentid=(TextView)findViewById(R.id.tv_userinformationstudentnum);
        tv_sex=(TextView)findViewById(R.id.tv_userinformationsex);
        tv_teacherid=(TextView)findViewById(R.id.tv_userinformationteachernum);
        tv_name=(TextView)findViewById(R.id.tv_userinformationname);
        tv_classnum=(TextView)findViewById(R.id.tv_userinformationclassnum);

        tv_faculty.setText(user3.getDepartment());
        tv_school.setText(user3.getSchool());
        tv_name.setText(user3.getName());
        if(user3.getSex()==1){tv_sex.setText("女");}else {tv_sex.setText("男");}

        if(user3.getDate().equals("000000")){

            tv_teachernum0.setVisibility(View.VISIBLE);
            tv_teacherid.setVisibility(View.VISIBLE);

            tv_position.setText("老师");
            tv_teacherid.setText(user3.getTeacherId());

            tv_studentnum0.setVisibility(View.GONE);
            tv_classnum0.setVisibility(View.GONE);
            tv_degree0.setVisibility(View.GONE);
            tv_year0.setVisibility(View.GONE);

            tv_studentid.setVisibility(View.GONE);
            tv_classnum.setVisibility(View.GONE);
            tv_degree.setVisibility(View.GONE);
            tv_year.setVisibility(View.GONE);

        }else{
            tv_studentnum0.setVisibility(View.VISIBLE);
            tv_classnum0.setVisibility(View.VISIBLE);
            tv_degree0.setVisibility(View.VISIBLE);
            tv_year0.setVisibility(View.VISIBLE);

            tv_studentid.setVisibility(View.VISIBLE);
            tv_classnum.setVisibility(View.VISIBLE);
            tv_degree.setVisibility(View.VISIBLE);
            tv_year.setVisibility(View.VISIBLE);

            tv_position.setText("学生");
            tv_studentid.setText(user3.getStudentId());
            tv_classnum.setText(user3.getClassId());
            tv_year.setText(user3.getDate());
            if(user3.getEducation()==1){
            tv_degree.setText("本科/专科");
            }else{
                tv_degree.setText("研究生");}

            tv_teachernum0.setVisibility(View.GONE);
            tv_teacherid.setVisibility(View.GONE);

        }

        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_userinformationtitle:
            finish();
            break;

        }
    }
}
