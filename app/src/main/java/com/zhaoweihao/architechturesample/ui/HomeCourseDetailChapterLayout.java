package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.HomeCourseDetailChapterListContentActivity;
import com.zhaoweihao.architechturesample.adapter.HomeCourseDetailChapterAdapter;
import com.zhaoweihao.architechturesample.bean.AddUnit;
import com.zhaoweihao.architechturesample.bean.GetUnit;
import com.zhaoweihao.architechturesample.bean.ModifyUnit;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
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
 * @author tanxinkui
 * @description 用于课程的章节页面
 * @time 2019/1/21 23:36
 */

public class HomeCourseDetailChapterLayout extends LinearLayout {

    @BindView(R.id.ahcdc_recyclerView)
    RecyclerView ahcdc_recyclerView;

    @BindView(R.id.ahcdc_fb_write_unit)
    FloatingActionButton ahcdc_fb_write_unit;

    @BindView(R.id.ahcdc_empty_view)
    LinearLayout ahcdc_empty_view;

    private HomeCourseDetailChapterAdapter mHomeCourseDetailChapterAdapter;

    private List<GetUnit> getUnitList = new ArrayList<>();

    private Handler mainHandler = new Handler(Looper.getMainLooper());


    QMUIDialog.EditTextDialogBuilder inputDialog, modifyDialog;
    QMUITipDialog LoadDialog = new QMUITipDialog.Builder(getContext())
            .setTipWord("正在添加单元...")
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .create();

    public HomeCourseDetailChapterLayout(Context context, @Nullable AttributeSet attrs, int courseID) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_home_course_detail_chapter, this);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ahcdc_recyclerView.setLayoutManager(layoutManager);
        init(courseID);
        initAdapter(courseID);
        if (DataSupport.findLast(User.class).getStudentId() != null) {
            ahcdc_fb_write_unit.setVisibility(GONE);
        }
    }

    private void init(int courseID) {
        ahcdc_fb_write_unit.setOnClickListener(view -> add_unit(courseID));
    }

    private void add_unit(int courseID) {
        inputDialog = new QMUIDialog.EditTextDialogBuilder(getContext())
                .setTitle("请输入您的章节标题")
                .setPlaceholder("例如：第一章国学经典")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        request(courseID, inputDialog.getEditText().getText().toString());

                    }
                });
        inputDialog.show();
    }

    private void modify_unit(int courseID, int unit_id, String title) {
        modifyDialog = new QMUIDialog.EditTextDialogBuilder(getContext())
                .setTitle("原标题：" + title)
                .setPlaceholder("请输入新单元标题！")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        modify_request(courseID, unit_id, modifyDialog.getEditText().getText().toString());

                    }
                });
        modifyDialog.show();
    }

    private void modify_request(int courseId, int unit_id, String modifyText) {
        ModifyUnit modifyUnit = new ModifyUnit();
        modifyUnit.setCourse_id(courseId);
        modifyUnit.setUser_id(DataSupport.findLast(User.class).getUserId());
        modifyUnit.setTeacher_id(DataSupport.findLast(User.class).getTeacherId());
        modifyUnit.setTitle(modifyText);
        String json = new Gson().toJson(modifyUnit);
        HttpUtil.sendPostRequest(Constant.UPDATE_UNIT_URL + unit_id, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                initAdapter(courseId);
            }
        });

    }

    private void delete_unit(int unit_id, int course_id) {
        HttpUtil.sendGetRequest(Constant.DELETE_UNIT_URL + unit_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                initAdapter(course_id);
            }
        });
    }

    private void request(int course_id, String title) {
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

    private void showTitle() {

    }

    private void initAdapter(int courseId) {
        HttpUtil.sendGetRequest(Constant.GET_UNIT_URL + "?course_id=" + courseId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoadDialog.dismiss();
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
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showAdapter(courseId);
                        }
                    });
                }
            }
        });
    }

    private void showAdapter(int course_id) {
        if (getUnitList.size() == 0) {
            ahcdc_empty_view.setVisibility(VISIBLE);
        } else {
            ahcdc_empty_view.setVisibility(GONE);
        }
        mHomeCourseDetailChapterAdapter = new HomeCourseDetailChapterAdapter(getUnitList);
        mHomeCourseDetailChapterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), HomeCourseDetailChapterListContentActivity.class);
                intent.putExtra("unit_title", mHomeCourseDetailChapterAdapter.getUnitSelectedTitleArray()[position]);
                intent.putExtra("unit_id", mHomeCourseDetailChapterAdapter.getUnitSelectedIdArray()[position]);
                intent.putExtra("course_id", course_id);
                intent.putExtra("reading_mode", "writing");
                getContext().startActivity(intent);

            }
        });
        mHomeCourseDetailChapterAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (DataSupport.findLast(User.class).getStudentId() == null) {
                    new QMUIDialog.MessageDialogBuilder(getContext())
                            .setTitle("您选中：" + mHomeCourseDetailChapterAdapter.getUnitSelectedTitleArray()[position])
                            .setMessage("可删除该单元所有内容或仅修改该单元的标题！")
                            .addAction("修改", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    modify_unit(course_id, mHomeCourseDetailChapterAdapter.getUnitSelectedIdArray()[position], mHomeCourseDetailChapterAdapter.getUnitSelectedTitleArray()[position]);
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

    }

}
