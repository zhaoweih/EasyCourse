package com.zhaoweihao.architechturesample.view.uiview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.zhaoweihao.architechturesample.interfaze.SeekbarCallback;

import java.util.Locale;


/**
 * 自定义拖拽条
 * @author zhaoweihao
 */
public class CustomSeekbar extends View {

    public static final String TAG = CustomSeekbar.class.getSimpleName();

    Paint paint = new Paint();

    /**
     * 段数
     */
    private int step;

    private SeekbarCallback seekbarCallback;

    /**
     * 初始化画笔
     */
    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
    }
    public CustomSeekbar(Context context) {
        super(context);
        init();
    }

    public CustomSeekbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSeekbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.parseColor("#00FFFF"));
        //设置线宽
        paint.setStrokeWidth(ConvertUtils.dp2px(5));
        canvas.translate(ConvertUtils.dp2px(20), ConvertUtils.dp2px(20));
        //画一条线
        canvas.drawLine(0, 0, ConvertUtils.dp2px(300), 0, paint);

        //画七个点，代表七个档位
        for (int i = 0; i < 7; i ++) {
            //圆点半径5dp
            float radius = 7;
            //首尾两个点需要大点
            if (i == 0 || i == 6) {
                radius = 12;
            }
            canvas.drawCircle(((ConvertUtils.dp2px(50) * i)), 0, ConvertUtils.dp2px(radius), paint);
        }

        paint.setColor(Color.WHITE);
        //画一个圆，代表当前档位
        canvas.drawCircle(ConvertUtils.dp2px(50) * step, 0, ConvertUtils.dp2px(18), paint);

        paint.setColor(Color.parseColor("#00FFFF"));
        //绘制文字
        String text = String.format(Locale.getDefault(), "%d档", step + 1);
        paint.setTextSize(ConvertUtils.sp2px(13));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, ConvertUtils.dp2px(50) * step, ConvertUtils.dp2px(3), paint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        Log.d(TAG, "x === " + x + "y === " + y);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                Log.d(TAG, "ACTION_DOWN ===");
                revalidateView(x);
                return true;
            }
            case MotionEvent.ACTION_UP: {
                Log.d(TAG, "ACTION_UP ===");
                seekbarCallback.callback(step);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.d(TAG, "ACTION_MOVE ===");
                revalidateView(x);
                return true;
            }
            default:{

            }
            break;
        }


        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重绘界面
     * @param x
     */
    private void revalidateView(int x) {
        if ( x < ConvertUtils.dp2px(45)) {
            Log.d(TAG, "第一段");
            step = 0;
        }
        //第二段
        if (x >= ConvertUtils.dp2px(45) && x < ConvertUtils.dp2px(95)) {
            Log.d(TAG, "第二段");
            step = 1;
        }
        if (x >= ConvertUtils.dp2px(95) && x < ConvertUtils.dp2px(145)) {
            Log.d(TAG, "第3段");
            step = 2;
        }
        if (x >= ConvertUtils.dp2px(145) && x < ConvertUtils.dp2px(195)) {
            Log.d(TAG, "第4段");
            step = 3;
        }
        if (x >= ConvertUtils.dp2px(195) && x < ConvertUtils.dp2px(245)) {
            Log.d(TAG, "第5段");
            step = 4;
        }
        if (x >= ConvertUtils.dp2px(245) && x < ConvertUtils.dp2px(295)) {
            Log.d(TAG, "第6段");
            step = 5;
        }

        if (x >= ConvertUtils.dp2px(295)) {
            Log.d(TAG, "第7段");
            step = 6;
        }
        invalidate();
    }

    public void setStep(int step) {
        this.step = step;
        invalidate();
    }


    public void setListener(SeekbarCallback seekbarCallback) {
        this.seekbarCallback = seekbarCallback;
    }


}
