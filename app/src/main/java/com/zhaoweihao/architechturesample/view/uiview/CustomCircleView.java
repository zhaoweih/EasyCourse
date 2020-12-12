package com.zhaoweihao.architechturesample.view.uiview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.Locale;

/**
 * 自定义圆形进度条
 *
 * @author zhaoweihao
 * @date 2019/1/9
 */
public class CustomCircleView extends View {

    /**
     * 进度
     */
    float progress = 0;

    public static final String TAG = CustomCircleView.class.getSimpleName();

    Paint paint = new Paint();

    public CustomCircleView(Context context) {
        super(context);
        init();
    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 创建getter方法
     *
     * @return
     */
    public float getProgress() {
        return progress;
    }

    /**
     * 创建setter方法
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * 初始化画笔
     */
    private void init() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);
        //设置抗锯齿
        paint.setAntiAlias(true);
        //先画一个透明正方形底图
        canvas.drawRect(0, 0, ConvertUtils.dp2px(150), ConvertUtils.dp2px(150), paint);

        paint.setColor(Color.parseColor("#00FFFF"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ConvertUtils.dp2px(2));

        //画一个底层描边的圆
        canvas.drawCircle(ConvertUtils.dp2px(75), ConvertUtils.dp2px(75), ConvertUtils.dp2px(73), paint);

        //画一个顶层描边的弧
        paint.setColor(Color.parseColor("#FFFF00"));

        canvas.drawArc(ConvertUtils.dp2px(2), ConvertUtils.dp2px(2), ConvertUtils.dp2px(148), ConvertUtils.dp2px(148), -90, progress * 3.6f, false, paint);

        //绘制文字
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(ConvertUtils.sp2px(24));

        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //基线到字体上边框的距离
        float top = fontMetrics.top;
        //基线到字体下边框的距离
        float bottom = fontMetrics.bottom;

        //基线中间点的y轴计算公式
        int baseLineY = (int) (ConvertUtils.dp2px(75) - top/2 - bottom/2);

        canvas.drawText(String.format(Locale.getDefault(), "%d%%", (int)progress), ConvertUtils.dp2px(75), baseLineY, paint);


    }

    /**
     * 执行动画
     */
    public void revalidateView(float progress) {
        // 创建 ObjectAnimator 对象
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 0, progress);
        //设置秒数
        animator.setDuration(800);
        // 执行动画
        animator.start();
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        //设置宽高
        setMeasuredDimension(ConvertUtils.dp2px(150), ConvertUtils.dp2px(150));
    }
}
