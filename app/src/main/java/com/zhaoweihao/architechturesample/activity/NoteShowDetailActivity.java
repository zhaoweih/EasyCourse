package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.adapter.NoteShowDetailCommentAdapter;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.AddNoteComment;
import com.zhaoweihao.architechturesample.bean.GetNoteComment;
import com.zhaoweihao.architechturesample.bean.Leave;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.ShowNote;
import com.zhaoweihao.architechturesample.bean.course.SendComment;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.NetWorkImageView;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.EasyCourseApplication.getContext;

/**
 * @author
 * @description 笔记-显示笔记具体内容
 * @time 2019/3/2 15:37
 */
public class NoteShowDetailActivity extends BaseActivity {
    @BindView(R.id.ansd_titleLayout)
    TitleLayout ansd_titleLayout;

    @BindView(R.id.ansd_iv_head)
    NetWorkImageView ansd_iv_head;

    @BindView(R.id.ansd_title)
    TextView ansd_title;

    @BindView(R.id.ansd_author)
    TextView ansd_author;

    @BindView(R.id.ansd_date)
    TextView ansd_date;

    @BindView(R.id.ansd_iv_type)
    QMUIRoundButton ansd_iv_type;

    @BindView(R.id.ansd_contentTitle)
    TextView ansd_contentTitle;

    @BindView(R.id.ansd_content)
    TextView ansd_content;

    @BindView(R.id.ansd_iv_1)
    ImageView ansd_iv_1;
    @BindView(R.id.ansd_iv_2)
    ImageView ansd_iv_2;
    @BindView(R.id.ansd_iv_3)
    ImageView ansd_iv_3;
    @BindView(R.id.ansd_iv_4)
    ImageView ansd_iv_4;
    @BindView(R.id.ansd_iv_5)
    ImageView ansd_iv_5;
    @BindView(R.id.ansd_iv_6)
    ImageView ansd_iv_6;

