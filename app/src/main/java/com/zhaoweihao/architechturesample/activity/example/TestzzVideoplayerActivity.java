package com.zhaoweihao.architechturesample.activity.example;


import android.content.res.Configuration;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.app.Activity;

import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.VideoView;


import com.zhaoweihao.architechturesample.R;

import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestzzVideoplayerActivity extends Activity {
    private Display currDisplay;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private int vWidth, vHeight;
    //private boolean readyToPlay = false;
    private String videoUrl = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";
    private String mVideoUrl;
    private VideoPlayer mVp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jcplayer);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        videoUrl = getIntent().getStringExtra("videourl");
        mVideoUrl = videoUrl;
        mVp = (VideoPlayer) findViewById(R.id.vp);
        mVp.setTitle(getIntent().getStringExtra("chapter_title")); //设置视频名称
        mVp.setToggleExpandable(true);
        //true-启用横竖屏切换按钮  false-隐藏横竖屏切换韩流

        mVp.setVideoUri(this, mVideoUrl);//设置视频路径
        // mVp.startPlay();//开始播放

        //也可以直接设置路径并播放
        mVp.loadAndStartVideo(this, mVideoUrl); // 设置视频播放路径并开始播放
        // 直接使用横屏显示
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mVp != null) {
            mVp.updateActivityOrientation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVp.onHostResume();
        Log.v("testplayer", "onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVp.onHostPause();
        Log.v("testplayer", "onpause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVp.onHostDestroy();
        Log.v("testplayer", "onDestroy");
    }

}

