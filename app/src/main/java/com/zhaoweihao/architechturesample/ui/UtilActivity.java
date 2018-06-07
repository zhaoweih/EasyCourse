package com.zhaoweihao.architechturesample.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.lixs.charts.BarChart.DragInerfaces;
import com.lixs.charts.BarChart.LBarChartView;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.Login;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.User;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;
import static com.zhaoweihao.architechturesample.util.Utils.*;

public class UtilActivity extends AppCompatActivity {

    private static final Class thisClass = UtilActivity.class;

    private Button sendPost,sendGet,saveData,readData,showData,parseData,showLoading,uploadFile;
    private TextView showText;
    private ImageView imageView;

    private ProgressDialog progressDialog;

    private static final int PERMISSION_REQ_CODE = 1; // 权限请求码
    private static final int REQUEST_CODE = 2; // 图片选择器请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util);
        //控件初始化
        initViews();

        //发送post请求示例,以注册功能为例
        sendPost.setOnClickListener(v -> {
            //假设一些数据
            //以注册功能为例
            /**
             * @id id int (不需要提交，数据库自动生成)
             * @username 用户名
             * @password 密码
             * @studentId 学号
             * @teacherId 教师编号
             * @classId 班级编号
             * @department 学院
             * @education 学历 int
             * @date 入学时间
             * @school 学校
             * @sex 性别 int
             * @name 真实姓名
             */
            User user = new User();
            user.setUsername("zhaoweihao221");
            user.setPassword("abssss");
            user.setStudentId("2015191054");
            user.setClassId("20151912");
            user.setDepartment("教育系");
            user.setEducation(1);
            user.setDate("2015");
            user.setSchool("韩山师范学院");
            user.setSex(1);
            user.setName("赵威豪");

            //转换成json数据，借助gson
            String json = new Gson().toJson(user);
            log(thisClass,json);

            //发送post请求注册
            String after = "user/register";

            sendPostRequest(after, json, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //发送请求失败，有可能是网络断了或者其他的
                    log(thisClass, "发送请求失败，请检查网络");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    //Gson解析数据 json -> 对象
                    try {
                        RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                        log(thisClass, restResponse.getCode().toString());
                        //状态码500表示失败，打印错误信息
                        if (restResponse.getCode() == 500) {
                            log(thisClass, restResponse.getMsg());
                        }
                        //200代表成功，打印成功信息
                        if (restResponse.getCode() == 200) {
                            log(thisClass, "已成功注册");
                            //执行注册成功后的操作
                            //...
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    log(thisClass, body);
                }
            });


        });
        //发送get请求示例,以获取所有用户信息为例
        sendGet.setOnClickListener(v -> {
            String after = "user/get";

           sendGetRequest(after, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //发送请求失败，有可能是网络断了或者其他的
                    log(thisClass, "发送请求失败，请检查网络");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    log(thisClass, body);
                }
            });
        });
        //保存数据到数据库示例
        saveData.setOnClickListener(v -> {
            try {
                com.zhaoweihao.architechturesample.database.User user = new com.zhaoweihao.architechturesample.database.User();
                user.setUserId(1000);
                user.setUsername("zhaoweihao22");
                user.setStudentId("2015191054");
                user.setClassId("20151912");
                user.setDepartment("教育系");
                user.setEducation(1);
                user.setDate("2015");
                user.setSchool("韩山师范学院");
                user.setSex(1);
                user.setName("赵威豪");
                //保存到数据库
                user.save();
            } catch (Exception e) {
                e.printStackTrace();
            }

            log(thisClass, "保存成功");

        });
        //从数据库读取数据
        readData.setOnClickListener(v -> {
            com.zhaoweihao.architechturesample.database.User user = DataSupport.findFirst(com.zhaoweihao.architechturesample.database.User.class);
            log(thisClass, user.getName());
            log(thisClass, user.getDate());
        });
        //发送请求并显示数据到界面上
        showData.setOnClickListener(v -> {
            String after = "user/get";
            sendGetRequest(after, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    log(thisClass, body);
                    //切换主进程更新UI
                    runOnUiThread(() -> {
                        showText.setText(body);
                    });


                }
            });
        });

        // 解析payload数据，以登录功能为例
        parseData.setOnClickListener(v -> {
            String suffix = "user/login";
            // 组装login对象
            Login login = new Login();
            login.setUsername("zhaoweihao");
            login.setPassword("abbccc");
            //转换为json
            String json = new Gson().toJson(login);
            sendPostRequest(suffix, json, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //网络错误处理
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    //解析json数据组装RestResponse对象
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    if ( restResponse.getCode() == 500 ) {
                        log(thisClass, "登录失败，请检查用户名和密码");
                    }
                    // code 200等于登录成功
                    if ( restResponse.getCode() == 200 ) {
                        //首先获取payload (Object) , toString()转换成json
                        //接着用gson将json组装起来
                        User user = new Gson().fromJson(restResponse.getPayload().toString(), User.class);

                        log(thisClass, user.getName());
                    }
                }
            });
        });

        showLoading.setOnClickListener( v -> {
            // 创建加载中窗口
            progressDialog = createDialog(this);
            // 展示加载中
            progressDialog.show();

            Handler handler = new Handler();
            // 这里延迟2秒加载中消失，实际应用场景应该是所有数据加载完窗口消失
            handler.postDelayed(() -> progressDialog.dismiss(), 2000);

        });

        uploadFile.setOnClickListener( v ->  {
            // 先进行运行时权限检测，Android 6.0+特性
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有权限则请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
            } else {
                // 如果有查看外部储存权限就打开图片选择器
                showImageSelector();
            }
        });
    }



    private void showImageSelector() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE);
    }

    private void uploadImage(File file) {
        String suffix = "img/upload";
        sendPostRequestWithFile(suffix, file, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                // 拼接完整图片url,注意查看图片的url不一样
                String url = "http://zhaoweihao.com:9001" + restResponse.getPayload().toString();
                runOnUiThread(() -> Glide.with(UtilActivity.this).load(url).into(imageView));
            }
        });
    }

    private void initViews() {
        sendPost = findViewById(R.id.btn_send_post);
        sendGet = findViewById(R.id.btn_send_get);
        saveData = findViewById(R.id.btn_save_data);
        readData = findViewById(R.id.btn_read_data);
        showData = findViewById(R.id.btn_show);
        showText = findViewById(R.id.tv_show);
        parseData = findViewById(R.id.btn_parse);
        showLoading = findViewById(R.id.btn_loading);
        uploadFile = findViewById(R.id.btn_upload);
        imageView = findViewById(R.id.iv_show);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户同意权限赋予
                    showImageSelector();
                    break;
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK ) {
                    // 取得第一张照片的Uri
                    Uri uri = Matisse.obtainResult(data).get(0);
                    // 转换成绝对路径
                    Cursor cursor = null;
                    try {
                        String[] proj = { MediaStore.Images.Media.DATA };
                        cursor = getContentResolver().query(uri,  proj, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        // 上传图片
                        uploadImage(new File(cursor.getString(column_index)));
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }

                }

                break;
        }
    }
}