    @BindView(R.id.ansd_comment)
    TextView ansd_comment;
    @BindView(R.id.ansd_likes)
    TextView ansd_likes;
    @BindView(R.id.ansd_fl_comment)
    FrameLayout ansd_fl_comment;
    @BindView(R.id.ansd_recyclerView)
    RecyclerView ansd_recyclerView;
    private NoteShowDetailCommentAdapter noteShowDetailCommentAdapter;
    private int picNum = 0;
    private ArrayList<String> arrPicUrl = new ArrayList<>();
    ArrayList<ImageView> netPics = new ArrayList<>();
    private ShowNote showNote = new ShowNote();
    private List<GetNoteComment> gnc = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_show_detail);
        ButterKnife.bind(this);
        init();
    }
    /*
    *      viewHolder.setText(R.id.ln_title, DataSupport.findLast(User.class).getName())
                .setText(R.id.ln_date, TimeUtils.millis2String(showNote.getTime()))
                .setText(R.id.ln_iv_type, showNote.getTag())
                .setText(R.id.ln_contentTitle, showNote.getTitle())
                .setText(R.id.ln_content, showNote.getContent())
                .setText(R.id.ln_comment, "0")
                .setText(R.id.ln_likes, "" + showNote.getLike_num())
    * */

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ansd_recyclerView.setLayoutManager(layoutManager);
        ImageView[] imageViews = {ansd_iv_1, ansd_iv_2, ansd_iv_3, ansd_iv_4, ansd_iv_5, ansd_iv_6};
        netPics.clear();
        for (ImageView imageView : imageViews) {
            netPics.add(imageView);
        }

        Intent intent = getIntent();
        showNote = (ShowNote) intent.getSerializableExtra("note");

        /*ImageSpan imgSpan = new ImageSpan(this, R.drawable.shareicon);
        SpannableString spannableString = new SpannableString("01234" + showNote.getTag());
        spannableString.setSpan(imgSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ansd_titleLayout.getTitleTextView().setText(spannableString);*/
        ansd_titleLayout.setTitle(showNote.getTag() + "详情");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("avatar"))) {
            ansd_iv_head.setImageURL(Constant.getBaseUrl() + "upload/" + getIntent().getStringExtra("avatar"));
        }
        ansd_title.setText(getIntent().getStringExtra("name"));
        ansd_author.setText(getIntent().getStringExtra("teaOrStu"));
        ansd_date.setText(TimeUtils.millis2String(showNote.getTime()));
        ansd_content.setText(showNote.getContent());
        ansd_contentTitle.setText(showNote.getTitle());
        ansd_iv_type.setText(showNote.getTag());
        ansd_comment.setText("0");
        ansd_likes.setText("" + showNote.getLike_num());

        if (showNote.getResoucrs() != null) {
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
        for (int i = 0; i < picNum; i++) {
            Glide.with(getContext()).load(Constant.getBaseUrl() + "upload/" + arrPicUrl.get(i)).thumbnail(Glide.with(getContext()).load(R.drawable.loading)).apply(new RequestOptions().fitCenter()).into(netPics.get(i));
        }
        if (picNum < 6) {
            for (int i = picNum; i < 6; i++) {
                netPics.get(i).setVisibility(View.GONE);
            }
        }
        gnc = (List<GetNoteComment>) getIntent().getSerializableExtra("commentList");
        ansd_fl_comment.setOnClickListener(view -> sendComment(showNote.getUser_id()));
        initAdapter();
    }

    private void initAdapter() {
        ansd_comment.setText("" + gnc.size());
        noteShowDetailCommentAdapter = new NoteShowDetailCommentAdapter(gnc);
        ansd_recyclerView.setAdapter(noteShowDetailCommentAdapter);
    }

    private void sendComment(int ownerID) {
        AddNoteComment sendComment = new AddNoteComment();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入您的评论");
        builder.setIcon(R.drawable.comment1);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setBackgroundColor(0x665544);
        input.setTextAlignment(input.TEXT_ALIGNMENT_CENTER);
        builder.setView(input);
        builder.setPositiveButton("发送", (dialog, which) -> {


            if (TextUtils.isEmpty(input.getText().toString())) {
                Toast.makeText(NoteShowDetailActivity.this, "请填写评论！", Toast.LENGTH_SHORT).show();
            } else {
                sendComment.setContent(input.getText().toString());
                sendComment.setOwner_id(ownerID);
                if (!TextUtils.isEmpty(DataSupport.findLast(User.class).getAvatar())) {
                    sendComment.setUser_avatar(DataSupport.findLast(User.class).getAvatar());
                }
                sendComment.setUser_id(DataSupport.findLast(User.class).getUserId());
                sendComment.setTime(System.currentTimeMillis());
                sendComment.setUser_name(DataSupport.findLast(User.class).getUsername());
                sendComment.setNotebook_id(showNote.getId());
                sendAction(sendComment);
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    private void sendAction(AddNoteComment addNoteComment) {
        String json = new Gson().toJson(addNoteComment);
        HttpUtil.sendPostRequest(Constant.ADD_NOTEBOOK_COMMENT_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                runOnUiThread(() -> {
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        Toast.makeText(NoteShowDetailActivity.this, "成功发送评论！", Toast.LENGTH_SHORT).show();
                        getNoteComment(showNote.getId());
                    }
                });
            }
        });
    }

    private void getNoteComment(int noteId) {
        HttpUtil.sendGetRequest(Constant.GET_NOTEBOOK_COMMENT_BY_ID_URL + noteId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = JSON.parseObject(body, RestResponse.class);
                runOnUiThread(() -> {
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        List<GetNoteComment> gnc1 = JSON.parseArray(restResponse.getPayload().toString(), GetNoteComment.class);
                        gnc.clear();
                        gnc.addAll(gnc1);
                        initAdapter();
                    } else {
                        Toast.makeText(NoteShowDetailActivity.this, "刷新评论失败，请退出笔记重试！", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }

}
