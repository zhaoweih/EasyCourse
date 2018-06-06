package com.zhaoweihao.architechturesample.vote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.vote.Select;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ResultListViewHolder>{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Select> list;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;

    public ResultListAdapter(Context context, List<Select> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ResultListAdapter.ResultListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResultListViewHolder(inflater.inflate(R.layout.result_item,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(ResultListAdapter.ResultListViewHolder holder, int position) {
        Select select = list.get(position);

        holder.title.setText(select.getTitle());
        holder.choiceA.setText(select.getChoiceA());
        holder.numA.setText(String.valueOf(select.getNumA()));
        holder.choiceB.setText(select.getChoiceB());
        holder.numB.setText(String.valueOf(select.getNumB()));
        holder.choiceC.setText(select.getChoiceC());
        holder.numC.setText(String.valueOf(select.getNumC()));
        holder.choiceD.setText(select.getChoiceD());
        holder.numD.setText(String.valueOf(select.getNumD()));


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

    public class ResultListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView title,choiceA,choiceB,choiceC,choiceD,numA,numB,numC,numD;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public ResultListViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            choiceA = itemView.findViewById(R.id.tv_a);
            numA = itemView.findViewById(R.id.tv_a_num);
            choiceB = itemView.findViewById(R.id.tv_b);
            numB = itemView.findViewById(R.id.tv_b_num);
            choiceC = itemView.findViewById(R.id.tv_c);
            numC = itemView.findViewById(R.id.tv_c_num);
            choiceD = itemView.findViewById(R.id.tv_d);
            numD = itemView.findViewById(R.id.tv_d_num);

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
