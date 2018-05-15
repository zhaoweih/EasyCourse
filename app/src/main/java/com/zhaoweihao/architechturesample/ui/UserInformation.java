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

public class UserInformation extends AppCompatActivity implements View.OnClickListener {
    private ImageView  iv_year, iv_degree,iv_classnum,iv_studentnum,iv_teachernum;
    private TextView tv_school, tv_faculty, tv_year, tv_position, tv_degree,tv_sex;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
    }
    public void initView() {

        back = (Button) findViewById(R.id.btn_submit);

        iv_year = (ImageView) findViewById(R.id.iv_userinformationyear);
        iv_degree = (ImageView) findViewById(R.id.iv_userinformationdegree);
        iv_classnum = (ImageView) findViewById(R.id.iv_userinformationclassnum);
        iv_studentnum = (ImageView) findViewById(R.id.iv_userinformationstudentnum);
        iv_teachernum=(ImageView) findViewById(R.id.iv_userinformationteachernum);

        tv_school = (TextView) findViewById(R.id.tv_userinformationschool);
        tv_faculty = (TextView) findViewById(R.id.tv_userinformationfaculty);
        tv_year = (TextView) findViewById(R.id.tv_userinformationyear);
        tv_position = (TextView) findViewById(R.id.tv_userinformationposition);
        tv_degree = (TextView) findViewById(R.id.tv_userinformationdegree);
        tv_sex=(TextView)findViewById(R.id.tv_userinformationsex);}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_clearusername:

            break;

        }
    }
}
