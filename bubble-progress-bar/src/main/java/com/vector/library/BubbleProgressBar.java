package com.vector.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Vector
 * on 2017/7/31 0031.
 */

public class BubbleProgressBar extends View {

    private Paint mBubblePaint;
    private Paint mBubbleTextPaint;
    private Path mBubblePath;
    private Path mBubbleTextPath;
    private Paint mUnreachedBarPaint;
    private Paint mReachedBarPaint;
    private PaintFlagsDrawFilter mDrawFilter;
    private int mBubbleColor = 0xffff3366;
    private int mBubbleTextColor = Color.WHITE;
    private int mReachedBarColor = 0xffff3366;
    private int mUnreachedBarColor = 0XFFCCCCCC;
    private int mProgress = 0;
    private int mBarWidth = 15;
    private int mBubbleRadius = 45;
    private float mDegrees = 0.0F;
    private Rect mRect;
    private AccelerationComputer mAccelerationComputer;
    private ValueAnimator mValueAnimator;


    public BubbleProgressBar(Context context) {
        this(context, null);
    }

    public BubbleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取初始化值
        //对界面的临界值，进行计算

        initValues(context, attrs, defStyleAttr);

        mAccelerationComputer = new AccelerationComputer();

    }

    private void initValues(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BubbleProgressBar, defStyleAttr, 0);
        //气泡颜色
        mBubbleColor = attributes.getColor(R.styleable.BubbleProgressBar_bubble_color, mBubbleColor);
        //气泡文字颜色
        mBubbleTextColor = attributes.getColor(R.styleable.BubbleProgressBar_bubble_text_color, mBubbleTextColor);
        //进度条已完成颜色
        mReachedBarColor = attributes.getColor(R.styleable.BubbleProgressBar_reached_bar_color, mReachedBarColor);
        //进度条未完成颜色
        mUnreachedBarColor = attributes.getColor(R.styleable.BubbleProgressBar_unreached_bar_color, mUnreachedBarColor);
        //进度条宽度
        mBarWidth = (int) attributes.getDimension(R.styleable.BubbleProgressBar_progress_bar_with, mBarWidth);
        //气泡半径
        mBubbleRadius = (int) attributes.getDimension(R.styleable.BubbleProgressBar_bubble_radius, mBubbleRadius);

        attributes.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int radius = (getMeasuredHeight() - getPaddingBottom() - getPaddingTop()) / 6;

        Log.d("BubbleProgressBar", "radius:" + radius);

        if (mBubbleRadius > radius) {
            mBubbleRadius = radius;
        }


        initPainters();
    }

    /**
     * 初始化画笔
     */
    private void initPainters() {
        //画布抗锯齿
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setAntiAlias(true);
        mReachedBarPaint.setStyle(Paint.Style.FILL);
        mReachedBarPaint.setColor(getReachedBarColor());
        mReachedBarPaint.setStrokeCap(Paint.Cap.ROUND);
        mReachedBarPaint.setStrokeJoin(Paint.Join.ROUND);
        mReachedBarPaint.setStrokeWidth(getBarWidth());
        mReachedBarPaint.setDither(true);

        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setAntiAlias(true);
        mUnreachedBarPaint.setStyle(Paint.Style.FILL);
        mUnreachedBarPaint.setColor(getUnreachedBarColor());
        mUnreachedBarPaint.setStrokeCap(Paint.Cap.ROUND);
        mUnreachedBarPaint.setStrokeJoin(Paint.Join.ROUND);
        mUnreachedBarPaint.setStrokeWidth(getBarWidth());
        mUnreachedBarPaint.setDither(true);


        mBubblePaint = new Paint();
        mBubblePaint.setAntiAlias(true);
        mBubblePaint.setColor(getBubbleColor());
        mBubblePaint.setDither(true);


        mBubbleTextPaint = new Paint();
        mBubbleTextPaint.setAntiAlias(true);
        mBubbleTextPaint.setColor(getBubbleTextColor());
        mBubbleTextPaint.setTextAlign(Paint.Align.CENTER);
        mBubbleTextPaint.setDither(true);
        mBubbleTextPaint.setTextSize(getBubbleTextSize());


        mBubblePath = new Path();
        mBubbleTextPath = new Path();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.d("BubbleProgressBar", "绘制" + getProgress());

        if (getProgress() == 100) {
            //矫正
            mDegrees = 0;
        }

        canvas.setDrawFilter(getDrawFilter());

        float currentX = getCurrentX();
        float currentY = getCurrentY();

        drawProgressBar(canvas, currentX, currentY);


        drawBubble(canvas, currentX, currentY);


    }

    /**
     * 绘画气泡
     *
     * @param canvas   画布
     * @param currentX 中心点x
     * @param currentY 中心点y
     */
    private void drawBubble(Canvas canvas, float currentX, float currentY) {


        mBubblePath.reset();

        mBubblePath.moveTo(currentX, currentY);

        RectF rectF = new RectF();

        rectF.set(currentX - getBubbleRadius(), currentY - 3 * getBubbleRadius(), currentX + getBubbleRadius(), currentY - getBubbleRadius());

        mBubblePath.arcTo(rectF, 150, 240);

        mBubblePath.close();

        Matrix matrix = new Matrix();
        matrix.setRotate(getDegrees(), currentX, currentY);

        mBubblePath.transform(matrix);

        canvas.drawPath(mBubblePath, mBubblePaint);


        drawBubbleText(canvas, currentX, currentY, matrix);

    }

    /**
     * 绘画气泡中的文字
     *
     * @param canvas   画布
     * @param currentX 中心点x
     * @param currentY 中心点y
     * @param matrix
     */
    private void drawBubbleText(Canvas canvas, float currentX, float currentY, Matrix matrix) {
        mBubbleTextPaint.getTextBounds(getProgressText(), 0, getProgressText().length(), mRect);
        Paint.FontMetrics fm = mBubbleTextPaint.getFontMetrics();
        float baseline = currentY - 2f * getBubbleRadius() + (fm.descent - fm.ascent) / 2f - fm.descent;
        float textWidth = mBubbleTextPaint.measureText(getProgressText());


        mBubbleTextPath.reset();

        mBubbleTextPath.moveTo(currentX - textWidth / 2.0f, baseline);
        mBubbleTextPath.lineTo(currentX + textWidth / 2.0f, baseline);

        mBubbleTextPath.transform(matrix);


        canvas.drawTextOnPath(getProgressText(), mBubbleTextPath, 0.0f, 0.0f, mBubbleTextPaint);


    }

    /**
     * 绘画进度进度条
     *
     * @param canvas   画布
     * @param currentX 中心点x
     * @param currentY 中心点y
     */
    private void drawProgressBar(Canvas canvas, float currentX, float currentY) {
        float centerY = getHeight() / 2.0f;

        canvas.drawLine(currentX, currentY, getStopX(), centerY, getUnreachedBarPaint());

        canvas.drawLine(getStartX(), centerY, currentX, currentY, getReachedBarPaint());
    }

    /**
     * @return 进度条的结束坐标X
     */
    private float getStopX() {
        return getWidth() - getPaddingRight() - getBarWidth() / 2.0f - getBubbleRadius();
    }

    /**
     * @return 进度条的开始坐标X
     */
    private float getStartX() {
        return getPaddingLeft() + getBarWidth() / 2.0f + getBubbleRadius();
    }

    /**
     * @return 返回当前y坐标
     */
    private float getCurrentY() {
        int progress = getProgress();

        if (progress > 50) {
            progress = 100 - progress;
        }

        return getHeight() / 2.0f + (getHeight() / 2.0f - getPaddingBottom()) * 0.8f * progress * 0.01f;
    }

    /**
     * @return 计算当前x坐标
     */
    private float getCurrentX() {
        return (getStopX() - getStartX()) * getProgress() * 0.01F + getStartX();
    }

    public int getProgress() {


        return mProgress;
    }

    /**
     * 设置进度
     *
     * @param progress 当前进度
     */
    public void setProgress(int progress) {
        progress = progress > 100 ? 100 : progress;
        this.mProgress = progress;

        int range = 10;

        float v = -mAccelerationComputer.getAcceler(getProgress());


        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        mValueAnimator = ValueAnimator
                .ofFloat(mDegrees, v * range)
                .setDuration(Math.abs((long) (1000 * ((mDegrees - v * range) / 90))));
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDegrees = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.start();

        invalidate();


    }

    public int getReachedBarColor() {
        return mReachedBarColor;
    }

    public void setReachedBarColor(int reachedBarColor) {
        mReachedBarColor = reachedBarColor;
    }

    public int getUnreachedBarColor() {
        return mUnreachedBarColor;
    }

    public void setUnreachedBarColor(int unreachedBarColor) {
        mUnreachedBarColor = unreachedBarColor;
    }

    public int getBarWidth() {
        return mBarWidth;
    }

    public void setBarWidth(int barWidth) {
        mBarWidth = barWidth;
    }

    public Paint getUnreachedBarPaint() {
        return mUnreachedBarPaint;
    }

    public Paint getReachedBarPaint() {
        return mReachedBarPaint;
    }

    public PaintFlagsDrawFilter getDrawFilter() {
        return mDrawFilter;
    }

    public int getBubbleColor() {
        return mBubbleColor;
    }

    public void setBubbleColor(int bubbleColor) {
        mBubbleColor = bubbleColor;
    }

    public int getBubbleTextColor() {
        return mBubbleTextColor;
    }

    public void setBubbleTextColor(int bubbleTextColor) {
        mBubbleTextColor = bubbleTextColor;
    }

    public float getBubbleTextSize() {
        return mBubbleRadius * 0.7f;
    }

    public int getBubbleRadius() {
        return mBubbleRadius;
    }

    public void setBubbleRadius(int bubbleRadius) {
        mBubbleRadius = bubbleRadius;
    }

    public String getProgressText() {
        return getProgress() + "%";
    }

    public float getDegrees() {
        return mDegrees;
    }

    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(getProgress() + by);
        }
    }
}
