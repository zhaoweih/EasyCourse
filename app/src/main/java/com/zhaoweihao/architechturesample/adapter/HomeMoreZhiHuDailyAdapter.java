package com.zhaoweihao.architechturesample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.ZhihuDaily;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class HomeMoreZhiHuDailyAdapter extends RecyclerView.Adapter<HomeMoreZhiHuDailyAdapter.ZhihuDailyViewHolder>{

    private final Context context;
    private LayoutInflater inflater;
    private final List<ZhihuDaily.Story> list;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;

    public HomeMoreZhiHuDailyAdapter(Context context, ArrayList<ZhihuDaily.Story> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public HomeMoreZhiHuDailyAdapter.ZhihuDailyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ZhihuDailyViewHolder(inflater.inflate(R.layout.home_more_zhihu_daily_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(HomeMoreZhiHuDailyAdapter.ZhihuDailyViewHolder holder, int position) {
        ZhihuDaily.Story article = list.get(position);
        holder.tvContent.setText(article.getTitle());

        Glide.with(context).load(article.getImages()[0]).into(holder.ivContent);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewClickListener listener) {
        this.listener = listener;
    }

    public void setItemLongClickListener(OnRecyclerViewLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public class ZhihuDailyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView tvContent;
        ImageView ivContent;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public ZhihuDailyViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.content);
            ivContent = itemView.findViewById(R.id.image);

            this.listener = listener;
            itemView.setOnClickListener(this);
            this.longClickListener = longClickListener;
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.OnClick(view, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (longClickListener != null) {
                longClickListener.OnLongClick(view, getLayoutPosition());
            }
            return true;
        }
    }
}
