package com.vector.diyprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Vector
 * on 2017/8/1 0001.
 */

public class TestView extends View {
    private Paint mBubblePaint;
    private Path mBubblePath;
    private float mDegrees = 0.0F;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBubblePaint = new Paint();
        mBubblePaint.setAntiAlias(true);
        mBubblePaint.setColor(Color.parseColor("#ffff3366"));
        mBubblePaint.setDither(true);

//        mBubblePaint.setStyle(Paint.Style.STROKE);
//        mBubblePaint.setStrokeWidth(5);


        mBubblePaint.setStrokeCap(Paint.Cap.ROUND);
        mBubblePaint.setStrokeJoin(Paint.Join.ROUND);


        mBubblePath = new Path();


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-90.f, 90.0f).setDuration(3 * 1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mDegrees = (float) (animation.getAnimatedValue());

                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(6 * mBubbleRadius, 3 * mBubbleRadius);
    }

    private int mBubbleRadius = 100;

    @Override
    protected void onDraw(Canvas canvas) {


        mBubblePath.reset();


        int srcX = 3 * mBubbleRadius;
        int srcY = 3 * mBubbleRadius;

        mBubblePath.moveTo(srcX, srcY);

        RectF rectF = new RectF();

        rectF.set(srcX - mBubbleRadius, srcY - 3 * mBubbleRadius, srcX + mBubbleRadius, srcY - mBubbleRadius);

        mBubblePath.arcTo(rectF, 150, 240);


        mBubblePath.close();


        Matrix matrix = new Matrix();
        matrix.setRotate(mDegrees, srcX, srcY);
        mBubblePath.transform(matrix);


        canvas.drawPath(mBubblePath, mBubblePaint);

    }
}
