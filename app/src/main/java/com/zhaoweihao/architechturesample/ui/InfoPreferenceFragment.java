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


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.StringRes;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.customtabs.CustomTabsHelper;
import com.zhaoweihao.architechturesample.database.User;
<<<<<<< HEAD
=======
import com.zhaoweihao.architechturesample.leave.LeaveListActivity;
import com.zhaoweihao.architechturesample.leave.LeaveShow;
import com.zhaoweihao.architechturesample.leave.LeaveSubmit;
import com.zhaoweihao.architechturesample.seat.EnterActivity;
>>>>>>> TanXinKui-master

import org.litepal.crud.DataSupport;


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

<<<<<<< HEAD
        // 打开源代码网页
        findPreference("source").setOnPreferenceClickListener(p -> {
            CustomTabsHelper.openUrl(getContext(), getString(R.string.source_code_desc));
            return true;
        });

        // 打开贡献者目录
        findPreference("contributors_android").setOnPreferenceClickListener(p -> {
            CustomTabsHelper.openUrl(getContext(), getString(R.string.contributors));
            return true;
        });

        // 打开贡献者目录
        findPreference("contributors_backend").setOnPreferenceClickListener(p -> {
            CustomTabsHelper.openUrl(getContext(), getString(R.string.contributors_backend));
            return true;
        });

        // 通过发送邮件反馈
        findPreference("feedback").setOnPreferenceClickListener(p -> {
            try{
                Uri uri = Uri.parse(getString(R.string.sendto));
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic));
                intent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.device_model) + Build.MODEL + "\n"
                                + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                                + getString(R.string.version));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex){
                showMessage(R.string.no_mail_app);
            }
            return true;
        });

        // 展示开源协议
        findPreference("open_source_license").setOnPreferenceClickListener(p -> {
            startActivity(new Intent(getActivity(), LicenseActivity.class));
            return true;
        });


    }
=======
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

>>>>>>> TanXinKui-master

    private void showMessage(@StringRes int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

}
