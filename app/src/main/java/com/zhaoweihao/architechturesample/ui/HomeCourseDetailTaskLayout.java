package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.ChatSystemActivity_;
import com.zhaoweihao.architechturesample.activity.ChatWithWSActivity_;
import com.zhaoweihao.architechturesample.activity.HomeCourseTaskTalkTopicQueryActivity;
import com.zhaoweihao.architechturesample.util.Constant;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author tanxinkui
 * @description 用于课程的任务页面
 * @time 2019/1/21 23:36
 */
public class HomeCourseDetailTaskLayout extends LinearLayout {
    @BindView(R.id.ahcdt_fl_homework)
    FrameLayout ahcdt_fl_homework;
    @BindView(R.id.ahcdt_fl_talk)
    FrameLayout ahcdt_fl_talk;
    @BindView(R.id.ahcdt_fl_exam)
    FrameLayout ahcdt_fl_exam;
    @BindView(R.id.ahcdt_fl_chat)
    FrameLayout ahcdt_fl_chat;

    private Context context;
    private Integer courseID;

    @OnClick(R.id.ahcdt_fl_chat)
    void ahcdt_fl_chat() {
        ChatWithWSActivity_.intent(context)
                .extra("username","班级群聊")
                .extra(Constant.COURSE_ID, courseID)
                .start();

    }


    public HomeCourseDetailTaskLayout(Context context, @Nullable AttributeSet attrs, int courseId) {
        super(context, attrs);
        this.context = context;
        this.courseID = courseId;
        LayoutInflater.from(context).inflate(R.layout.activity_home_course_detail_task, this);
        ButterKnife.bind(this);
        init(courseId);
    }

    public void init(int courseId) {
        ahcdt_fl_talk.setOnClickListener(view -> taskActivity(courseId));
    }

    private void taskActivity(int courseId) {
        Intent intent = new Intent(getContext(), HomeCourseTaskTalkTopicQueryActivity.class);
        intent.putExtra("courseId", courseId);
        getContext().startActivity(intent);
    }

}
