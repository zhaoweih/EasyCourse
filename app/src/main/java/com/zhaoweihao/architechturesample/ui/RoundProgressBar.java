package com.zhaoweihao.architechturesample.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;


import com.zhaoweihao.architechturesample.R;

/**
 * @author
 * @description 圆形进度条
 * @time 2019/4/6 20:12
 */
public class RoundProgressBar extends View {
    /**
     * 圆环的颜色
     */
    private int mRoundColor;
    /**
     * 进度的颜色
     */
    private int mRoundProgressColor;
    /**
     * 圆环的宽度
     */
    private float mRoundWidth;
    /**
     * 文字颜色
     */
    private int mTextColor;
    /**
     * 文字大小
     */
    private float mTextSize;
    /**
     * 最大值
     */
    private int mMax;
    /**
     * 进度文字是否显示
     */
    private boolean mTextIsDisplayable;
    /**
     * 实心还是空心
     */
    private int mStyle;

    private static final int STROKE = 0;
    private static final int FILL = 1;


    private int mProgress = 10;
    Paint mPaint;

    public RoundProgressBar(Context context) {
        this(context, null);
    }


    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();

        /**
         * 获取自定义的属性
         */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        //底色
        mRoundColor = typedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        //进度的颜色
        mRoundProgressColor = typedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.BLUE);
        //圆形的宽
        mRoundWidth = typedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 20);

        //字体颜色 中间
        mTextColor = typedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.BLUE);

        //中间进度显示的字体大小
        mTextSize = typedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);

        //最大值
        mMax = typedArray.getInteger(R.styleable.RoundProgressBar_max, 100);

        //文字是否显示
        mTextIsDisplayable = typedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);

        //实心或者 空心
        mStyle = typedArray.getInt(R.styleable.RoundProgressBar_style, 0);


        typedArray.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //圆心
        int centerOfCircle = getWidth() / 2;
        //radius 半径
        int radius = (int) (centerOfCircle - mRoundWidth / 2);


        //设置画笔
        mPaint.setAntiAlias(true);
        //圆环的颜色
        mPaint.setColor(mRoundColor);

        //设置空心
        mPaint.setStyle(Paint.Style.STROKE);

        //画笔宽度
        mPaint.setStrokeWidth(mRoundWidth);

        //画圆
        canvas.drawCircle(centerOfCircle, centerOfCircle, radius, mPaint);


        /**
         * 画百分比
         */
        mPaint.setStrokeWidth(0);
        //字体大小
        mPaint.setTextSize(mTextSize);
        //画笔颜色
        mPaint.setColor(mTextColor);
        //字体
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

        //计算百分比
        int percent = (int) (((float) mProgress / (float) mMax) * 100);

        //测量字体的宽度
        float textWidth = mPaint.measureText(percent + "%");
        //判断是否显示进度文字 不是0，风格是空心的
        if (mTextIsDisplayable && percent != 0 && mStyle == STROKE) {

            canvas.drawText(percent + "%", centerOfCircle - textWidth / 2, centerOfCircle + textWidth / 2, mPaint);
        }


        /**
         * 设置进度
         */
        mPaint.setColor(mRoundProgressColor);
        //画笔宽度
        mPaint.setStrokeWidth(mRoundWidth);
        mPaint.setAntiAlias(true);


        RectF oval = new RectF(centerOfCircle - radius, centerOfCircle - radius, centerOfCircle + radius, centerOfCircle + radius);


        switch (mStyle) {

            case STROKE:
                //空心
                mPaint.setStyle(Paint.Style.STROKE);
                //画圆弧

                /**
                 *
                 *开始的角度
                 */
                canvas.drawArc(oval, 180, 360 * mProgress / mMax, false, mPaint);
                break;
            case FILL:
                //实心
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                //画圆弧
                if (mProgress != 0) {
                    canvas.drawArc(oval, 180, 360 * mProgress / mMax, true, mPaint);
                }
                break;
        }


    }

    public int getRoundColor() {
        return mRoundColor;
    }

    public void setRoundColor(int roundColor) {
        mRoundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return mRoundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        mRoundProgressColor = roundProgressColor;
    }

    public float getRoundWidth() {
        return mRoundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        mRoundWidth = roundWidth;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public synchronized int getMax() {
        return mMax;
    }

    public synchronized void setMax(int max) {
        mMax = max;
    }

    public boolean isTextIsDisplayable() {
        return mTextIsDisplayable;
    }

    public void setTextIsDisplayable(boolean textIsDisplayable) {
        mTextIsDisplayable = textIsDisplayable;
    }

    public int getStyle() {
        return mStyle;
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public synchronized int getProgress() {
        return mProgress;
    }

    public synchronized void setProgress(int progress) {
        if (progress > mMax) {
            progress = mMax; }
        if (progress <= mMax) {
            mProgress = progress;
            postInvalidate();
        }
    }
}
