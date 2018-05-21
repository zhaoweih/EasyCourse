package com.zhaoweihao.architechturesample.course;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

/**
 * 课程管理界面
 */

public class CourseActivity extends AppCompatActivity {

    private Button submit,query,select,selectByStuId,selectByCourseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        // 初始化控件
        initViews();
        // 设置标题
        getSupportActionBar().setTitle("课程管理");

        User user = DataSupport.findLast(User.class);

        if (user == null)
            // 用户没有登录
            return;

        if (user.getTeacherId() == null)
            // 隐藏提交课程按钮
            submit.setVisibility(View.INVISIBLE);

        if (user.getStudentId() == null)
            // 隐藏选课按钮
            select.setVisibility(View.INVISIBLE);

        // 各个按钮点击事件

    }

    private void initViews() {
        submit = findViewById(R.id.btn_submit);
        query = findViewById(R.id.btn_query);
        select = findViewById(R.id.btn_select);
        selectByStuId = findViewById(R.id.btn_query_select_by_stuid);
        selectByCourseId = findViewById(R.id.btn_query_select_by_courseid);
        setSupportActionBar(findViewById(R.id.toolbar));
    }
}
