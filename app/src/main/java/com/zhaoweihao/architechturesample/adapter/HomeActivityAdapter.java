package com.zhaoweihao.architechturesample.adapter;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.AddAndShowActivity;
import com.zhaoweihao.architechturesample.bean.HuoDong;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.util.Constant;

import java.util.List;

/**
 * @author tanxinkui
 * @description 用于活动的Adapter
 * @time 2019/1/22 16:34
 */
public class HomeActivityAdapter extends BaseQuickAdapter<AddAndShowActivity, BaseViewHolder> {
    public HomeActivityAdapter(List<AddAndShowActivity> data) {
        super(R.layout.layout_home_activity, data);
    }
    @Override
    protected void convert(BaseViewHolder viewHolder, AddAndShowActivity huoDong) {
        viewHolder.setText(R.id.lhd_tag, huoDong.getTags())
                .setText(R.id.lhd_tv_title, huoDong.getTitle())
                .setText(R.id.lhd_tv_during, TimeUtils.millis2String(huoDong.getStart_time()).substring(0,10)+" ~ "+TimeUtils.millis2String(huoDong.getEnd_time()).substring(0,10));
        NetWorkImageView netWorkImageView=viewHolder.getView(R.id.lhd_iv);
        netWorkImageView.setImageURL(Constant.getBaseUrl() + "upload/" +huoDong.getImg_url());
    }
}
