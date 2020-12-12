package com.zhaoweihao.architechturesample.adapter;

import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanxinkui
 * @description 用于课程的Adapter
 * @time 2019/1/22 16:33
 */
public class HomeCourseTeacherAdapter extends BaseQuickAdapter<Query, BaseViewHolder> {
    private int[] courseSelectedId;
    private String[] courseSelectedName;
    private List<Query> queryList = new ArrayList<>();

    public HomeCourseTeacherAdapter(ArrayList<Query> data) {
        super(R.layout.layout_home_course, data);
        courseSelectedId = new int[data.size()];
        courseSelectedName = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            courseSelectedId[i] = data.get(i).getId();
            courseSelectedName[i] = data.get(i).getCourseName();
        }
        queryList.clear();
        queryList.addAll(data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Query query) {
      /*  helper.setText(R.id.text, course.getTitle());
        helper.setImageResource(R.id.icon, course.getImageResource());
        // 加载网络图片
        Glide.with(mContext).load(course.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));*/

        viewHolder.setText(R.id.lhc_tv_title, query.getCourseName())
                .setText(R.id.lhc_tv_teacher_name, query.getTeacherName())
                .setText(R.id.lhc_iv_collect, "简述")
                .addOnClickListener(R.id.lhc_iv_collect);
        if (!TextUtils.isEmpty(query.getClass_image())) {
            NetWorkImageView netWorkImageView = viewHolder.getView(R.id.lhc_iv_course_icon);
            netWorkImageView.setImageURL(Constant.getBaseUrl() + "upload/" + query.getClass_image());
        }
    }

    public int[] getCilckedCourseIdArray() {
        Log.v("tanxinkui", "" + courseSelectedId.length);
        return courseSelectedId;
    }

    public String[] getCourseSelectedName() {
        return courseSelectedName;
    }
    public List<Query> getQueryList(){
        return queryList;
    }


}
