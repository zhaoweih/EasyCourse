package com.zhaoweihao.architechturesample.course;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuerySelectCourseAdapter extends RecyclerView.Adapter<QuerySelectCourseAdapter.QueryViewHolder>{
    private final Context context;
    private LayoutInflater inflater;
    private final List<QuerySelect> list;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;
    public QuerySelectCourseAdapter(Context context, ArrayList<QuerySelect> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public QuerySelectCourseAdapter.QueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuerySelectCourseAdapter.QueryViewHolder(inflater.inflate(R.layout.query_course_student_list_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(QuerySelectCourseAdapter.QueryViewHolder holder, int position) {
        QuerySelect query = list.get(position);
        holder.tv_query_student_list_studentId.setText(query.getStudentId());
        if (position % 2 == 0) {
            holder.imageView.setImageResource(R.drawable.wave_t);
            holder.frameLayout.setBackgroundColor(Color.parseColor("#c2e8fa"));
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

    public class QueryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView tv_query_student_list_studentId;
        ImageView imageView;
        FrameLayout frameLayout;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public QueryViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);


            tv_query_student_list_studentId=itemView.findViewById(R.id.tv_query_student_list_studentId);
            imageView = itemView.findViewById(R.id.image_view);
            frameLayout = itemView.findViewById(R.id.fl);

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
