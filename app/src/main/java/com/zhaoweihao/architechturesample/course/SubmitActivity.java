package com.zhaoweihao.architechturesample.course;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.User;
import com.zhaoweihao.architechturesample.data.course.DeleteTopic;
import com.zhaoweihao.architechturesample.data.course.Submit;

import org.litepal.crud.DataSupport;

public class SubmitActivity extends AppCompatActivity implements SubmitContract.View{

    private SubmitContract.Presenter presenter;

    private TextView teacherId;

    private EditText courseName,teacherName,password,description;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        // 配置presenter
        new SubmitPresenter(this, this);

        initViews(null);

        /**
         * tecId : 26
         * teacherId : 20151120
         * courseName : 牛津和爱因斯坦的搏斗
         * teacherName : 赵威豪
         * password : 123456
         * description : 讲述牛津和爱因斯坦的斗争
         */
        com.zhaoweihao.architechturesample.database.User user = DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class);

        if (user == null) {
            return;
        }

        if (user.getTeacherId() == null) {
            Toast.makeText(this, "你不是老师身份", Toast.LENGTH_SHORT).show();
            return;
        }

        //toolbar.setTitle("提交课程");
        getSupportActionBar().setTitle("提交课程");

        teacherId.setText(user.getTeacherId());

        teacherName.setText(user.getName());

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.submit:
                    String courseNameText = courseName.getText().toString();
                    String teacherNameText = teacherName.getText().toString();
                    String descriptionText = description.getText().toString();
                    String passwordText = password.getText().toString();
                    if (courseNameText.equals("")||teacherNameText.equals("")||descriptionText.equals("")||passwordText.equals("")){
                        Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        AlertDialog alert = new AlertDialog.Builder(this)
                                .setIcon(R.drawable.warming)
                                .setTitle("温馨提示")
                                .setMessage("确定要提交课程吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确定按钮
                                    @Override//处理确定按钮点击事件
                                    public void onClick(DialogInterface dialog, int which) {
                                        Submit submit = new Submit();
                                        submit.setTecId(user.getUserId());
                                        submit.setTeacherId(user.getTeacherId());
                                        submit.setCourseName(courseNameText);
                                        submit.setTeacherName(teacherNameText);
                                        submit.setDescription(descriptionText);
                                        submit.setPassword(passwordText);
                                        // 交给presenter去进行网络请求，各自负责的功能清晰
                                        presenter.submit(submit);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();//对话框关闭。
                                    }
                                }).create();
                        alert.show();
                    }
                    break;
            }
            return true;
        });

    }

    @Override
    public void setPresenter(SubmitContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        teacherId = findViewById(R.id.teacher_id);
        courseName = findViewById(R.id.course_name);
        teacherName = findViewById(R.id.teacher_name);
        password = findViewById(R.id.password);
        description = findViewById(R.id.description);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showResult(Boolean status) {
        runOnUiThread(() -> {
            if (status) {
                Toast.makeText(this, "提交课程成功", Toast.LENGTH_SHORT).show();
                finish();
            } else  {
                Toast.makeText(this, "提交课程失败", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showLoadError() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_submit, menu);
        return true;
    }
}
