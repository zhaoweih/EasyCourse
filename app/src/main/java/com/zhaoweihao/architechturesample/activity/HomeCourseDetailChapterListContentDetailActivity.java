package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.vincent.filepicker.ToastUtil;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.VideoFile;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.activity.example.TestzzVideoplayerActivity;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.AddChapter;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.UpdateChapter;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.Constant;
import com.zhaoweihao.architechturesample.util.HttpUtil;

import org.litepal.crud.DataSupport;
import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

/**
 * @author
 * @description 首页-课程-详细界面-章节-展示章节的列表-章节的视频展示
 * @time 2019/2/13 0:06
 */
public class HomeCourseDetailChapterListContentDetailActivity extends BaseActivity {
    @BindView(R.id.ahcdccd_vv)
    VideoView ahcdccd_vv;

    @BindView(R.id.ahcdccd_tv_video_title)
    TextView ahcdccd_tv_video_title;

    @BindView(R.id.ahcdccd_title_layout)
    TitleLayout ahcdccd_title_layout;

    @BindView(R.id.ahcdcacd_ll_add_video)
    LinearLayout ahcdcacd_ll_add_video;

    @BindView(R.id.ahcdccd_fl_chapter)
    FrameLayout ahcdccd_fl_chapter;

    @BindView(R.id.ahcdccd_fl_chapter_1)
    FrameLayout ahcdccd_fl_chapter_1;

    @BindView(R.id.ahcdccd_iv_submit)
    ImageView ahcdccd_iv_submit;

    @BindView(R.id.ahcdccd_iv_delete)
    ImageView ahcdccd_iv_delete;

    @BindView(R.id.ahcdccd_iv_add_title)
    ImageView ahcdccd_iv_add_title;

    @BindView(R.id.ahcdcacd_ll_show_video)
    LinearLayout ahcdcacd_ll_show_video;

    @BindView(R.id.ahcdccd_fl_chapter_2)
    FrameLayout ahcdccd_fl_chapter_2;

    @BindView(R.id.fullscreen)
    ImageView fullscreen;

    private Button modifyButton;

    private QMUIDialog.EditTextDialogBuilder reSetTitle;

    //本地选择的视频的路径

    private String localVideoUri;

    private File finalFile;

    private QMUITipDialog mLoadingDialog;


