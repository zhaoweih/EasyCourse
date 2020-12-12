package com.zhaoweihao.architechturesample.ui;


import android.content.Context;

import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *@description
 *搜索框界面的模板，实际上是一个搜索button的功能
 *可以设置hinttext,例如：setSearchTip("搜索");
 * @author tanxinkui
 * @date 2019/1/8
 */

public class SearchBoardLayout extends LinearLayout {
    @BindView(R.id.lsb_btn_search)
    com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton lsb_btn_search;
    public SearchBoardLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_search_board, this);
        ButterKnife.bind(this);
    }
    public void setSearchTip(String tip){
        lsb_btn_search.setText(tip);
    }
}
