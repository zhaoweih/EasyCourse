package com.zhaoweihao.architechturesample.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.database.LocalCourse;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.util.Constant;

import java.util.List;

public class HomeRescentUseAdapter extends BaseQuickAdapter <LocalCourse,BaseViewHolder>{
    public HomeRescentUseAdapter(@Nullable List data) {
        super(R.layout.layout_home_course, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, LocalCourse item) {
        viewHolder.setText(R.id.lhc_tv_title, item.getCourseName())
                .setText(R.id.lhc_tv_teacher_name, item.getTeacherName())
                .setText(R.id.lhc_iv_collect, "简述")
                .addOnClickListener(R.id.lhc_iv_collect);
        if (!TextUtils.isEmpty(item.getClass_image())) {
            NetWorkImageView netWorkImageView = viewHolder.getView(R.id.lhc_iv_course_icon);
            netWorkImageView.setImageURL(Constant.getBaseUrl() + "upload/" + item.getClass_image());
        }
    }
}