    private OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_detail_chapter_list_content_detail);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {
        if (getIntent().getStringExtra("reading_mode").equals("writing")) {
            ahcdccd_fl_chapter_2.setOnClickListener(view -> {
                Intent intent = new Intent(HomeCourseDetailChapterListContentDetailActivity.this, HomeCourseMoreTestActivity.class);
                intent.putExtra("chapter_id", getIntent().getIntExtra("chapter_id", -1));
                intent.putExtra("courseId", getIntent().getIntExtra("course_id", -1));
                startActivity(intent);
            });
        } else {
            ahcdccd_fl_chapter_2.setVisibility(View.INVISIBLE);
        }
        ahcdcacd_ll_add_video.setVisibility(View.GONE);
        mLoadingDialog = new QMUITipDialog.Builder(HomeCourseDetailChapterListContentDetailActivity.this)
                .setTipWord("正在上传视频...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        if (getIntent().getStringExtra("reading_mode").equals("writing")) {
            if (DataSupport.findLast(User.class).getStudentId() == null) {
                modifyButton = ahcdccd_title_layout.getSettingButton();
                modifyButton.setText("修改");
                modifyButton.setVisibility(View.INVISIBLE);
                modifyButton.setOnClickListener(view -> modify());
                ahcdccd_iv_add_title.setVisibility(View.VISIBLE);
                ahcdccd_fl_chapter.setOnClickListener(view -> modifyTitle());
                ahcdccd_iv_delete.setVisibility(View.VISIBLE);
                ahcdccd_iv_delete.setOnClickListener(view -> deletePreviousVideo());
            } else {
                ahcdccd_iv_add_title.setVisibility(View.INVISIBLE);
                ahcdccd_iv_delete.setVisibility(View.INVISIBLE);
            }
        } else {
            ahcdccd_iv_add_title.setVisibility(View.INVISIBLE);
            ahcdccd_iv_delete.setVisibility(View.INVISIBLE);
        }


        ahcdccd_title_layout.setTitle(getIntent().getStringExtra("unit_title"));
        ahcdccd_tv_video_title.setText(getIntent().getStringExtra("chapter_title"));
        playVideos(Constant.BASE_NO_SOLIDI_URL + "/upload/" + getIntent().getStringExtra("chapter_video_url"));


    }

    private void deletePreviousVideo() {
        ahcdcacd_ll_show_video.setVisibility(View.GONE);
        ahcdccd_vv.stopPlayback();
        //ahcdccd_vv.stopPlay();
        ahcdcacd_ll_add_video.setVisibility(View.VISIBLE);
        ahcdcacd_ll_add_video.setOnClickListener(view -> pickVideo());

    }

    private void setFullscreen(String url) {
        fullscreen.setOnClickListener(view -> {
            Intent intent = new Intent(HomeCourseDetailChapterListContentDetailActivity.this, TestzzVideoplayerActivity.class);
            intent.putExtra("videourl", url);
            intent.putExtra("chapter_title", getIntent().getStringExtra("chapter_title"));
            startActivity(intent);
        });
    }

    private void pickVideo() {
        Intent intentVideo = new Intent(HomeCourseDetailChapterListContentDetailActivity.this, VideoPickActivity.class);
        intentVideo.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
        intentVideo.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"mp4"});
        startActivityForResult(intentVideo, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_VIDEO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case com.vincent.filepicker.Constant.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    ArrayList<VideoFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_VIDEO);
                    Toast.makeText(HomeCourseDetailChapterListContentDetailActivity.this, "Url:" + list.get(0).getPath(), Toast.LENGTH_SHORT).show();
                    playVideos(list.get(0).getPath());
                    ahcdcacd_ll_add_video.setVisibility(View.GONE);
                    ahcdcacd_ll_show_video.setVisibility(View.VISIBLE);
                    modifyButton.setVisibility(View.VISIBLE);
                    String url = "https://test.tanxinkui.cn/api/stuffs/upload";
                    //上传单个文件
                    File file = new File(list.get(0).getPath());
                    if (!file.exists()) {
                        ToastUtil.getInstance(this).showToast("文件不存在");
                    }
                    localVideoUri = url;
                    finalFile = file;
                }
                break;
            default:
                break;
        }
    }

    private void modify() {
        new QMUIDialog.MessageDialogBuilder(HomeCourseDetailChapterListContentDetailActivity.this)
                .setTitle("提交修改")
                .setMessage("确定要提交该对该课程视频的修改吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        if (!TextUtils.isEmpty(localVideoUri)) {
                            uploadFile(localVideoUri, finalFile);
                            mLoadingDialog.show();
                        } else {
                            uploadRequest(getIntent().getStringExtra("chapter_video_url"));
                            mLoadingDialog.show();
                        }
                    }
                })
                .show();
    }

    /**
     * 上传文件方法
     *
     * @param url
     * @param file
     */
    private void uploadFile(String url, File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                // 设置type为"multipart/form-data"，不然无法上传参数
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
//                .addFormDataPart("comment", "上传一个图片哈哈哈哈")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            /**
             * 上传失败回调
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("失败");
                Log.d(TAG, "上传失败");
            }

            /**
             * 上传成功回调
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                String uploadString = restResponse.getPayload().toString();
                runOnUiThread(() -> {
                    mLoadingDialog.dismiss();
                    Toast.makeText(HomeCourseDetailChapterListContentDetailActivity.this, uploadString.substring(8, uploadString.length()), Toast.LENGTH_SHORT).show();
                    String res_list = uploadString.substring(8, uploadString.length());
                    uploadRequest(res_list);
                });

            }
        });
    }

    private void uploadRequest(String res_list) {
        UpdateChapter updateChapter = new UpdateChapter();
        updateChapter.setCourse_id(getIntent().getIntExtra("course_id", -1));
        updateChapter.setRes_list(res_list);
        updateChapter.setUnit_id(getIntent().getIntExtra("unit_id", -1));
        updateChapter.setTitle(ahcdccd_tv_video_title.getText().toString());
        String json = new Gson().toJson(updateChapter);
        HttpUtil.sendPostRequest(com.zhaoweihao.architechturesample.util.Constant.UPDATE_CHAPTER_URL + "/" + getIntent().getIntExtra("chapter_id", -1), json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == com.zhaoweihao.architechturesample.util.Constant.SUCCESS_CODE) {
                    runOnUiThread(() -> {
                        mLoadingDialog.dismiss();
                        Toast.makeText(HomeCourseDetailChapterListContentDetailActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }

    private void modifyTitle() {
        reSetTitle = new QMUIDialog.EditTextDialogBuilder(HomeCourseDetailChapterListContentDetailActivity.this)
                .setTitle("原标题：" + getIntent().getStringExtra("chapter_title"))
                .setPlaceholder("请输入新标题!")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        modifyButton.setVisibility(View.VISIBLE);
                        ahcdccd_tv_video_title.setText(reSetTitle.getEditText().getText().toString());

                    }
                });
        reSetTitle.show();
    }

    private void playVideos(String videoUrl) {

        Uri uri = Uri.parse(videoUrl);
       /* String mVideoUrl = videoUrl;
        ahcdccd_vv.setTitle("视频名称");
        //设置视频名称
        ahcdccd_vv.setToggleExpandable(true);
        //true-启用横竖屏切换按钮  false-隐藏横竖屏切换韩流

        ahcdccd_vv.setVideoUri(this, mVideoUrl);
        //设置视频路径
        // mVp.startPlay();//开始播放

        //也可以直接设置路径并播放
        ahcdccd_vv.loadAndStartVideo(this, mVideoUrl);
        // 设置视频播放路径并开始播放
        //mVp.setFullScreenImmediately(); // 直接使用横屏显示*/
        //设置视频控制器
        ahcdccd_vv.setMediaController(new MediaController(this));

        //播放完成回调
        ahcdccd_vv.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //设置视频路径
        ahcdccd_vv.setVideoURI(uri);

        //开始播放视频
        ahcdccd_vv.start();

        setFullscreen(videoUrl);

    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(HomeCourseDetailChapterListContentDetailActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }


}
