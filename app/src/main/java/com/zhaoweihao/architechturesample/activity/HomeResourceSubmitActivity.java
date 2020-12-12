package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.SubmitAndShowResource;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
 * @description 首页-资源-资源列表-提交资源
 * @time 2019/3/7 15:03
 */
public class HomeResourceSubmitActivity extends BaseActivity implements TbsReaderView.ReaderCallback {
    @BindView(R.id.ahrs_titleLayout)
    TitleLayout ahrs_titleLayout;
    @BindView(R.id.ahrs_btn_submit)
    Button ahrs_btn_submit;
    @BindView(R.id.rl1_root)
    RelativeLayout rootRl;
    private Button button;
    private QMUITipDialog uploadDialog;
    private final String TAG1 = "tanxinkuisubmit";
    private SubmitAndShowResource submitAndShowResource;
    private String fileOtherName;
    private int courseId;
    OkHttpClient client = new OkHttpClient();
    TbsReaderView mTbsReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_resource_submit);
        ButterKnife.bind(this);
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
        ahrs_btn_submit.setVisibility(View.GONE);
        ahrs_titleLayout.setTitle("添加资源预览");
        button = ahrs_titleLayout.getSettingButton();
        courseId = getIntent().getIntExtra("courseId", -1);
        uploadDialog = new QMUITipDialog.Builder(HomeResourceSubmitActivity.this)
                .setTipWord("正在上传资源文档...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        setButton();
        pickFile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    @Override
    public void onBackPressed() {
        finish();//不关掉此界面，之后加载文件会无法加载
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
        //  finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    //获得选择的文件集合
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                    //Log.d("uplao", list.get(0).toString());
                    Log.d("Uplaodaciit", list.get(0).getPath());
                    //上传文件的url
                    String url = "https://test.tanxinkui.cn/api/stuffs/upload";
                    //上传单个文件
                    File file = new File(list.get(0).getPath());
                    if (!file.exists()) {
                        ToastUtil.getInstance(this).showToast("文件不存在");
                    }
                    String fileName = file.getName();
                    String prefix = fileName.substring(fileName.lastIndexOf("."));
                    int num = prefix.length();
                    fileOtherName = fileName.substring(0, fileName.length() - num);
                    Log.v(TAG1, file.getAbsolutePath());
                    Log.v(TAG1, file.getPath());
                    Log.v(TAG1, file.getName());
                    Log.v(TAG1, fileOtherName);
                    Log.v(TAG1, getCurrentDate());
                    displayFile(url, file);
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private String getCurrentDate() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        return df.format(today);
    }

    private void setButton() {
        if (DataSupport.findLast(User.class).getTeacherId() != null) {
            button.setText("修改");
            button.setOnClickListener(view -> pickFile());
        }
    }

    private void pickFile() {
        Intent intent1 = new Intent(HomeResourceSubmitActivity.this, NormalFilePickActivity.class);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        intent1.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf"});
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_FILE);
    }

    private void displayFile(String url, File file) {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
        mTbsReaderView = new TbsReaderView(HomeResourceSubmitActivity.this, this);
        rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        ahrs_btn_submit.setVisibility(View.VISIBLE);
        ahrs_btn_submit.setText("提交正在浏览的文档");
        ahrs_btn_submit.setOnClickListener(view -> {
            uploadFile(url, file);
        });
        Bundle bundle = new Bundle();
        bundle.putString("filePath", file.getPath());
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        Log.v(TAG1, "what?3");
        boolean result = false;
        try {
            result = mTbsReaderView.preOpen(parseFormat(file.getName()), false);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG1, "what?" + e.toString());
        }
        Log.v(TAG1, "what?d" + result);
        if (result) {
            mTbsReaderView.openFile(bundle);
            Log.v(TAG1, "what?");
        }
    }

    private void uploadFile(String url, File file) {
        uploadDialog.show();
        // 创建一个RequestBody，文件的类型是image/png multipart/form-data
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                // 设置type为"multipart/form-data"，不然无法上传参数
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
//                .addFormDataPart("comment", "上传一个图片哈哈哈哈")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            /**
             * 上传失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    uploadFail();
                });
            }

            /**
             * 上传成功回调
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                String uploadString = restResponse.getPayload().toString();
                runOnUiThread(() -> {
                    if (TextUtils.isEmpty(uploadString)) {
                        uploadFail();
                    } else {
                        Log.v(TAG1, "return_url_trimed" + uploadString.substring(8, uploadString.length()));
                        uploadSucceed(uploadString.substring(8, uploadString.length()));
                    }
                });
            }
        });
    }

    private void uploadFail() {
        uploadDialog.dismiss();
        Toast.makeText(HomeResourceSubmitActivity.this, "上传失败,请检查网络或重试！", Toast.LENGTH_SHORT).show();
    }

    private void uploadSucceed(String uploadString) {
        submitResource(uploadString);
    }

    private void submitResource(String url) {
        SubmitAndShowResource submitAndShowResource = new SubmitAndShowResource();
        submitAndShowResource.setClass_id(courseId);
        submitAndShowResource.setRes_name(fileOtherName);
        submitAndShowResource.setRes_size(1);
        submitAndShowResource.setRes_time(getCurrentDate());
        submitAndShowResource.setTeacher_id(DataSupport.findLast(User.class).getTeacherId());
        submitAndShowResource.setRes_url(url);
        String json = JSON.toJSONString(submitAndShowResource);
        Log.v(TAG1, "JSON" + json);
        HttpUtil.sendPostRequest(com.zhaoweihao.architechturesample.util.Constant.SUBMIT_RESOURCE_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    onSubmitFail();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                if (restResponse.getCode() == com.zhaoweihao.architechturesample.util.Constant.SUCCESS_CODE) {
                    runOnUiThread(() -> {
                        onSubmitSucceed();
                    });
                } else {
                    runOnUiThread(() -> {
                        onSubmitFail();
                    });
                }
            }
        });
    }

    private void onSubmitSucceed() {
        uploadDialog.dismiss();
        Toast.makeText(HomeResourceSubmitActivity.this, "提交资源成功！", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void onSubmitFail() {
        uploadDialog.dismiss();
        Toast.makeText(HomeResourceSubmitActivity.this, "提交资源失败！", Toast.LENGTH_SHORT).show();
    }

}
