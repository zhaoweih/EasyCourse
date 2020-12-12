package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonSyntaxException;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeCourseDetailChapterAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.GetUnit;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author
 * @description 推荐课程-章节列表
 * @time 2019/3/18 16:49
 */
public class HomeRecommandChapterListActivity extends BaseActivity {
    @BindView(R.id.ahrcl_empty_view)
    LinearLayout ahrcl_empty_view;
    @BindView(R.id.ahrcl_recyclerView)
    RecyclerView ahrcl_recyclerView;
    @BindView(R.id.ahrcl_titleLayout)
    TitleLayout ahrcl_titleLayout;
    private int mcourseId;
    private HomeCourseDetailChapterAdapter mHomeCourseDetailChapterAdapter;
    private List<GetUnit> getUnitList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_recommand_chapter_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mcourseId = intent.getIntExtra("courseId", 0);
        ahrcl_titleLayout.setTitle(intent.getStringExtra("courseName") + "章节列表");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ahrcl_recyclerView.setLayoutManager(layoutManager);
        initAdapter(mcourseId);
    }

    private void initAdapter(int courseId) {
        HttpUtil.sendGetRequest(Constant.GET_UNIT_URL + "?course_id=" + courseId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                //RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                Log.v("tanxiiii", restResponse.getPayload().toString());
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    getUnitList.clear();
                    try {
                        getUnitList = JSON.parseArray(restResponse.getPayload().toString(), GetUnit.class);
                     /*   getUnitList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<GetUnit>>() {
                        }.getType()));*/
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        Log.v("tanxiiii", e.toString());
                    }
                    Log.v("tanxiiii", "lsSize():" + getUnitList.size());
                    runOnUiThread(() -> {
                        showAdapter(courseId);
                    });
                }
            }
        });
    }

    private void showAdapter(int course_id) {
        if (getUnitList.size() == 0) {
            ahrcl_empty_view.setVisibility(View.VISIBLE);
        } else {
            ahrcl_empty_view.setVisibility(View.GONE);
        }
        mHomeCourseDetailChapterAdapter = new HomeCourseDetailChapterAdapter(getUnitList);
        mHomeCourseDetailChapterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(HomeRecommandChapterListActivity.this, HomeCourseDetailChapterListContentActivity.class);
                intent.putExtra("unit_title", mHomeCourseDetailChapterAdapter.getUnitSelectedTitleArray()[position]);
                intent.putExtra("unit_id", mHomeCourseDetailChapterAdapter.getUnitSelectedIdArray()[position]);
                intent.putExtra("course_id", course_id);
                intent.putExtra("reading_mode", "reading");
                startActivity(intent);

            }
        });
        ahrcl_recyclerView.setAdapter(mHomeCourseDetailChapterAdapter);

    }

}
