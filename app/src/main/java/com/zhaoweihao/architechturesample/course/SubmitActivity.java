package com.zhaoweihao.architechturesample.course;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.Submit;

public class SubmitActivity extends AppCompatActivity implements SubmitContract.View{

    private SubmitContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        // 配置presenter
        new SubmitPresenter(this, this);

        /**
         * tecId : 26
         * teacherId : 20151120
         * courseName : 牛津和爱因斯坦的搏斗
         * teacherName : 赵威豪
         * password : 123456
         * description : 讲述牛津和爱因斯坦的斗争
         */
        int tecId = 26;
        String teacherId = "20151120";
        String courseName = "牛津和爱因斯坦的搏斗";
        String teacherName = "赵威豪";
        String password = "123456";
        String description = "讲述牛津和爱因斯坦的斗争";

        Submit submit = new Submit();
        submit.setTecId(tecId);
        submit.setTeacherId(teacherId);
        submit.setCourseName(courseName);
        submit.setTeacherName(teacherName);
        submit.setDescription(description);
        submit.setPassword(password);
        // 交给presenter去进行网络请求，各自负责的功能清晰
        presenter.submit(submit);


    }

    @Override
    public void setPresenter(SubmitContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {

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
            } else  {
                Toast.makeText(this, "提交课程失败", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
