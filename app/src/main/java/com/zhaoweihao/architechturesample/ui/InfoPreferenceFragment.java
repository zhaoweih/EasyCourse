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
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.preference.PreferenceFragmentCompat;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import sviolet.seatselectionview.demo.SeatSelectionActivity;


/**
 * Created by lizhaotailang on 2017/5/21.
 *
 * A preference fragment that displays the setting options and
 * about page.
 */

public class InfoPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.info_preference);

        // 测试显示信息界面

        findPreference("info1").setOnPreferenceClickListener(p -> {
            Intent intent = new Intent(getActivity(), UserInformation.class);
            startActivity(intent);
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
            Intent intent = new Intent(getActivity(), SeatSelectionActivity.class);
            startActivity(intent);
            return true;
        });


    }

}
