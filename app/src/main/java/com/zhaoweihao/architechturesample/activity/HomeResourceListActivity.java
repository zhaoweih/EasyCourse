package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.HomeResourceAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.SubmitAndShowResource;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.interfaze.DownloadListener;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.DownloadUtil;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
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
 * @description 首页-资源-课程-资源列表
 * @time 2019/3/7 11:26
 */
public class HomeResourceListActivity extends BaseActivity {
    @BindView(R.id.ahrl_titleLayout)
    TitleLayout ahrl_titleLayout;
    @BindView(R.id.ahrl_empty_view)
    LinearLayout ahrl_empty_view;
    @BindView(R.id.ahrl_recyclerView)
    RecyclerView ahrl_recyclerView;
    private QMUITipDialog qmuiTipDialog, deleteTipDialog, downloadTipDialog;
    private HomeResourceAdapter mHomeResourceAdapter;
    private final String TAG1 = "tanxinkuiResourceList";
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_resource_list);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ahrl_recyclerView.setLayoutManager(layoutManager);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryIfResourceExists();
    }

    private void init() {
        ahrl_titleLayout.setTitle(getIntent().getStringExtra("courseName"));
        addBtn = ahrl_titleLayout.getSettingButton();
        if (DataSupport.findLast(User.class).getTeacherId() != null) {
            addBtn.setText("添加");
            addBtn.setOnClickListener(view -> AddResource());
        }
        qmuiTipDialog = new QMUITipDialog.Builder(HomeResourceListActivity.this)
                .setTipWord("正在加载资源文档...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        deleteTipDialog = new QMUITipDialog.Builder(HomeResourceListActivity.this)
                .setTipWord("正在删除资源文档...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        downloadTipDialog = new QMUITipDialog.Builder(HomeResourceListActivity.this)
                .setTipWord("正在下载资源文档...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
    }

    private void AddResource() {
        Intent intent = new Intent(HomeResourceListActivity.this, HomeResourceSubmitActivity.class);
        intent.putExtra("courseId", getIntent().getIntExtra("courseId", -1));
        startActivity(intent);
    }

    private void queryIfResourceExists() {
        qmuiTipDialog.show();
        HttpUtil.sendGetRequest(com.zhaoweihao.architechturesample.util.Constant.QERRY_RESOURCE_URL +
                getIntent().getIntExtra("courseId", -1), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    resourceNotExits();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                if (restResponse.getCode() == com.zhaoweihao.architechturesample.util.Constant.SUCCESS_CODE) {
                    runOnUiThread(() -> {
                        List<SubmitAndShowResource> submitAndShowResource = JSON.parseArray(restResponse.getPayload().toString(), SubmitAndShowResource.class);
                        resourceExists(submitAndShowResource);
                    });
                } else {
                    runOnUiThread(() -> {
                        resourceNotExits();
                    });
                }
            }
        });
    }

    private void resourceExists(List<SubmitAndShowResource> submitAndShowResource) {
        Log.v(TAG1, "now the resource exist");
        qmuiTipDialog.dismiss();
        ahrl_empty_view.setVisibility(View.GONE);
        initStudentAdapter(submitAndShowResource);
    }

    private void initStudentAdapter(List<SubmitAndShowResource> submitAndShowResources) {
        mHomeResourceAdapter = new HomeResourceAdapter(submitAndShowResources);
        mHomeResourceAdapter.openLoadAnimation();
        mHomeResourceAdapter.isFirstOnly(false);
        mHomeResourceAdapter.setEnableLoadMore(false);
        mHomeResourceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(HomeResourceListActivity.this, HomeResourceDetailActivity.class);
                intent.putExtra("submitAndShowResources", submitAndShowResources.get(position));
                startActivity(intent);
            }
        });
        mHomeResourceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.lr_iv_tag) {
                    downLoadResource(submitAndShowResources.get(position));
                }
            }
        });
        mHomeResourceAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                new QMUIDialog.MessageDialogBuilder(HomeResourceListActivity.this)
                        .setTitle("确定要删除：" + submitAndShowResources.get(position).getRes_name() + "?")
                        .setMessage("删除该文档！")
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
                                deleteTipDialog.show();
                                deleteResource(submitAndShowResources.get(position).getId());
                            }
                        })
                        .show();
                return false;
            }
        });
        ahrl_recyclerView.setAdapter(mHomeResourceAdapter);
    }

    private void deleteResource(int ResourceId) {
        HttpUtil.sendGetRequest(com.zhaoweihao.architechturesample.util.Constant.DELETE_RESOURCE_URL +
                ResourceId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    deleteResourceProcess(false);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    deleteResourceProcess(true);
                });
            }
        });
    }

    private void deleteResourceProcess(Boolean ifSucceed) {
        deleteTipDialog.dismiss();
        if (ifSucceed) {
            Toast.makeText(HomeResourceListActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
            queryIfResourceExists();

        } else {
            Toast.makeText(HomeResourceListActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private void resourceNotExits() {
        Log.v(TAG1, "now the resource dosent exist");
        qmuiTipDialog.dismiss();
        ahrl_empty_view.setVisibility(View.VISIBLE);
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void downLoadResource(SubmitAndShowResource submitAndShowResource) {
        String url = submitAndShowResource.getRes_url();
        String path = Environment.getExternalStorageDirectory().getPath() + "/lightCourse/" + submitAndShowResource.getRes_name() + "." + parseFormat(submitAndShowResource.getRes_url());
        DownloadUtil.download(url, path, new DownloadListener() {
            @Override
            public void onStart() {
                runOnUiThread(() -> {
                    downloadTipDialog.show();
                });
            }

            @Override
            public void onProgress(int progress) {
                runOnUiThread(() -> {
                    Log.v(TAG1, "loading_progress" + progress);
                });
            }

            @Override
            public void onFinish(String path) {
                runOnUiThread(() -> {
                    Log.v(TAG1, "finish downlaod!");
                    downloadTipDialog.dismiss();
                    Toast.makeText(HomeResourceListActivity.this, "已下载为："+path, Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onFail(String errorInfo) {
                runOnUiThread(() -> {
                    downloadTipDialog.dismiss();
                    Toast.makeText(HomeResourceListActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
