package com.zhaoweihao.architechturesample.timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.QuerySelect;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class DoubanMomentAdapter extends RecyclerView.Adapter<DoubanMomentAdapter.DoubanMomentViewHolder> {
    private final Context context;
    private LayoutInflater inflater;
    private final List<QuerySelect> list;
    private final Boolean checkTecOrStu;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;
    public DoubanMomentAdapter(Context context, ArrayList<QuerySelect> list, Boolean checkTecOrStu) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.checkTecOrStu = checkTecOrStu;
    }
    @Override
    public DoubanMomentAdapter.DoubanMomentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoubanMomentAdapter.DoubanMomentViewHolder(inflater.inflate(R.layout.query_select_course_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(DoubanMomentAdapter.DoubanMomentViewHolder holder, int position) {
        QuerySelect query = list.get(position);
        User user3 = DataSupport.findLast(User.class);
        if (user3.getStudentId() == null && !(user3.getTeacherId() == null)) {
            if(query!=null){
                holder.tv_query_select_course_id.setText("课程ID: " + query.getId());
                holder.tv_query_select_course_name.setText("课程名称: " + query.getCourseName());
                holder.tv_query_select_course_teachername.setText("课程老师: " + query.getTeacherName());}
        } else if (!(user3.getStudentId() == null) && user3.getTeacherId() == null) {
            if(query!=null){
                holder.tv_query_select_course_id.setText("课程ID: " + query.getCourseId());
                holder.tv_query_select_course_name.setText("课程名称: " + query.getCourseName());
                holder.tv_query_select_course_teachername.setText("课程老师: " + query.getTeacherName());}
        }

        /*if (checkTecOrStu)
            holder.iv_query_select_course_manage.setVisibility(View.VISIBLE);*/


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

    public class DoubanMomentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView tv_query_select_course_id,tv_query_select_course_name,tv_query_select_course_teachername,tv_query_select_course_teacherId;
        ImageView iv_query_select_course_manage;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public DoubanMomentViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            tv_query_select_course_id = itemView.findViewById(R.id.tv_query_select_course_id);
            tv_query_select_course_name = itemView.findViewById(R.id.tv_query_select_course_name);
            tv_query_select_course_teachername = itemView.findViewById(R.id.tv_query_select_course_teachername);
            tv_query_select_course_teacherId=itemView.findViewById(R.id.tv_query_select_course_teacherId);
            //iv_query_select_course_manage= itemView.findViewById(R.id.iv_query_select_course_manage);去掉图片

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
