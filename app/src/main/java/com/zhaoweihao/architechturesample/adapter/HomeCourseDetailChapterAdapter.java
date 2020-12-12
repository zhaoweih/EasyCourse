package com.zhaoweihao.architechturesample.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.GetUnit;

import java.util.List;
/**
*@description 首页-课程-详细界面-课程章节-展示单元
*@author
*@time 2019/2/8 23:31
*/
public class HomeCourseDetailChapterAdapter extends BaseQuickAdapter <GetUnit,BaseViewHolder>{
    private int[] unitSelectedId;
    private String[] unitSeletedTitle;
    public HomeCourseDetailChapterAdapter(@Nullable List<GetUnit> data) {
        super(R.layout.layout_home_course_detail_chapter,data);
        unitSelectedId=new int[data.size()];
        unitSeletedTitle=new String[data.size()];
        for(int i=0;i<data.size();i++){
            unitSelectedId[i]=data.get(i).getId();
            unitSeletedTitle[i]=data.get(i).getTitle();
        }
    }
    @Override
    protected void convert(BaseViewHolder viewHolder,GetUnit getUnit){
        Log.v("tanxiiii","adpater44478889"+getUnit.getTitle());
        viewHolder.setText(R.id.lhcdc_tv_title,getUnit.getTitle());
    }
    public int[] getUnitSelectedIdArray(){
        return unitSelectedId;
    }
    public String[] getUnitSelectedTitleArray(){
        return unitSeletedTitle;
    }
}
