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

package com.zhaoweihao.architechturesample.timeline;


import com.zhaoweihao.architechturesample.BasePresenter;
import com.zhaoweihao.architechturesample.BaseView;
import com.zhaoweihao.architechturesample.course.QuerySelectContract;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;

import java.util.ArrayList;
import java.util.List;


public interface DoubanMomentContract {

    interface View extends BaseView< DoubanMomentContract.Presenter> {
        void showResult(ArrayList<QuerySelect> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showSelectSuccess(Boolean status);

    }

    interface  Presenter extends BasePresenter {
        void querySelect(String url);

        ArrayList<QuerySelect> getQueryList();

        Boolean checkTecOrStu();
    }

}
