package com.zhaoweihao.architechturesample.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.User;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;
import static com.zhaoweihao.architechturesample.util.Utils.*;

public class UtilActivity extends AppCompatActivity {

    private static final Class thisClass = UtilActivity.class;

    private Button sendPost,sendGet,saveData,readData,showData;
    private TextView showText;

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
            user.setUsername("zhaoweihao22");
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
    }

    private void initViews() {
        sendPost = findViewById(R.id.btn_send_post);
        sendGet = findViewById(R.id.btn_send_get);
        saveData = findViewById(R.id.btn_save_data);
        readData = findViewById(R.id.btn_read_data);
        showData = findViewById(R.id.btn_show);
        showText = findViewById(R.id.tv_show);
    }
}
