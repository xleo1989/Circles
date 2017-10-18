package com.x.leo.circles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @作者:My
 * @创建日期: 2017/4/11 17:37
 * @描述:${TODO}
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 */

public class CircleProgressBarView extends View {
    private int   mProgress;
    private int   mMeasuredHeight;
    private int   mMeasuredWidth;

    private int   mStrokeWidth;
    private int   mRadius;
    private int   mStrokeBackgroundColor;
    private int   mProgressColor;
    private Paint mPaint;

    public CircleProgressBarView(Context context) {
        this(context,null);
    }

    public CircleProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public CircleProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public CircleProgressBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView);
        mProgressColor = typedArray.getColor(R.styleable.CircleProgressBarView_progresscolor,Color.parseColor("#53c792"));
        mStrokeBackgroundColor = typedArray.getColor(R.styleable.CircleProgressBarView_backgroundcolor, Color.parseColor("#C5DbD0"));
        mRadius = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBarView_radius, 0);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBarView_strokewidth, DensityUtils.dp2px(getContext(), 5));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        mMeasuredHeight = getMeasuredHeight();
        mMeasuredWidth = getMeasuredWidth();
    }

    private RectF oval = new RectF();
    @Override
    protected void onDraw(Canvas canvas) {
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            if (mStrokeWidth == 0) {
                return;
            }
            mPaint.setStrokeWidth(mStrokeWidth);
        }
        if (mRadius == 0) {
            mRadius = mMeasuredWidth / 2 - mStrokeWidth / 2 - mStrokeWidth/2;
        }
        mPaint.setColor(mStrokeBackgroundColor);
        canvas.drawCircle(mMeasuredWidth / 2, mMeasuredHeight / 2, mRadius, mPaint);
        oval.set(mMeasuredWidth / 2 - mRadius, mMeasuredWidth / 2 - mRadius, mMeasuredWidth / 2 + mRadius, mMeasuredWidth / 2 + mRadius);
        mPaint.setColor(mProgressColor);
        canvas.drawArc(oval, 0, mProgress * 360 / 100, false, mPaint);
    }


    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public int getStrokeBackgroundColor() {
        return mStrokeBackgroundColor;
    }


    public void setStrokeBackgroundColor(int backgroundColor) {
        mStrokeBackgroundColor = backgroundColor;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    public void setProgress(int progress){
        mProgress = progress;
        invalidate();
    }

    public int getProgress(){
        return mProgress;
    }
}
