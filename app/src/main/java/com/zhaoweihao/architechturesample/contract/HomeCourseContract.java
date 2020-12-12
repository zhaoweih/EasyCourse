package com.zhaoweihao.architechturesample.contract;

import com.zhaoweihao.architechturesample.base.BasePresenter;
import com.zhaoweihao.architechturesample.base.BaseView;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;

import java.util.ArrayList;
/**
*@description 用于查询学生/老师课程信息之后，展示课程信息
*@author tanxinkui
*@time 2019/1/23 14:08
*/
public interface HomeCourseContract {
    interface View extends BaseView< HomeCourseContract.Presenter> {
        void showResult(ArrayList<QuerySelect> queryArrayList);

        void showTeacherResult(ArrayList<Query> queryArrayList);

        void startLoading();

        void stopLoading();

        void showLoadError(String error);

        void showSelectSuccess(Boolean status);

        void onDeleteCourseSuccess();

    }

    interface  Presenter extends BasePresenter {
        void querySelect();

        ArrayList<QuerySelect> getQueryList();

        ArrayList<Query> getQueryTeacherList();

        Boolean checkIsStu();

        void deleteCourse(int courseId);
    }
}
