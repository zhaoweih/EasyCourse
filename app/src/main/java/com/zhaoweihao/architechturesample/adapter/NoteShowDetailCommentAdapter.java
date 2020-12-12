package com.zhaoweihao.architechturesample.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.GetNoteComment;

import java.util.List;

public class NoteShowDetailCommentAdapter extends BaseQuickAdapter<GetNoteComment, BaseViewHolder> {
    public NoteShowDetailCommentAdapter(@Nullable List<GetNoteComment> data) {
        super(R.layout.layout_show_detail_note, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetNoteComment item) {
        helper.setText(R.id.lsdn_name, item.getUser_name()+":")
                .setText(R.id.lsdn_content, item.getContent());
    }
}
