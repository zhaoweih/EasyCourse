package com.zhaoweihao.architechturesample.activity.example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.interfaze.SeekbarCallback;
import com.zhaoweihao.architechturesample.view.uiview.CustomCircleView;
import com.zhaoweihao.architechturesample.view.uiview.NewCustomSeekbar;

/**
 * @author Zhaoweihao
 * 测试腾讯UI库
 * Github:https://github.com/Tencent/QMUI_Android
 * 开始使用：https://qmuiteam.com/android
 */
public class TestTecentUIActivity extends BaseActivity implements NewCustomSeekbar.SeekbarCallback {

    private NewCustomSeekbar customSeekbar;

    private CustomCircleView customCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tecent_ui);

        customSeekbar = findViewById(R.id.customSeekbar);

        customCircleView = findViewById(R.id.custom_circle_view);

        customSeekbar.setListener(this);

        customSeekbar.setStep(3);

        customCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customCircleView.revalidateView(65f);

            }
        });



    }

    @Override
    public void callback(int step) {
        Log.d(TAG, step + "");
    }


}


