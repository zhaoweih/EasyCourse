package com.zhaoweihao.architechturesample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.quiz.Query;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class HomeCourseMoreQuizRankAdapter extends RecyclerView.Adapter<HomeCourseMoreQuizRankAdapter.QuizViewHolder>{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Query> list;


    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;

    public HomeCourseMoreQuizRankAdapter(Context context, ArrayList<Query> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public HomeCourseMoreQuizRankAdapter.QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuizViewHolder(inflater.inflate(R.layout.quiz_item,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(HomeCourseMoreQuizRankAdapter.QuizViewHolder holder, int position) {
        Query query = list.get(position);

        holder.name.setText(query.getStudentName());
        holder.studentId.setText(""+query.getStudentId());
        holder.quizNum.setText(""+query.getQuizNum());
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

    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView name,studentId,quizNum;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public QuizViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            studentId = itemView.findViewById(R.id.student_id);
            quizNum = itemView.findViewById(R.id.quiz_num);

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
