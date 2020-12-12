package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeCourseDetailChapterContentListAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowChapter;
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
 * @description 首页-课程-详细界面-章节-展示章节的列表
 * @time 2019/2/11 15:05
 */
public class HomeCourseDetailChapterListContentActivity extends BaseActivity {
    @BindView(R.id.lhcdcc_title_layout)
    TitleLayout lhcdcc_title_layout;

    @BindView(R.id.lhcdcc_recyclerView)
    RecyclerView lhcdcc_recyclerView;

    @BindView(R.id.lhcdc_fb_write_chapter_content)
    FloatingActionButton lhcdc_fb_write_chapter_content;

    @BindView(R.id.lhcdcc_empty_view)
    LinearLayout lhcdcc_empty_view;

    private HomeCourseDetailChapterContentListAdapter mHomeCourseDetailChapterContentListAdapter;

    QMUITipDialog deleteDialog;


    //private ArrayList<ShowChapter> showChapterList = new ArrayList<>();
    private List<ShowChapter> showChapterList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_detail_chapter_list_content);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        deleteDialog = new QMUITipDialog.Builder(HomeCourseDetailChapterListContentActivity.this)
                .setTipWord("正在删除章节...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeCourseDetailChapterListContentActivity.this);
        lhcdcc_recyclerView.setLayoutManager(layoutManager);
        lhcdcc_title_layout.setTitle(getIntent().getStringExtra("unit_title"));
        if (DataSupport.findLast(User.class).getStudentId() != null) {
            lhcdc_fb_write_chapter_content.setVisibility(View.INVISIBLE);
        }
        if (getIntent().getStringExtra("reading_mode").equals("reading")) {
            lhcdc_fb_write_chapter_content.setVisibility(View.INVISIBLE);
        }
        lhcdc_fb_write_chapter_content.setOnClickListener(v -> {
            Intent intent = new Intent(HomeCourseDetailChapterListContentActivity.this, HomeCourseDetailChapterAddContentActivity.class);
            intent.putExtra("unit_title", getIntent().getStringExtra("unit_title"));
            intent.putExtra("course_id", getIntent().getIntExtra("course_id", -1));
            intent.putExtra("unit_id", getIntent().getIntExtra("unit_id", -1));
            startActivity(intent);
        });
        if (DataSupport.findLast(User.class).getStudentId() != null) {
            request(getIntent().getIntExtra("unit_id", -1));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataSupport.findLast(User.class).getStudentId() == null) {
            request(getIntent().getIntExtra("unit_id", -1));
        }
    }

    private void showAdapter() {
        if (showChapterList.size() == 0) {
            lhcdcc_empty_view.setVisibility(View.VISIBLE);
        } else {
            lhcdcc_empty_view.setVisibility(View.GONE);
        }
        mHomeCourseDetailChapterContentListAdapter = new HomeCourseDetailChapterContentListAdapter(showChapterList);
        mHomeCourseDetailChapterContentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(HomeCourseDetailChapterListContentActivity.this, HomeCourseDetailChapterListContentDetailActivity.class);
                intent.putExtra("unit_title", getIntent().getStringExtra("unit_title"));
                intent.putExtra("reading_mode", getIntent().getStringExtra("reading_mode"));
                intent.putExtra("chapter_id", mHomeCourseDetailChapterContentListAdapter.getChapterId()[position]);
                intent.putExtra("course_id", mHomeCourseDetailChapterContentListAdapter.getCourseId()[position]);
                intent.putExtra("chapter_title", mHomeCourseDetailChapterContentListAdapter.getChapterTitle()[position]);
                intent.putExtra("unit_id", mHomeCourseDetailChapterContentListAdapter.getUnitId()[position]);
                intent.putExtra("chapter_video_url", mHomeCourseDetailChapterContentListAdapter.getChapterVideoUrl()[position]);
                startActivity(intent);

            }
        });
        mHomeCourseDetailChapterContentListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                new QMUIDialog.MessageDialogBuilder(HomeCourseDetailChapterListContentActivity.this)
                        .setTitle("确定要删除：" + getIntent().getStringExtra("unit_title"))
                        .setMessage("删除该节课所有内容！")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                deleteDialog.show();
                                deleteChapter(mHomeCourseDetailChapterContentListAdapter.getChapterId()[position]);
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });
        lhcdcc_recyclerView.setAdapter(mHomeCourseDetailChapterContentListAdapter);
    }

    private void deleteChapter(int chapterId) {
        HttpUtil.sendGetRequest(Constant.DELETE_CHAPTER_URL + "/" + chapterId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                deleteDialog.dismiss();
                runOnUiThread(() -> {
                    request(getIntent().getIntExtra("unit_id", -1));
                });
            }
        });
    }

    private void request(int unit_id) {
        HttpUtil.sendGetRequest(Constant.GET_CHAPTER_URL + "?unit_id=" + unit_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // RestResponse restResponse = new Gson().fromJson(response.body().string(), RestResponse.class);
                RestResponse restResponse = JSON.parseObject(response.body().string(), RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    showChapterList.clear();
                    showChapterList = JSON.parseArray(restResponse.getPayload().toString(), ShowChapter.class);
                    //showChapterList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<ShowChapter>>() {
                    //}.getType()));
                    runOnUiThread(() -> {
                        showAdapter();
                    });
                }

            }
        });

    }

    /*
    *   private void request(int course_id, String title) {
        LoadDialog.show();
        AddUnit addUnit = new AddUnit();
        addUnit.setCourse_id(course_id);
        addUnit.setTitle(title);
        addUnit.setTeacher_id(DataSupport.findLast(User.class).getTeacherId());
        addUnit.setUser_id(DataSupport.findLast(User.class).getUserId());
        String json = new Gson().toJson(addUnit);
        HttpUtil.sendPostRequest(Constant.ADD_UNIT_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoadDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LoadDialog.dismiss();
                initAdapter(course_id);
            }
        });
    }
    * */
    /*
    * private void showAdapter(int course_id) {
       mHomeCourseDetailChapterContentListAdapter = new HomeCourseDetailChapterAdapter(getUnitList);
       mHomeCourseDetailChapterContentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getContext(), "unit_id:" +mHomeCourseDetailChapterContentListAdapter.getUnitSelectedIdArray()[position], Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),HomeCourseDetailChapterAddContentActivity.class);
                intent.putExtra("unit_title",mHomeCourseDetailChapterAdapter.getUnitSelectedTitleArray()[position]);
                getContext().startActivity(intent);

            }
        });
       mHomeCourseDetailChapterContentListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (DataSupport.findLast(User.class).getStudentId() == null) {
                    new QMUIDialog.MessageDialogBuilder(getContext())
                            .setTitle("您选中："+mHomeCourseDetailChapterAdapter.getUnitSelectedTitleArray()[position])
                            .setMessage("可删除该单元所有内容或仅修改该单元的标题！")
                            .addAction("修改", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    modify_unit(course_id,mHomeCourseDetailChapterAdapter.getUnitSelectedIdArray()[position],mHomeCourseDetailChapterAdapter.getUnitSelectedTitleArray()[position]);
                                }
                            })
                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    delete_unit(mHomeCourseDetailChapterAdapter.getUnitSelectedIdArray()[position], course_id);
                                }
                            })
                            .show();
                }
                return true;
            }
        });
        ahcdc_recyclerView.setAdapter(mHomeCourseDetailChapterAdapter);

    }*/

}
