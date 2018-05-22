package com.zhaoweihao.architechturesample.course;

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
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.QueryViewHolder>{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Query> list;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;

    public QueryAdapter(Context context, ArrayList<Query> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public QueryAdapter.QueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QueryViewHolder(inflater.inflate(R.layout.query_course_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(QueryAdapter.QueryViewHolder holder, int position) {
        Query query = list.get(position);

        holder.name.setText("课程名称: " + query.getCourseName());
        holder.description.setText("课程简介: " + query.getDescription());
        holder.teacherName.setText("课程老师: " + query.getTeacherName());
        holder.selectNum.setText("选课人数: " + query.getCourseNum());

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

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public QueryViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            teacherName = itemView.findViewById(R.id.teacher_name);
            selectNum = itemView.findViewById(R.id.num);

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
