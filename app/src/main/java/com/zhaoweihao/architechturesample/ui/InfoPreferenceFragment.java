/*
 * Copyright 2016 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhaoweihao.architechturesample.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.course.QueryActivity;
import com.zhaoweihao.architechturesample.course.QuerySelectActivity;
import com.zhaoweihao.architechturesample.course.SendNoti;
import com.zhaoweihao.architechturesample.course.SubmitActivity;
import com.zhaoweihao.architechturesample.data.Leave;
import com.zhaoweihao.architechturesample.data.RestResponse;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;
import com.zhaoweihao.architechturesample.data.course.Select;
import com.zhaoweihao.architechturesample.data.course.Submit;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.leave.LeaveListActivity;
import com.zhaoweihao.architechturesample.leave.LeaveShow;
import com.zhaoweihao.architechturesample.leave.LeaveSubmit;
import com.zhaoweihao.architechturesample.seat.EnterActivity;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import sviolet.seatselectionview.demo.SeatSelectionActivity;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendGetRequest;
import static com.zhaoweihao.architechturesample.util.Utils.*;


/**
 * Created by lizhaotailang on 2017/5/21.
 * <p>
 * A preference fragment that displays the setting options and
 * about page.
 */

public class InfoPreferenceFragment extends PreferenceFragmentCompat {
    private static final Class thisClass = InfoPreferenceFragment.class;
    private boolean Toastflag;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        Toastflag = true;
        addPreferencesFromResource(R.xml.info_preference);

        // 测试显示信息界面

        findPreference("info1").setOnPreferenceClickListener(p -> {
            User user3 = DataSupport.findLast(User.class);
            if (user3 == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), UserInformation.class);
                startActivity(intent);
            }
            return true;
        });
        // 测试提交请假条

        findPreference("submit").setOnPreferenceClickListener(p -> {
            User user3 = DataSupport.findLast(User.class);
            if (user3 == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else if (user3.getStudentId() == null) {
                Toast.makeText(getActivity(), "您不是学生，无法请假！", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), LeaveSubmit.class);
                startActivity(intent);
            }
            return true;
        });
        // 测试确认请假条并显示请假条

        findPreference("confirm").setOnPreferenceClickListener(p -> {
            User user3 = DataSupport.findLast(User.class);
            if (user3 == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else {
                // 修改点
                // 不要在这里做请求网络的工作，交给LeaveListActivity就可以
                Intent intent = new Intent(getActivity(), LeaveListActivity.class);
                startActivity(intent);


//                if (user3.getStudentId() == null && !(user3.getTeacherId() == null)) {
////                    submit(user3.getTeacherId(), 2);
//
//                    log(thisClass, "测试点1");
//                } else if (!(user3.getStudentId() == null) && user3.getTeacherId() == null) {
////                    submit(user3.getStudentId(), 1);
//                    log(thisClass, "测试点2");
//                }
//                if (Toastflag) {
//                    Toast.makeText(getActivity(), "写完请假条之后才可显示请假条！", Toast.LENGTH_SHORT).show();
//                }
            }
            return true;
        });
        // 打开登录界面
        findPreference("login").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return true;
        });
        // 打开点名界面
        findPreference("seat_select").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), EnterActivity.class);
            startActivity(intent);
            return true;
        });
        // 打开发布课程
        findPreference("sendnoti").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), SendNoti.class);
            startActivity(intent);
            return true;
        });
        // 打开提交课程
        findPreference("submitcourse").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), SubmitActivity.class);
            startActivity(intent);
            return true;
        });
        // 打开课程查询并选课queryandselectcourse
        findPreference("queryandselectcourse").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), QueryActivity.class);
            startActivity(intent);
            return true;
        });
        // 打开课程查询并选课queryselectcourse
        findPreference("queryselectcourse").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), QuerySelectActivity.class);
            startActivity(intent);
            return true;
        });

    }

    //判断学生是否有请假条
//    public void submit(String positoinId, int mode) {
//
//        /**
//         * @id id int (不需要提交，数据库自动生成)
//         * @username 用户名
//         * @password 密码
//         * @studentId 学号
//         * @teacherId 教师编号
//         * @classId 班级编号
//         * @department 学院
//         * @education 学历 int
//         * @date 入学时间
//         * @school 学校
//         * @sex 性别 int
//         * @name 真实姓名
//         */
//
//        //发送post请求注册
//        String after = "leave/query?studentId=" + positoinId;
//        if (mode == 1) {
//            after = "leave/query?studentId=" + positoinId;
//        } else if (mode == 2) {
//            after = "leave/query?teacherId=" + positoinId;
//        }
//
//        sendGetRequest(after, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //发送请求失败，有可能是网络断了或者其他的
//                //Toast.makeText(RegisterActivity.this,"发送请求失败，请检查网络！", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String body = response.body().string();
//                //Gson解析数据 json -> 对象
//                try {
//                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
//                    //状态码500表示失败，打印错误信息
//                    if (restResponse.getCode() == 500) {
//                        log(thisClass, restResponse.getMsg());
//                    }
//                    //200代表成功，打印成功信息
//                    if (restResponse.getCode() == 200) {
//                        log(thisClass, "已成功注册");
//                        // Toast.makeText(RegisterActivity.this,"注册成功！", Toast.LENGTH_SHORT).show();
//                        //执行注册成功后的操作
//                        //...
//
//                        Leave leaves[] = new Gson().fromJson(restResponse.getPayload().toString(), Leave[].class);
//                        if (leaves.length == 0) {
//
//                        } else {
//                            Toastflag = false;
//                            Intent intent = new Intent(getActivity(), LeaveListActivity.class);
//                            startActivity(intent);
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
