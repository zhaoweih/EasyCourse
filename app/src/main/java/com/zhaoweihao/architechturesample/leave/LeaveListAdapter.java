package com.zhaoweihao.architechturesample.leave;

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
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.LeaveListViewHolder>{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Leave> list;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;

    public LeaveListAdapter(Context context, ArrayList<Leave> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public LeaveListAdapter.LeaveListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LeaveListViewHolder(inflater.inflate(R.layout.leave_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(LeaveListAdapter.LeaveListViewHolder holder, int position) {
        Leave leave = list.get(position);
        holder.applicant.setText("申请人: " + leave.getStudentId());
        holder.content.setText("申请内容: " + leave.getContent());

        switch (leave.getStatus()) {
            case 1:
                Glide.with(context).load(R.drawable.wait).into(holder.status);
                break;
            case 2:
                Glide.with(context).load(R.drawable.fail).into(holder.status);
                break;
            case 3:
                Glide.with(context).load(R.drawable.success).into(holder.status);
                break;
                default:
        }

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

    public class LeaveListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView content;
        TextView applicant;
        ImageView status;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public LeaveListViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            applicant = itemView.findViewById(R.id.applicant);
            status = itemView.findViewById(R.id.iv_status);

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
