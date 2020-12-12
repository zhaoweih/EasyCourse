package com.zhaoweihao.architechturesample.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.SubmitAndShowResource;

import java.util.List;

public class HomeResourceAdapter extends BaseQuickAdapter<SubmitAndShowResource, BaseViewHolder> {
    public HomeResourceAdapter(@Nullable List<SubmitAndShowResource> data) {
        super(R.layout.layout_resource, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, SubmitAndShowResource item) {
        viewHolder
                .setText(R.id.lr_tv_time, item.getRes_time())
                .setText(R.id.lr_tv_title, item.getRes_name())
                .setText(R.id.lr_iv_tag, "下载")
                .setImageResource(R.id.lr_iv_resource_icon, getDrawable(parseFormat(item.getRes_url())))
                .addOnClickListener(R.id.lr_iv_tag);
    }
    private int getDrawable(String suffix) {
        int[] drawable = {R.drawable.doc_icon, R.drawable.excel_icon, R.drawable.ppt_icon, R.drawable.pdf_icon};
        int finaldwrable = drawable[3];
        switch (suffix) {
            case "doc":
            case "docx":
                finaldwrable = drawable[0];
                break;
            case "xls":
            case "xlsx":
                finaldwrable = drawable[1];
                break;
            case "ppt":
            case "pptx":
                finaldwrable = drawable[2];
                break;
            default:
                break;
        }
        return finaldwrable;
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
