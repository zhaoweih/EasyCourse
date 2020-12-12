package com.zhaoweihao.architechturesample.activity;

import android.os.Bundle;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
*@description 首页-主页的搜索框跳转的界面（注：主页搜索框-->当前界面）
* @author tanxinkui
* @date 2019/1/8
*/

public class HomeSearchAllResourceActivity extends BaseActivity {
    @BindView(R.id.amsar_search_board_input_layout)
    com.zhaoweihao.architechturesample.ui.SearchBoardInputLayout amsar_search_board_input_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search_all_resource);
        ButterKnife.bind(this);
        //先设置editext的hintText,然后是限制的历史记录数量，然后是搜索的页面的标签，（标签设置完，在HistoryTag备注一下:home_search_all）
        amsar_search_board_input_layout.initWithArgs("搜索",10,"home_search_all");
    }


}
