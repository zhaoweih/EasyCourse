package com.zhaoweihao.architechturesample.course;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.leave.LeaveListActivity;
import com.zhaoweihao.architechturesample.quiz.QuizActivity;
import com.zhaoweihao.architechturesample.seat.EnterActivity;
import com.zhaoweihao.architechturesample.vote.ShowActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 4 testing UI
 */

public class CourseActivity extends AppCompatActivity {

    private int courseId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.check_stu)
    void checkStu() {
        Intent intent = new Intent(this, QuerySelectCourseActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    @OnClick(R.id.check_noti)
    void checkNoti() {
        Intent intent = new Intent(this, QueryNotiActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    @OnClick(R.id.to_vote)
    void toVote() {
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    @OnClick(R.id.to_discuss)
    void toDiscuss() {
        Intent intent = new Intent(this, QueryTopicActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    @OnClick(R.id.to_rank)
    void toRank() {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    @OnClick(R.id.to_leave)
    void toLeave() {
        Intent intent = new Intent(this, LeaveListActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    @OnClick(R.id.to_seat)
    void toSeat() {
        Intent intent = new Intent(this, EnterActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fav_new);

        courseId = getIntent().getIntExtra("courseId", 0);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
