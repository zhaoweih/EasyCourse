package com.zhaoweihao.architechturesample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.course.QueryComment;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeCourseTaskTalkTopicCommentAdapter extends RecyclerView.Adapter<HomeCourseTaskTalkTopicCommentAdapter.QueryViewHolder>{
    private final Context context;
    private LayoutInflater inflater;
    private final List<QueryComment> list;
    private final Boolean checkTecOrStu;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;
    public HomeCourseTaskTalkTopicCommentAdapter(Context context, ArrayList<QueryComment> list, Boolean checkTecOrStu) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.checkTecOrStu = checkTecOrStu;
    }
    @Override
    public HomeCourseTaskTalkTopicCommentAdapter.QueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeCourseTaskTalkTopicCommentAdapter.QueryViewHolder(inflater.inflate(R.layout.query_comment_list_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(HomeCourseTaskTalkTopicCommentAdapter.QueryViewHolder holder, int position) {
        QueryComment query = list.get(position);
     /*   if (checkTecOrStu)
            holder.iv_query_select_course_manage.setVisibility(View.VISIBLE);*/
        holder.tv_query_comment_list_studentId.setText(query.getStudentId()+":");
        holder.tv_query_comment_list_content.setText(query.getContent());

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

        //TextView tv_query_select_course_id,tv_query_select_course_name,tv_query_select_course_teachername;
        TextView tv_query_comment_list_studentId,tv_query_comment_list_content;
        /**
         * tv_query_comment_list_studentId
         * */
        ImageView iv_query_select_course_manage;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public QueryViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

           // tv_query_select_course_id = itemView.findViewById(R.id.tv_query_select_course_id);
            //tv_query_select_course_name = itemView.findViewById(R.id.tv_query_select_course_name);
           // tv_query_select_course_teachername = itemView.findViewById(R.id.tv_query_select_course_teachername);
            //iv_query_select_course_manage= itemView.findViewById(R.id.iv_query_select_course_manage);去掉图片
            tv_query_comment_list_studentId=itemView.findViewById(R.id.tv_query_comment_list_studentId);
            tv_query_comment_list_content=itemView.findViewById(R.id.tv_query_comment_list_content);

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
