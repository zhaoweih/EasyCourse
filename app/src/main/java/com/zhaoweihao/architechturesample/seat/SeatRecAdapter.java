package com.zhaoweihao.architechturesample.seat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.data.course.Query;
import com.zhaoweihao.architechturesample.data.seat.Record;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhao weihao on 2018/4/5.
 */

public class SeatRecAdapter extends RecyclerView.Adapter<SeatRecAdapter.SeatRecViewHolder>{

    private final Context context;
    private LayoutInflater inflater;
    private final List<Record> list;


    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;

    public SeatRecAdapter(Context context, ArrayList<Record> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public SeatRecAdapter.SeatRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SeatRecViewHolder(inflater.inflate(R.layout.seat_rec_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(SeatRecAdapter.SeatRecViewHolder holder, int position) {
        Record record = list.get(position);

        holder.studentId.setText(record.getStudentId());
        holder.seat.setText("该同学占了" + record.getClassRow() + "行" + record.getClassColumn() + "列");

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

    public class SeatRecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView studentId, seat;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public SeatRecViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

            studentId = itemView.findViewById(R.id.student_id);
            seat = itemView.findViewById(R.id.seat);

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
