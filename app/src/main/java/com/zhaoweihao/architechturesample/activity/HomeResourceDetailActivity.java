package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.ToastUtil;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.Leave;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.SubmitAndShowResource;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.interfaze.DownloadListener;
import com.zhaoweihao.architechturesample.ui.RoundProgressBar;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.DownloadUtil;
import com.zhaoweihao.architechturesample.util.HttpUtil;
import com.zhihu.matisse.MimeType;


import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.sl.usermodel.SlideShow;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author
 * @description 首页-资源-详细界面
 * @time 2019/3/4 11:34
 */
public class HomeResourceDetailActivity extends BaseActivity implements TbsReaderView.ReaderCallback {
    @BindView(R.id.ahrd_titleLayout)
    TitleLayout ahrd_titleLayout;
    @BindView(R.id.rl_root)
    RelativeLayout rootRl;
    private Button button;
    @BindView(R.id.ahrd_empty_view)
    LinearLayout ahrd_empty_view;
    @BindView(R.id.ahrd_btn_submit)
    Button ahrd_btn_submit;
    @BindView(R.id.ahrd_roundProgress)
    RoundProgressBar ahrd_roundProgress;
    private QMUITipDialog qmuiTipDialog, uploadDialog;
    private final String TAG1 = "tanxinkuiddd";
    private SubmitAndShowResource submitAndShowResource;
    private final static int REQUEST_PICKFILE = 5;
    TbsReaderView mTbsReaderView;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_resource_detail);
        ButterKnife.bind(this);
        //setButtonMode(SUBMIT_MODE);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("APPAplication", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                Log.e("APPAplication", " onCoreInitFinished");
            }
        };
        QbSdk.initX5Environment(getApplicationContext(), cb);
        ahrd_btn_submit.setVisibility(View.GONE);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
    /*    page++;
        Log.v("tanxinkuifind", integer.toString()+"page"+page);
        if (o != null) {
            Log.v("tanxinkuifind", "o:  "+o.toString());
        }
        if (o1 != null) {
            Log.v("tanxinkuifind","o1: "+ o1.toString());
        }*/
        //  finish();
    }

    @Override
    public void onBackPressed() {
        finish();//不关掉此界面，之后加载文件会无法加载
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    private void initViews() {
        Intent intent = getIntent();
        submitAndShowResource = (SubmitAndShowResource) intent.getSerializableExtra("submitAndShowResources");
        ahrd_titleLayout.setTitle(submitAndShowResource.getRes_name() + "." + parseFormat(submitAndShowResource.getRes_url()));
        button = ahrd_titleLayout.getSettingButton();
        qmuiTipDialog = new QMUITipDialog.Builder(HomeResourceDetailActivity.this)
                .setTipWord("正在加载资源文档...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        showResource(submitAndShowResource);
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void showResource(SubmitAndShowResource submitAndShowResource) {
        qmuiTipDialog.dismiss();
        ahrd_empty_view.setVisibility(View.GONE);
        //setButtonMode(DELETE_MODE);
        String url = submitAndShowResource.getRes_url();
        String path = Environment.getExternalStorageDirectory().getPath() + "/lightCourseTemp/tempDoc." + parseFormat(submitAndShowResource.getRes_url());
        DownloadUtil.download(url, path, new DownloadListener() {
            @Override
            public void onStart() {
                runOnUiThread(() -> {
                    ahrd_roundProgress.setVisibility(View.VISIBLE);
                    //ahrd_roundProgress.setMax(100);
                    ahrd_roundProgress.setProgress(0);
                    Log.v(TAG1, "starting downlaod!");
                });
            }

            @Override
            public void onProgress(int progress) {
                runOnUiThread(() -> {
                    Log.v(TAG1, "loading_progress" + progress);
                    ahrd_roundProgress.setProgress(progress);
                });
            }

            @Override
            public void onFinish(String path) {
                runOnUiThread(() -> {
                    Log.v(TAG1, "finish downlaod!");
                    ahrd_roundProgress.setVisibility(View.GONE);
                    File file = new File(path);
                    try {
                        displayFile(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v(TAG1, "this file may not exits!" + e.toString());
                    }
                });
            }

            @Override
            public void onFail(String errorInfo) {
                runOnUiThread(() -> {
                    ahrd_roundProgress.setVisibility(View.GONE);
                });
            }
        });
    }

    private void displayFile(File file) {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
        mTbsReaderView = new TbsReaderView(HomeResourceDetailActivity.this, this);
        rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        ahrd_empty_view.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString("filePath", file.getPath());
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        Log.v(TAG1, "what?3");
        boolean result = false;
        try {
            result = mTbsReaderView.preOpen(parseFormat(file.getName()), false);
            Log.v(TAG1, "what?preopening" + mTbsReaderView.getContentDescription());
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG1, "what?" + e.toString());
        }
        Log.v(TAG1, "what?d" + result);
        mTbsReaderView.openFile(bundle);


    }

    private void pickFileNotUseThisTime(String type) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        String path = "content://";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Uri docsURI = FileProvider.getUriForFile(this, "com.zhaoweihao.architechturesample.provider", dir);
        //调用系统文件管理器打开指定路径目录
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(docsURI, type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_PICKFILE);
    }

    private void displayFile() {
        Log.v(TAG1, "123");
        Bundle bundle = new Bundle();
        bundle.putString("filePath", "/storage/emulated/0/tencent/MicroMsg/Download/2015191036谭新奎毕业论文提纲.docx");
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        Log.v(TAG1, "123456");
        boolean result = false;
        result = mTbsReaderView.preOpen(parseFormat("2015191036谭新奎毕业论文提纲.docx"), false);
        if (result) {
            mTbsReaderView.openFile(bundle);
            Log.v(TAG1, "12dd3" + result);
        }
    }
}
