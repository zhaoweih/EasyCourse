package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.HomeCourseMoreCallRollStuActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseMoreQuizRankActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseMoreCallRollTecCreateActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseMoreLeaveListActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseMoreCourseNotiQueryActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseMoreTestActivity;
import com.zhaoweihao.architechturesample.activity.HomeCourseMoreVoteActivity;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author tanxinkui
 * @description 用于课程界面的更多
 * @time 2019/1/22 0:00
 */
public class HomeCourseDetailMoreLayout extends LinearLayout {
    @BindView(R.id.ahcdm_fl_rollCall)
    FrameLayout ahcdm_fl_rollCall;
    @BindView(R.id.ahcdm_fl_LeaveList)
    FrameLayout ahcdm_fl_LeaveList;
    @BindView(R.id.ahcdm_fl_courseNotify)
    FrameLayout ahcdm_fl_courseNotify;
    @BindView(R.id.ahcdm_fl_vote)
    FrameLayout ahcdm_fl_vote;
    @BindView(R.id.ahcdm_fl_rank)
    FrameLayout ahcdm_fl_rank;
    @BindView(R.id.ahcdm_fl_test)
    FrameLayout ahcdm_fl_test;

    public HomeCourseDetailMoreLayout(Context context, @Nullable AttributeSet attrs, int courseId) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_home_course_detail_more, this);
        ButterKnife.bind(this);
        enterRollCallSystem(courseId);
        enterLeaveListSystem(courseId);
        enterNotiSystem(courseId);
        enterVoteSystem(courseId);
        enterRanking(courseId);

        ahcdm_fl_test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HomeCourseMoreTestActivity.class);
                intent.putExtra("chapter_id", 4);
                getContext().startActivity(intent);
            }
        });
    }

    private void enterRollCallSystem(int courseId) {
        ahcdm_fl_rollCall.setOnClickListener(view -> {
            if (checkIsStu()) {
                Intent intent = new Intent(getContext(), HomeCourseMoreCallRollStuActivity.class);
                intent.putExtra("courseId", courseId);
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), HomeCourseMoreCallRollTecCreateActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    private void enterLeaveListSystem(int courseId) {
        ahcdm_fl_LeaveList.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HomeCourseMoreLeaveListActivity.class);
            intent.putExtra("courseId", courseId);
            getContext().startActivity(intent);
        });
    }

    private void enterNotiSystem(int courseId) {
        ahcdm_fl_courseNotify.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HomeCourseMoreCourseNotiQueryActivity.class);
            intent.putExtra("courseId", courseId);
            getContext().startActivity(intent);
        });
    }
    private void enterVoteSystem(int courseId){
        ahcdm_fl_vote.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HomeCourseMoreVoteActivity.class);
            intent.putExtra("courseId", courseId);
            getContext().startActivity(intent);
        });
    }
    private void enterRanking(int courseId){
        ahcdm_fl_rank.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HomeCourseMoreQuizRankActivity.class);
            intent.putExtra("courseId", courseId);
            getContext().startActivity(intent);
        });
    }
    public Boolean checkIsStu() {
        User user = DataSupport.findLast(User.class);
        Boolean isStudent = true;
        if (user.getTeacherId() != null) {
            isStudent = false;
        }
        return isStudent;
    }
}
