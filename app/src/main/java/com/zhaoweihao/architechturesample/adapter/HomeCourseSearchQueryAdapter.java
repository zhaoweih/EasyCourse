package com.zhaoweihao.architechturesample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.course.Query;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class HomeCourseSearchQueryAdapter extends RecyclerView.Adapter<HomeCourseSearchQueryAdapter.QueryViewHolder>{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Query> list;

    private final Boolean checkTecOrStu;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;

    public HomeCourseSearchQueryAdapter(Context context, ArrayList<Query> list, Boolean checkTecOrStu) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.checkTecOrStu = checkTecOrStu;
    }

    @Override
    public HomeCourseSearchQueryAdapter.QueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QueryViewHolder(inflater.inflate(R.layout.query_course_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(HomeCourseSearchQueryAdapter.QueryViewHolder holder, int position) {
        Query query = list.get(position);

        if (checkTecOrStu)
            holder.select.setVisibility(View.VISIBLE);

        holder.name.setText(query.getCourseName());
        holder.description.setText(query.getDescription());
        holder.teacherName.setText(query.getTeacherName());
        holder.selectNum.setText(String.valueOf(query.getCourseNum()));

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

    public class QueryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView name,description,teacherName,selectNum;
        ImageView select;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public QueryViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            teacherName = itemView.findViewById(R.id.teacher_name);
            selectNum = itemView.findViewById(R.id.num);
            select = itemView.findViewById(R.id.select);

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
