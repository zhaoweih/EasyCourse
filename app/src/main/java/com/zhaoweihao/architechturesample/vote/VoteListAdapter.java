package com.zhaoweihao.architechturesample.vote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.Leave;
import com.zhaoweihao.architechturesample.data.vote.Add;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class VoteListAdapter extends RecyclerView.Adapter<VoteListAdapter.VoteListViewHolder>{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Add> list;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;

    public VoteListAdapter(Context context, ArrayList<Add> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public VoteListAdapter.VoteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VoteListViewHolder(inflater.inflate(R.layout.vote_item,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(VoteListAdapter.VoteListViewHolder holder, int position) {
        Add add = list.get(position);

        holder.title.setText(add.getTitle());
        holder.choiceNum.setText(String.valueOf(add.getChoiceNum()));

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

    public class VoteListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView title;
        TextView choiceNum;
        TextView startDate;
        TextView endDate;
        TextView choiceMode;


        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public VoteListViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            choiceNum = itemView.findViewById(R.id.choice_num);


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
