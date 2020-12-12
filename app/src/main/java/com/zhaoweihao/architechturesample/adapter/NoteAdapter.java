package com.zhaoweihao.architechturesample.adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.MessageFriendProfileActivity;
import com.zhaoweihao.architechturesample.bean.AddLike;
import com.zhaoweihao.architechturesample.bean.Course;
import com.zhaoweihao.architechturesample.bean.GetNoteComment;
import com.zhaoweihao.architechturesample.bean.NoteUi;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowNote;
import com.zhaoweihao.architechturesample.bean.UserInfoByUsername;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.EasyCourseApplication.getContext;


public class NoteAdapter extends BaseQuickAdapter<ShowNote, BaseViewHolder> {
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private String keyword;
    private Boolean isNeedShowSelectImage = false;
    private ArrayList<String> avatar = new ArrayList<>();
    private ArrayList<String> stuOrTea = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private List<ShowNote> originShowNote = new ArrayList<>();
    private List<UserInfoByUsername> userInfoByUsernames = new ArrayList<>();
    private List<List<GetNoteComment>> noteCommentList = new ArrayList<List<GetNoteComment>>();
    private UserInfoByUsername userInfoByUsername;
    /*public NoteAdapter(List<NoteUi> data) {
        super(R.layout.layout_note, data);
    }
    @Override
    protected void convert(BaseViewHolder viewHolder, NoteUi noteUi) {
        viewHolder.setImageDrawable(R.id.ln_iv_head, noteUi.getUserDrawable())
                .setText(R.id.ln_title, noteUi.getTitle())
                .setText(R.id.ln_author,noteUi.getAuthor())
                .setText(R.id.ln_date,noteUi.getDate())
                .setText(R.id.ln_iv_type,noteUi.getType())
                .setText(R.id.ln_contentTitle,noteUi.getContentTitle())
                .setText(R.id.ln_content,noteUi.getContent())
                .setText(R.id.ln_comment,""+noteUi.getComment())
                .setText(R.id.ln_likes,""+noteUi.getLikes())
                .addOnClickListener(R.id.ln_iv_type)
                .addOnClickListener(R.id.ln_fl_comment)
                .addOnClickListener(R.id.ln_fl_likes)
                .addOnClickListener(R.id.ln_fl_share_note)
                .addOnClickListener(R.id.ln_ll_whole_note);
        int[] drawable=new int[]{R.id.ln_iv_1,R.id.ln_iv_2,R.id.ln_iv_3,R.id.ln_iv_4,R.id.ln_iv_5,R.id.ln_iv_6};
        if(noteUi.getPic().length!=4){
            for(int i=0;i<noteUi.getPic().length;i++){
                viewHolder.setImageDrawable(drawable[i],noteUi.getPic()[i]);
            }
        }
        if(noteUi.getPic().length<=3){
            viewHolder.setGone(R.id.ln_iv_4,false);
            viewHolder.setGone(R.id.ln_iv_5,false);
            viewHolder.setGone(R.id.ln_iv_6,false);
        }
        if(noteUi.getPic().length==4){
            viewHolder.setVisible(R.id.ln_iv_3,false);
            viewHolder.setVisible(R.id.ln_iv_6,false);
            viewHolder.setImageDrawable(drawable[0],noteUi.getPic()[0]);
            viewHolder.setImageDrawable(drawable[1],noteUi.getPic()[1]);
            viewHolder.setImageDrawable(drawable[3],noteUi.getPic()[2]);
            viewHolder.setImageDrawable(drawable[4],noteUi.getPic()[3]);
        }
    }
*/

    public NoteAdapter(List<ShowNote> data, List<List<GetNoteComment>> noteCommentList1, List<UserInfoByUsername> userInfoByUsernames1) {
        super(R.layout.layout_note, data);
        noteCommentList.clear();
        userInfoByUsernames.clear();
        originShowNote.clear();
        noteCommentList = noteCommentList1;
        userInfoByUsernames = userInfoByUsernames1;
        originShowNote = data;
       /* originShowNote.clear();
        originShowNote.addAll(data);
        initNoteComment();
        initOriginUserInfoList();*/
    }

    public NoteAdapter(List<ShowNote> data, String keyword) {
        super(R.layout.layout_note, data);
        this.keyword = keyword;
       /* originShowNote.clear();
        originShowNote.addAll(data);
        initNoteComment();
        initOriginUserInfoList();*/
    }

    public NoteAdapter(List<ShowNote> data) {
        super(R.layout.layout_note, data);
    }

