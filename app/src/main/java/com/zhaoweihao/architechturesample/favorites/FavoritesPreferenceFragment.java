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

package com.zhaoweihao.architechturesample.favorites;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.course.QueryActivity;
import com.zhaoweihao.architechturesample.course.SendNoti;
import com.zhaoweihao.architechturesample.course.SubmitActivity;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.leave.LeaveListActivity;
import com.zhaoweihao.architechturesample.leave.LeaveSubmit;
import com.zhaoweihao.architechturesample.reading.ZhihuDailyActivity;
import com.zhaoweihao.architechturesample.seat.CreateActivity;
import com.zhaoweihao.architechturesample.seat.EnterActivity;
import com.zhaoweihao.architechturesample.tools.TranslateActivity;

import org.litepal.crud.DataSupport;


/**
 * Created by lizhaotailang on 2017/5/21.
 * <p>
 * A preference fragment that displays the setting options and
 * about page.
 */

public class FavoritesPreferenceFragment extends PreferenceFragmentCompat {
    private static final Class thisClass = FavoritesPreferenceFragment.class;
    private boolean Toastflag;
    private User userCheck;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        Toastflag = true;

        //用来判断当前的用户为学生或老师
        addPreferencesFromResource(R.xml.fav_preference);
        // 测试提交请假条

        findPreference("submit").setOnPreferenceClickListener(p -> {
            userCheck= DataSupport.findLast(User.class);
            if (userCheck == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else if (userCheck.getStudentId() == null) {
                Toast.makeText(getActivity(), "您不是学生，无法请假！", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), LeaveSubmit.class);
                startActivity(intent);
            }
            return true;
        });
        // 测试确认请假条并显示请假条

        findPreference("confirm").setOnPreferenceClickListener(p -> {
            userCheck= DataSupport.findLast(User.class);
            if (userCheck == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else {
                // 修改点
                // 不要在这里做请求网络的工作，交给LeaveListActivity就可以
                Intent intent = new Intent(getActivity(), LeaveListActivity.class);
                startActivity(intent);
            }
            return true;
        });
        // 创建点名房间
        findPreference("seat_create").setOnPreferenceClickListener(p -> {
            userCheck= DataSupport.findLast(User.class);
            if (userCheck == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else if (userCheck.getTeacherId() == null) {
                Toast.makeText(getActivity(), "您不是老师，无法创建点名房间！", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                startActivity(intent);
            }
            return true;
        });
        // 进入点名房间
        findPreference("seat_enter").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), EnterActivity.class);
            startActivity(intent);
            return true;
        });
        // 打开发布课程通知
        findPreference("sendnoti").setOnPreferenceClickListener(p -> {
            userCheck= DataSupport.findLast(User.class);
            if (userCheck == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else if (userCheck.getTeacherId() == null) {
                Toast.makeText(getActivity(), "您不是老师，无法发布通知！", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), SendNoti.class);
                startActivity(intent);
            }
            return true;
        });
        // 打开提交课程
        findPreference("submitcourse").setOnPreferenceClickListener(p -> {
            userCheck= DataSupport.findLast(User.class);
            if (userCheck == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else if (userCheck.getTeacherId() == null) {
                Toast.makeText(getActivity(), "您不是老师，无法提交课程！", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), SubmitActivity.class);
                startActivity(intent);
            }
            return true;
        });
        // 打开课程查询并选课queryandselectcourse
        findPreference("queryandselectcourse").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), QueryActivity.class);
            startActivity(intent);
            return true;
        });


        // 打开翻译页面
        findPreference("translate").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), TranslateActivity.class);
            startActivity(intent);
            return true;
        });

        // 打开知乎日报
        findPreference("zhihu_daily").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), ZhihuDailyActivity.class);
            startActivity(intent);
            return true;
        });

    }

}
