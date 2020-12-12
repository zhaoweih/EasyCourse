package com.zhaoweihao.architechturesample.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.MessageFriendProfileActivity;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.bean.course.QueryClassmate;
import com.zhaoweihao.architechturesample.bean.course.QuerySelect;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.EasyCourseApplication.getContext;

public class HomeCourseClassmateQueryAdapter extends RecyclerView.Adapter<HomeCourseClassmateQueryAdapter.QueryViewHolder> {
    private final Context context;
    private LayoutInflater inflater;
    private final List<QueryClassmate> list;

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public HomeCourseClassmateQueryAdapter(Context context, ArrayList<QueryClassmate> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public HomeCourseClassmateQueryAdapter.QueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return new HomeCourseClassmateQueryAdapter.QueryViewHolder(inflater.inflate(R.layout.query_course_student_list_layout,parent,false),listener,longClickListener);
        return new HomeCourseClassmateQueryAdapter.QueryViewHolder(inflater.inflate(R.layout.layout_address_list, parent, false), listener, longClickListener);
    }

    @Override
    public void onBindViewHolder(HomeCourseClassmateQueryAdapter.QueryViewHolder holder, int position) {
        QueryClassmate query = list.get(position);
        holder.textView.setText(query.getStudentId());

        //holder.tv_query_student_list_studentId.setText(query.getStudentId());
       /* if (position % 2 == 0) {
            holder.imageView.setImageResource(R.drawable.wave_t);
            holder.frameLayout.setBackgroundColor(Color.parseColor("#c2e8fa"));
        }*/
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_ID_AND_TOKEN_URL + query.getStuId() + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                    UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);

                    try {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(userInfoByUsername.getAvatar())) {
                                    RequestOptions requestOptions = new RequestOptions()
                                            .placeholder(R.drawable.userprofile)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE);
                                    Glide.with(getContext())
                                            .load(Constant.getBaseUrl() + "upload/" + userInfoByUsername.getAvatar())
                                            .apply(requestOptions)
                                            .into(holder.imageView);
                                }
                                holder.textView.setText(query.getStudentId() + "(" + userInfoByUsername.getName() + ")");
                                if (!userInfoByUsername.getUsername().equals(DataSupport.findLast(User.class).getUsername())) {
                                    holder.linearLayout.setOnClickListener(view -> getUserInfomation(userInfoByUsername.getUsername()));
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("tanxkk", e.toString());
                    }

                }
            }
        });
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

    public class QueryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        /* TextView tv_query_student_list_studentId;
         ImageView imageView;
         FrameLayout frameLayout;*/
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public QueryViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);


            /*tv_query_student_list_studentId=itemView.findViewById(R.id.tv_query_student_list_studentId);
            imageView = itemView.findViewById(R.id.image_view);
            frameLayout = itemView.findViewById(R.id.fl);*/
            textView = itemView.findViewById(R.id.lal_tv_title);
            imageView = itemView.findViewById(R.id.lal_iv_course_icon);
            linearLayout = itemView.findViewById(R.id.lal_ly_main);

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

    private void getUserInfomation(String username) {
        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_USERNAME_AND_TOKEN_URL + username + "&token="
                + DataSupport.findLast(User.class).getToken(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "找不到该好友", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                            UserInfoByUsername userInfoByUsername = JSON.parseObject(restResponse.getPayload().toString(), UserInfoByUsername.class);
                            Intent intent = new Intent(getContext(), MessageFriendProfileActivity.class);
                            intent.putExtra("userInfoByUsername", userInfoByUsername);
                            intent.putExtra("mode", "发送消息");
                            intent.putExtra("username", username);
                            getContext().startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "找不到该好友", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
