package com.zhaoweihao.architechturesample.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 *标题的界面模板
 * @author tanxinkui
 * @date 2019/1/8
 */


public class TitleLayout extends LinearLayout {
    @BindView(R.id.tl_bt_back)
    Button tl_bt_back;
    @BindView(R.id.tl_tv_title)
    TextView tl_tv_title;
    @BindView(R.id.tl_bt_setting)
    Button tl_bt_setting;
    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_title,this);
        ButterKnife.bind(this);
        tl_bt_back.setOnClickListener(view->((Activity)getContext()).finish());
    }
    public void setTitle(String title){
        tl_tv_title.setText(title);
    }
    public TextView getTitleTextView(){
        return tl_tv_title;
    }
    public void setSettingString(String title){
        tl_bt_setting.setText(title);
    }
    public Button getSettingButton(){
        return tl_bt_setting;
    }
}
