package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 用于显示课程简单的信息
 * @time 2019/3/18 13:30
 */
public class CourseSimpleInfoLayout extends LinearLayout {
    @BindView(R.id.lcsi_tv_course_name)
    TextView lcsi_tv_course_name;
    @BindView(R.id.lcsi_tv_teacher_id)
    TextView lcsi_tv_teacher_id;
    @BindView(R.id.lcsi_tv_mun)
    TextView lcsi_tv_mun;
    @BindView(R.id.lcsi_tv_password)
    TextView lcsi_tv_password;
    @BindView(R.id.lcsi_tv_description)
    TextView lcsi_tv_description;
    @BindView(R.id.lcsi_fl_close)
    FrameLayout lcsi_fl_close;
    @BindView(R.id.lcsi_fl_main)
    FrameLayout lcsi_fl_main;
    @BindView(R.id.lcsi_ly_main)
    LinearLayout lcsi_ly_main;
    @BindView(R.id.lcsi_iv_password)
    ImageView lcsi_iv_password;

    public CourseSimpleInfoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_course_simple_info, this);
        ButterKnife.bind(this);
    }

    public FrameLayout getLcsi_fl_close() {
        return lcsi_fl_close;
    }

    public void initWithSimpleInfo(String course_name, String teacher_id, String selected_num, String password, String description) {
        lcsi_tv_course_name.setText(course_name);
        lcsi_tv_teacher_id.setText("教师编号：" + teacher_id);
        lcsi_tv_mun.setText("选课人数：" + selected_num);
        lcsi_tv_password.setText("选课密码：" + password);
        lcsi_tv_password.setVisibility(VISIBLE);
        lcsi_iv_password.setVisibility(VISIBLE);
        lcsi_tv_description.setText("课程描述：" + description);
    }

    public void initWithoutPasswordSimpleInfo(String course_name, String teacher_id, String selected_num, String description) {
        lcsi_tv_course_name.setText(course_name);
        lcsi_tv_teacher_id.setText("教师编号：" + teacher_id);
        lcsi_tv_mun.setText("选课人数：" + selected_num);
        lcsi_tv_password.setVisibility(GONE);
        lcsi_iv_password.setVisibility(GONE);
        lcsi_tv_description.setText("课程描述：" + description);
    }
}
