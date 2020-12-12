package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * @description 产生消息红点
 * @time 2019/3/11 23:48
 */
public class DotView extends LinearLayout {
    @BindView(R.id.lrd_btn_mesg)
    Button lrd_btn_mesg;

    public DotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_red_point, this);
        ButterKnife.bind(this);
    }

    public void setUnReadCount(int num) {
        Log.v("tanxinkuihhh", "dotview:"+num);
        if (num == 0) {
            lrd_btn_mesg.setVisibility(GONE);
            Log.v("tanxinkuihhh", "dotviewinvisible:"+num);
        } else {
            Log.v("tanxinkuihhh", "dotviewvisible:"+num);
            lrd_btn_mesg.setText("" + num);
            lrd_btn_mesg.setVisibility(VISIBLE);
        }
    }

}
