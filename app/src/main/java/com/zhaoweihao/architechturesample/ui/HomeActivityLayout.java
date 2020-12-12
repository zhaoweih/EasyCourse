package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.zhaoweihao.architechturesample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
*@description 自定义活动页面的布局
*@author tanxinkui
*@time 2019/1/22 15:55
*/
public class HomeActivityLayout extends LinearLayout {
    @BindView(R.id.lhd_qmui_linearLayout)
    QMUILinearLayout lhd_qmui_linearLayout;

    @BindView(R.id.lhd_iv)
    QMUIRadiusImageView lhd_iv;

    @BindView(R.id.lhd_tag)
    TextView lhd_tag;

    @BindView(R.id.lhd_tv_title)
    TextView lhd_tv_title;

    @BindView(R.id.lhd_tv_during)
    TextView lhd_tv_during;

    @BindView(R.id.lhd_tv_tag)
    TextView lhd_tv_tag;

    public HomeActivityLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_home_activity, this);
        ButterKnife.bind(this);
        lhd_qmui_linearLayout.setShadowElevation(5);
        lhd_qmui_linearLayout.setRadius(30);
    }
    public  void setRadiusAndShadow(){
        lhd_qmui_linearLayout.setShadowElevation(5);
        lhd_qmui_linearLayout.setRadius(30);
    }
    public void initWithArgs(Drawable drawable,String tag,String title,String duration){
        setImage(drawable);
        setTag(tag);
        setTvTitle(title);
        setDuration(duration);
    }
    private void setTag(String tag){
        lhd_tv_tag.setText(tag);
    }
    private void setTvTitle(String title){
        lhd_tv_title.setText(title);
    }
    private void setDuration(String duration){
        lhd_tv_during.setText(duration);
    }
    private void setImage(Drawable drawable){
        lhd_iv.setImageDrawable(drawable);
    }

}
