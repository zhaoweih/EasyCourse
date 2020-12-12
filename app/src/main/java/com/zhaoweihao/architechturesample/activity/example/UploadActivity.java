package com.zhaoweihao.architechturesample.activity.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vincent.filepicker.Constant;
import com.vincent.filepicker.ToastUtil;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



/**
 * 上传文件示例
 */
public class UploadActivity extends BaseActivity {
    public static final String TAG = UploadActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button uplaodFile = findViewById(R.id.uplaod_file);
        uplaodFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择文件
                Intent intent4 = new Intent(UploadActivity.this, ImagePickActivity.class);
                intent4.putExtra(Constant.MAX_NUMBER, 9);
                //intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf","avi","mp4","jpg","png"});
                intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"jpg"});
                startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_IMAGE);
            }
        });
    }

    OkHttpClient client = new OkHttpClient();

    /**
     * 上传文件方法
     * @param url
     * @param file
     */
    private void uploadFile(String url, File file) {
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
                System.out.println("失败");
                Log.d(TAG, "上传失败");
            }

            /**
             * 上传成功回调
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //这里会返回上传后的文件地址（在payload里面）,前面加上前缀,HttpUtil.prefix

                try {
                    System.out.println("上传返回：\n" + response.body());
                    Log.v("上传", "上传数据body" + response.body()+"**tostring**"+response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("上传错误返回：\n"+e.toString());
                }


//                Log.d(TAG, "上传返回 ===" + response.body().string());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
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
                    uploadFile(url, file);
                }
                break;
                default:
                    break;
        }

    }
}