    public NoteAdapter(List<ShowNote> data, Boolean isNeedShowSelectImage) {
        super(R.layout.layout_note, data);
        this.isNeedShowSelectImage = isNeedShowSelectImage;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, ShowNote showNote) {
        viewHolder.setText(R.id.ln_title, "--")
                .setText(R.id.ln_date, TimeUtils.millis2String(showNote.getTime()))
                .setText(R.id.ln_iv_type, showNote.getTag())
                .setText(R.id.ln_contentTitle, showNote.getTitle())
                .setText(R.id.ln_content, showNote.getContent())
                .setText(R.id.ln_comment, "0")
                .setText(R.id.ln_likes, "" + showNote.getLike_num())
                .addOnClickListener(R.id.ln_iv_type)
                .addOnClickListener(R.id.ln_fl_comment)
                .addOnClickListener(R.id.ln_fl_likes)
                .addOnClickListener(R.id.ln_fl_share_note)
                .addOnClickListener(R.id.ln_ll_whole_note)
                .addOnClickListener(R.id.ln_iv_selector);
        enableSelectImage(viewHolder, showNote);
        TextView contentView = viewHolder.getView(R.id.ln_content);
        TextView titleView = viewHolder.getView(R.id.ln_contentTitle);
        TextView NameView = viewHolder.getView(R.id.ln_title);
        TextView commentNum = viewHolder.getView(R.id.ln_comment);
        getNoteComment(showNote.getId(), commentNum);
        if (!TextUtils.isEmpty(keyword)) {
            contentView.setText(matcherSearchText(getContext().getResources().getColor(R.color.colorBlue), showNote.getContent(), keyword));
            titleView.setText(matcherSearchText(getContext().getResources().getColor(R.color.colorBlue), showNote.getTitle(), keyword));
        }
        NetWorkImageView headiv = viewHolder.getView(R.id.ln_iv_head);
        FrameLayout likeCilck = viewHolder.getView(R.id.ln_fl_likes);
        ImageView likeImageView = viewHolder.getView(R.id.ln_iv_like);
        TextView likeTextView = viewHolder.getView(R.id.ln_likes);

        int likeNum = showNote.getLike_num();

        likeCilck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showNote.getAddedLike() == null) {
                    likeImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.liked));
                    final int likenum1 = likeNum + 1;
                    likeTextView.setText("" + likenum1);
                    likeTextView.setTextColor(getContext().getResources().getColor(R.color.colorBlue));
                    showNote.setAddedLike(true);
                    addLike(showNote.getId());
                    Toast.makeText(getContext(), "点了赞", Toast.LENGTH_SHORT).show();
                } else {
                    if (showNote.getAddedLike()) {
                        likeTextView.setText("" + likeNum);
                        likeTextView.setTextColor(getContext().getResources().getColor(R.color.black));
                        showNote.setAddedLike(false);
                        likeImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.likes));
                        Toast.makeText(getContext(), "取消了点赞！", Toast.LENGTH_SHORT).show();
                    } else {
                        likeImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.liked));
                        final int likenum1 = likeNum + 1;
                        likeTextView.setText("" + likenum1);
                        likeTextView.setTextColor(getContext().getResources().getColor(R.color.colorBlue));
                        showNote.setAddedLike(true);
                        addLike(showNote.getId());
                        Toast.makeText(getContext(), "点了赞", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (showNote.getAddedLike() != null && showNote.getAddedLike()) {
            final int likenum1 = likeNum + 1;
            likeTextView.setText("" + likenum1);
            likeTextView.setTextColor(getContext().getResources().getColor(R.color.colorBlue));
            likeImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.liked));
        } else {
            likeTextView.setText("" + likeNum);
            likeTextView.setTextColor(getContext().getResources().getColor(R.color.black));
            likeImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.likes));

        }
        TextView textView = viewHolder.getView(R.id.ln_author);


       /* if (!TextUtils.isEmpty(DataSupport.findLast(User.class).getAvatar())) {

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.userprofile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(getContext())
                    .load(Constant.getBaseUrl() + "upload/" + DataSupport.findLast(User.class).getAvatar())
                    .apply(requestOptions)
                    .into(headiv);
            //Glide.with(getContext()).load(Constant.getBaseUrl() + "upload/" +DataSupport.findLast(User.class).getAvatar()).apply(requestOptions).into(headiv);
            //headiv.setImageURL(Constant.getBaseUrl() + "upload/" +DataSupport.findLast(User.class).getAvatar());
        }*/
        int picNum = 0;
        ArrayList<String> arrPicUrl = new ArrayList<>();


        if (showNote.getResoucrs() != null) {
            //arrPicUrl=showNote.getResoucrs();
            //List<String> arr1=JSON.parseArray(showNote.getResoucrs().toString(),String.class);
            // picNum=arrPicUrl.size();
            //String[] arr1= new Gson().fromJson(showNote.getResoucrs().toString(), String[].class);
           /* for (int i = 0; i < arr1.length; i++) {
                arrPicUrl.add(arr1[i]);
            }*/
            if (showNote.getResoucrs().contains(",")) {
                String[] arr1 = showNote.getResoucrs().split(",");
                picNum = arr1.length;
                for (int i = 0; i < arr1.length; i++) {
                    arrPicUrl.add(arr1[i]);
                }
            } else {
                picNum = 1;
                Log.v("tanxnk", "showNote.g" + showNote.getResoucrs());
                arrPicUrl.add(showNote.getResoucrs());
            }
        }

        final int[] drawable = new int[]{R.id.ln_iv_1, R.id.ln_iv_2, R.id.ln_iv_3, R.id.ln_iv_4, R.id.ln_iv_5, R.id.ln_iv_6};
        ArrayList<ImageView> netPics = new ArrayList<>();
        clearImageView(viewHolder, drawable);
        if (picNum != 4) {
            if (picNum == 0) {
                goneImageView(viewHolder, drawable);
            } else {
                for (int i = 0; i < picNum; i++) {
                    netPics.add(viewHolder.getView(drawable[i]));
                    netPics.get(i).setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(Constant.getBaseUrl() + "upload/" + arrPicUrl.get(i)).thumbnail(Glide.with(getContext()).load(R.drawable.loading)).apply(new RequestOptions().fitCenter()).into(netPics.get(i));
                    //netPics.get(i).setImageURL(Constant.getBaseUrl() + "upload/" + arrPicUrl.get(i));
                }
            }
        }

        if (picNum <= 3 && picNum != 0) {
            setVisibleImageView(viewHolder, drawable, picNum);
            viewHolder.setGone(R.id.ln_iv_4, false);
            viewHolder.setGone(R.id.ln_iv_5, false);
            viewHolder.setGone(R.id.ln_iv_6, false);
        }
        if (picNum == 4) {
            viewHolder.setVisible(R.id.ln_iv_3, false);
            viewHolder.setVisible(R.id.ln_iv_6, false);
            viewHolder.setVisible(drawable[0], true);
            viewHolder.setVisible(drawable[1], true);
            viewHolder.setVisible(drawable[3], true);
            viewHolder.setVisible(drawable[4], true);
            netPics.add(viewHolder.getView(drawable[0]));
            netPics.add(viewHolder.getView(drawable[1]));
            netPics.add(viewHolder.getView(drawable[3]));
            netPics.add(viewHolder.getView(drawable[4]));
            for (int i = 0; i < 4; i++) {
                netPics.get(i).setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(Constant.getBaseUrl() + "upload/" + arrPicUrl.get(i)).thumbnail(Glide.with(getContext()).load(R.drawable.loading)).apply(new RequestOptions().fitCenter()).into(netPics.get(i));
            }
        }

        /*for (int i = 0; i < originShowNote.size(); i++) {
            if (originShowNote.get(i).getId() == showNote.getId()) {
                userInfoByUsername = userInfoByUsernames.get(i);

            }
        }
        avatar.add(userInfoByUsername.getAvatar());
        name.add(userInfoByUsername.getName());
        if (!TextUtils.isEmpty(userInfoByUsername.getAvatar())) {
            try {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        RequestOptions requestOptions = new RequestOptions()
                                .placeholder(R.drawable.userprofile)
                                .diskCacheStrategy(DiskCacheStrategy.NONE);
                        Glide.with(getContext())
                                .load(Constant.getBaseUrl() + "upload/" + userInfoByUsername.getAvatar())
                                .apply(requestOptions)
                                .into(headiv);
                        if (!userInfoByUsername.getUsername().equals(DataSupport.findLast(User.class).getUsername())) {
                            headiv.setOnClickListener(view -> getUserInfomation(userInfoByUsername.getUsername()));
                        }
                        if (userInfoByUsername.getStudentId() == null) {
                            textView.setText("教师");
                            stuOrTea.add("教师");
                        } else {
                            textView.setText("学生");
                            stuOrTea.add("学生");
                        }
                        NameView.setText(userInfoByUsername.getName());
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.v("tanxkk", e.toString());
            }
        }*/

        HttpUtil.sendGetRequest(Constant.GET_USERINFO_BY_ID_AND_TOKEN_URL + showNote.getUser_id() + "&token="
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
                    avatar.add(userInfoByUsername.getAvatar());
                    name.add(userInfoByUsername.getName());
                    if (!TextUtils.isEmpty(userInfoByUsername.getAvatar())) {
                        try {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    RequestOptions requestOptions = new RequestOptions()
                                            .placeholder(R.drawable.userprofile)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE);
                                    Glide.with(getContext())
                                            .load(Constant.getBaseUrl() + "upload/" + userInfoByUsername.getAvatar())
                                            .apply(requestOptions)
                                            .into(headiv);
                                    if (!userInfoByUsername.getUsername().equals(DataSupport.findLast(User.class).getUsername())) {
                                        headiv.setOnClickListener(view -> getUserInfomation(userInfoByUsername.getUsername()));
                                    }
                                    if (userInfoByUsername.getStudentId() == null) {
                                        textView.setText("教师");
                                        stuOrTea.add("教师");
                                    } else {
                                        textView.setText("学生");
                                        stuOrTea.add("学生");
                                    }
                                    NameView.setText(userInfoByUsername.getName());
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.v("tanxkk", e.toString());
                        }
                    }
                }
            }
        });
    }

    private void clearImageView(BaseViewHolder viewHolder, int[] drawable) {
        for (int i = 0; i < drawable.length; i++) {
            viewHolder.setVisible(drawable[i], false);
        }
    }

    private void goneImageView(BaseViewHolder viewHolder, int[] drawable) {
        for (int i = 0; i < drawable.length; i++) {
            viewHolder.setGone(drawable[i], false);
        }
    }

    private void setVisibleImageView(BaseViewHolder viewHolder, int[] drawable, int num) {
        for (int i = 0; i < num; i++) {
            viewHolder.setVisible(drawable[i], true);
        }
    }

    private void addLike(int note_book_id) {
        AddLike addLike = new AddLike();
        User user = DataSupport.findLast(com.zhaoweihao.architechturesample.database.User.class);
        Log.v("tanxinkui", "use44444");
        if (user == null) {
            return;
        }
        if (!TextUtils.isEmpty(user.getUsername())) {
            addLike.setUser_name(user.getUsername());
            Log.v("tanxinkui", "user.getUsername()" + user.getUsername());
        } else {
            return;
        }
        if (user.getUserId() != null) {
            addLike.setUser_id(user.getUserId());
            Log.v("tanxinkui", "user.getUserID()" + user.getUserId());
        } else {
            return;
        }
        addLike.setNotebook_id(note_book_id);
        String json = new Gson().toJson(addLike);
        Log.v("tanxinkui", "json" + json);
        HttpUtil.sendPostRequest(Constant.ADD_LIKE_NOTEBOOK_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public SpannableString matcherSearchText(int color, String text, String keyword) {
        SpannableString ss = new SpannableString(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    private void enableSelectImage(BaseViewHolder viewHolder, ShowNote showNote) {
        if (isNeedShowSelectImage) {
            viewHolder.getView(R.id.ln_iv_selector).setVisibility(View.VISIBLE);
            if (showNote.getSelected() != null && showNote.getSelected()) {
                viewHolder.getView(R.id.ln_iv_selector).setBackground(getContext().getResources().getDrawable(R.drawable.note_selected));
            } else {
                viewHolder.getView(R.id.ln_iv_selector).setBackground(getContext().getResources().getDrawable(R.drawable.note_selectable));
            }

        } else {
            showNote.setSelected(false);
        }
    }

    public ArrayList<String> getAvatar() {
        return avatar;
    }

    public ArrayList<String> getStuOrTea() {
        return stuOrTea;
    }

    public ArrayList<String> getName() {
        return name;
    }

    private void getNoteComment(int noteId, TextView textView) {

        HttpUtil.sendGetRequest(Constant.GET_NOTEBOOK_COMMENT_BY_ID_URL + noteId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                            List<GetNoteComment> gnc = JSON.parseArray(restResponse.getPayload().toString(), GetNoteComment.class);
                            // noteCommentList.add(gnc);
                            textView.setText("" + gnc.size());
                        } else {
                        }
                    }
                });
            }
        });
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
                            intent.putExtra("mode", "输入添加好友");
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

    public List<List<GetNoteComment>> getNoteCommentList() {
        return noteCommentList;
    }
}
