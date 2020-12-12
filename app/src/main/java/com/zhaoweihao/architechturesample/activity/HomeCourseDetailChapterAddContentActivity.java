package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.ToastUtil;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.VideoFile;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.AddChapter;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.ui.TitleLayout;
import com.zhaoweihao.architechturesample.util.HttpUtil;

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

/**
 * @author
 * @description 首页-课程-详细界面--章节-单元内容（包括单元视频和单元测试）
 * @time 2019/2/10 16:45
 */
public class HomeCourseDetailChapterAddContentActivity extends BaseActivity {
    @BindView(R.id.ahcdcc_title_layout)
    TitleLayout ahcdcc_title_layout;

    @BindView(R.id.ahcdcc_vv)
    VideoView ahcdcc_vv;

    @BindView(R.id.ahcdcac_ll_add_video)
    LinearLayout ahcdcac_ll_add_video;

    @BindView(R.id.ahcdcac_ll_show_video)
    LinearLayout ahcdcac_ll_show_video;

    @BindView(R.id.ahcdcc_iv_submit)
    ImageView ahcdcc_iv_submit;

    @BindView(R.id.ahcdcc_iv_delete)
    ImageView ahcdcc_iv_delete;

    @BindView(R.id.ahcdcc_fl_chapter_1)
    FrameLayout ahcdcc_fl_chapter_1;

    @BindView(R.id.ahcdcc_fl_chapter)
    FrameLayout ahcdcc_fl_chapter;

    @BindView(R.id.ahcdcc_tv_video_title)
    TextView ahcdcc_tv_video_title;

    @BindView(R.id.ahcdcc_iv_video)
    ImageView ahcdcc_iv_video;



    private QMUIDialog.EditTextDialogBuilder setVideoTitleDialog;

    private QMUITipDialog mLoadingDialog;


    private OkHttpClient client = new OkHttpClient();


    private String VIDEO_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/CourseTeachingTemp.mp4";

    private Uri VideoUriOri;

    private Uri VideoUri = Uri.parse(VIDEO_FILE_LOCATION);

    //本地选择的视频的路径

    private String localVideoUri;

    //返回的视频的路径 如：123456.mp4

    private String returnedVideoUri;

    private File finalFile;

    private String finalVideoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_detail_chapter_add_content);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        ahcdcc_title_layout.setTitle(getIntent().getStringExtra("unit_title"));
        ahcdcac_ll_add_video.setOnClickListener(view -> selectVideo());
        ahcdcac_ll_show_video.setVisibility(View.GONE);
        ahcdcc_fl_chapter_1.setVisibility(View.GONE);
        ahcdcc_iv_delete.setOnClickListener(view -> deleteVideo());
        ahcdcc_fl_chapter.setOnClickListener(view -> setVideoTitle());
        mLoadingDialog = new QMUITipDialog.Builder(HomeCourseDetailChapterAddContentActivity.this)
                .setTipWord("正在上传视频...")
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create();
        ahcdcc_iv_submit.setOnClickListener(view -> submitThisChapter());
    }

    private void submitThisChapter() {
        new QMUIDialog.MessageDialogBuilder(HomeCourseDetailChapterAddContentActivity.this)
                .setTitle("提交课程视频")
                .setMessage("确定要提交该课程视频吗？")
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
                        }
                    }
                })
                .show();

    }

    private void setVideoTitle() {
        setVideoTitleDialog = new QMUIDialog.EditTextDialogBuilder(HomeCourseDetailChapterAddContentActivity.this)
                .setTitle("请输入您的视频标题")
                .setPlaceholder("例如：第一节国学经典教学视频")
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
                        ahcdcc_tv_video_title.setText(setVideoTitleDialog.getEditText().getText().toString());
                        finalVideoTitle = setVideoTitleDialog.getEditText().getText().toString();
                        ahcdcc_iv_video.setVisibility(View.INVISIBLE);
                    }
                });
        setVideoTitleDialog.show();
    }

    private void deleteVideo() {
        ahcdcc_iv_submit.setVisibility(View.GONE);
        ahcdcc_fl_chapter_1.setVisibility(View.GONE);
        ahcdcac_ll_show_video.setVisibility(View.GONE);
        ahcdcac_ll_add_video.setVisibility(View.VISIBLE);
        ahcdcc_vv.stopPlayback();
    }

    private void selectVideo() {
        Intent intentVideo = new Intent(HomeCourseDetailChapterAddContentActivity.this, VideoPickActivity.class);
        intentVideo.putExtra(Constant.MAX_NUMBER, 1);
        intentVideo.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"mp4"});
        startActivityForResult(intentVideo, Constant.REQUEST_CODE_PICK_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                    Toast.makeText(HomeCourseDetailChapterAddContentActivity.this, "Url:" + list.get(0).getPath(), Toast.LENGTH_SHORT).show();
                    playVideos2(list.get(0).getPath());
                    ahcdcac_ll_add_video.setVisibility(View.GONE);
                    ahcdcc_fl_chapter_1.setVisibility(View.VISIBLE);
                    ahcdcac_ll_show_video.setVisibility(View.VISIBLE);
                    ahcdcc_iv_submit.setVisibility(View.VISIBLE);
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
                    Toast.makeText(HomeCourseDetailChapterAddContentActivity.this, uploadString.substring(8, uploadString.length()), Toast.LENGTH_SHORT).show();
                    returnedVideoUri = uploadString.substring(8, uploadString.length());
                    String res_list = uploadString.substring(8, uploadString.length());
                    uploadRequest(res_list);
                });

            }
        });
    }

    private void uploadRequest(String res_list) {
        AddChapter addChapter = new AddChapter();
        addChapter.setCourse_id(getIntent().getIntExtra("course_id", -1));
        addChapter.setRes_list(res_list);
        addChapter.setUnit_id(getIntent().getIntExtra("unit_id", -1));
        addChapter.setTitle(finalVideoTitle);
        String json = new Gson().toJson(addChapter);
        HttpUtil.sendPostRequest(com.zhaoweihao.architechturesample.util.Constant.ADD_CHAPTER_URL, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == com.zhaoweihao.architechturesample.util.Constant.SUCCESS_CODE) {
                    runOnUiThread(() -> {
                        Toast.makeText(HomeCourseDetailChapterAddContentActivity.this, "成功上传!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }



    private void playVideos2(String videoUrl) {
        Uri uri = Uri.parse(videoUrl);

        //设置视频控制器
        ahcdcc_vv.setMediaController(new MediaController(this));

        //播放完成回调
        ahcdcc_vv.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //设置视频路径
        ahcdcc_vv.setVideoURI(uri);

        //开始播放视频
        ahcdcc_vv.start();
        //Glide.with(HomeCourseDetailChapterAddContentActivity.this).load(uri).into();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(HomeCourseDetailChapterAddContentActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }

}
