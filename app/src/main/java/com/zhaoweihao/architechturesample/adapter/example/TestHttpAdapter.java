package com.zhaoweihao.architechturesample.adapter.example;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.Course;

import java.util.List;

public class TestHttpAdapter extends BaseQuickAdapter<Course, BaseViewHolder> {
    public TestHttpAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Course item) {
//        helper.setText(R.id.text, item.getTitle());
//        helper.setImageResource(R.id.icon, item.getImageResource());
//        // 加载网络图片
//      Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));
        helper.setText(R.id.tv_couse_name, item.getCourseName());
        helper.setText(R.id.tv_teacher_name, item.getTeacherName());
        //添加子控件点击监听器
        helper.addOnClickListener(R.id.btn_join);
        helper.addOnClickListener(R.id.tv_couse_name);

    }
}