package com.zhaoweihao.architechturesample.adapter;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanxinkui
 * @description 用于课程的Adapter
 * @time 2019/1/22 16:33
 */
public class HomeCourseStudentAdapter extends BaseQuickAdapter<QuerySelect, BaseViewHolder> {
    private int[] courseSelectedId;
    private String[] courseSelectedName;
    private List<QuerySelect> querySelectNow = new ArrayList<>();

    public HomeCourseStudentAdapter(ArrayList<QuerySelect> data) {
        super(R.layout.layout_home_course, data);
        courseSelectedId = new int[data.size()];
        courseSelectedName = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            courseSelectedId[i] = data.get(i).getCourseId();
            courseSelectedName[i] = data.get(i).getCourseName();
        }
        querySelectNow.clear();
        querySelectNow.addAll(data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, QuerySelect querySelect) {
      /*  helper.setText(R.id.text, course.getTitle());
        helper.setImageResource(R.id.icon, course.getImageResource());
        // 加载网络图片
        Glide.with(mContext).load(course.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));*/
        viewHolder
                .setText(R.id.lhc_tv_title, querySelect.getCourseName())
                .setText(R.id.lhc_tv_teacher_name, querySelect.getTeacherName())
                .setText(R.id.lhc_iv_collect, "简述")
                .setVisible(R.id.lhc_iv_collect, true)
                .addOnClickListener(R.id.lhc_iv_collect);
        Log.v("tanxinkui", "password:" + querySelect.getPassword());
        Log.v("HomeCourseStudent", "getcourse" + querySelect.getCourse().toString());
        if (querySelect.getCourse() != null) {
            //Query query = new Gson().fromJson(querySelect.getCourse(), Query.class);
            Query query = null;
            try {
                query = JSON.parseObject(querySelect.getCourse().toString(), Query.class);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("HomeCourseStudent", "eee" + e.toString());
            }
            if (!TextUtils.isEmpty(query.getClass_image())) {
                Log.v("HomeCourseStudent", "" + query.getClass_image());
                NetWorkImageView netWorkImageView = viewHolder.getView(R.id.lhc_iv_course_icon);
                netWorkImageView.setImageURL(Constant.getBaseUrl() + "upload/" + query.getClass_image());
            }
        }
    }

    public int[] getCilckedCourseIdArray() {
        Log.v("tanxinkui", "" + courseSelectedId.length);
        return courseSelectedId;
    }

    public String[] getCourseSelectedName() {
        return courseSelectedName;
    }

    public List<QuerySelect> getQuerySelectNow() {
        return querySelectNow;
    }
}
