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
import com.zhaoweihao.architechturesample.util.Logger;

import java.util.Locale;


/**
 * 自定义档位拖拽条
 * 总共有7个档位，从1档开始
 *
 * @author zhaoweihao
 * @date 2019/1/6
 */
public class NewCustomSeekbar extends View {

    public static final String TAG = NewCustomSeekbar.class.getSimpleName();

    Paint paint = new Paint();

    /**
     * 计算的宽度
     */
    private int width;

    /**
     * 计算的高度
     */
    private int height;

    /**
     * 档位数
     * 从0开始，0代表1档
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

    public NewCustomSeekbar(Context context) {
        super(context);
        init();
    }

    public NewCustomSeekbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewCustomSeekbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logger.d("onMeasure ===");
        Logger.d("widthMeasureSpec === " + widthMeasureSpec);
        Logger.d("heightMeasureSpec === " + heightMeasureSpec);
//        width = widthMeasureSpec;
//        height = heightMeasureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Logger.d("onLayout ===");

        width = getWidth();
        height = getHeight();

    }

    @Override
    public void onDraw(Canvas canvas) {
        Logger.d("onDraw ===");

        paint.setColor(Color.parseColor("#91C1FC"));
        //设置线宽
        paint.setStrokeWidth(ConvertUtils.dp2px(5));
        canvas.translate(ConvertUtils.dp2px(18), ConvertUtils.dp2px(18));
        //画一条线
        canvas.drawLine(0, 0, width - width / 7, 0, paint);

        Logger.d("width = " + width + "height = " + height);
        Logger.d("width = " + width + "width / 7 = " + width / 7);

        //画七个点，代表七个档位
        for (int i = 0; i < 7; i++) {
            //圆点半径5dp
            float radius = 7;
            //首尾两个点需要大点
            if (i == 0 || i == 6) {
                radius = 12;
            }
//            canvas.drawCircle(((ConvertUtils.dp2px(50) * i)), 0, ConvertUtils.dp2px(radius), paint);

            Logger.d("width /7 * i = " + width / 7 * i);
            int x = width / 7 * i;
            canvas.drawCircle(x, 0, ConvertUtils.dp2px(radius), paint);

        }

        paint.setColor(Color.parseColor("#077ECD"));
        //画一个圆，代表当前档位
//        canvas.drawCircle(ConvertUtils.dp2px(50) * step, 0, ConvertUtils.dp2px(18), paint);

        canvas.drawCircle(width / 7 * step, 0, ConvertUtils.dp2px(18), paint);


        paint.setColor(Color.parseColor("#ffffff"));
        //绘制文字
        String text = String.format(Locale.getDefault(), "%d", step + 1);
        paint.setTextSize(ConvertUtils.sp2px(13));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, width / 7 * step, ConvertUtils.dp2px(3), paint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        Logger.d(TAG, "x === " + x + "y === " + y);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
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
            default: {

            }
            break;
        }


        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重绘界面
     *
     * @param x
     */
    private void revalidateView(int x) {
        int firstStep = ConvertUtils.dp2px(18) + width / 7 / 2;
        if (x < firstStep) {
            Logger.d("第一段");
            step = 0;
        }

        if (x >= firstStep && x < firstStep + width / 7 ) {
            Logger.d("第二段");
            step = 1;
        }

        if (x >= firstStep + width / 7 && x < firstStep + width / 7 * 2) {
            Logger.d("第三段");
            step = 2;
        }

        if (x >= firstStep + width / 7 * 2 && x < firstStep + width / 7 * 3) {
            Logger.d("第四段");
            step = 3;
        }

        if (x >= firstStep + width / 7 * 3 && x < firstStep + width / 7 * 4) {
            Logger.d("第五段");
            step = 4;
        }

        if (x >= firstStep + width / 7 * 4 && x < firstStep + width / 7 * 5) {
            Logger.d("第六段");
            step = 5;
        }

        if (x > firstStep + width / 7 * 5) {
            Logger.d("第七段");
            step = 6;
        }

        invalidate();
//        if (x < ConvertUtils.dp2px(45)) {
//            Log.d(TAG, "第一段");
//            step = 0;
//        }
//        //第二段
//        if (x >= ConvertUtils.dp2px(45) && x < ConvertUtils.dp2px(95)) {
//            Log.d(TAG, "第二段");
//            step = 1;
//        }
//        if (x >= ConvertUtils.dp2px(95) && x < ConvertUtils.dp2px(145)) {
//            Log.d(TAG, "第3段");
//            step = 2;
//        }
//        if (x >= ConvertUtils.dp2px(145) && x < ConvertUtils.dp2px(195)) {
//            Log.d(TAG, "第4段");
//            step = 3;
//        }
//        if (x >= ConvertUtils.dp2px(195) && x < ConvertUtils.dp2px(245)) {
//            Log.d(TAG, "第5段");
//            step = 4;
//        }
//        if (x >= ConvertUtils.dp2px(245) && x < ConvertUtils.dp2px(295)) {
//            Log.d(TAG, "第6段");
//            step = 5;
//        }
//
//        if (x >= ConvertUtils.dp2px(295)) {
//            Log.d(TAG, "第7段");
//            step = 6;
//        }
//        invalidate();
    }

    /**
     * 设置显示的档位
     *
     * @param step
     */
    public void setStep(int step) {
        this.step = step;
        invalidate();
    }


    public void setListener(SeekbarCallback seekbarCallback) {
        this.seekbarCallback = seekbarCallback;
    }

    public interface SeekbarCallback {
        void callback(int step);
    }


}
