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

        // 打开登录界面
        findPreference("login").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return true;
        });

        /*  // 打开点名界面
        findPreference("seat_select").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), EnterActivity.class);
            startActivity(intent);
            return true;
        });// 打开发布课程
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
        });*/


    }

}
