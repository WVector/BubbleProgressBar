package com.vector.library;

/**
 * Created by Vector
 * on 2017/8/2 0002.
 */

public class AccelerationComputer {

    private int mCurrentProgress;
    private long lastTime;
    private long mVelocity;

    public float getAcceleration(int progress) {
        long currentTime = System.currentTimeMillis();
        float acceleration = 0.0f;
        if (lastTime == 0) {
            lastTime = currentTime;
        } else {
            long dTime = currentTime - lastTime;
            long tempVelocity = Math.round((progress - this.mCurrentProgress) * 1000 * 100F * 100.0f / dTime);
            tempVelocity = (long) Math.sqrt(tempVelocity);
            acceleration = (tempVelocity - mVelocity) * 1F / dTime;
            mVelocity = tempVelocity;
            lastTime = currentTime;
        }
        this.mCurrentProgress = progress;
        return acceleration;
    }

    public float getAcceler(int progress) {
        //基本思路，根据进度，实时计算加速度,并且要计算中间过程值，实时改变
        float acceleration = getAcceleration(progress);
        //边界
        float boundary = 1.0F;
        if (acceleration < -boundary) {
            acceleration = -boundary;
        }
        if (acceleration > boundary) {
            acceleration = boundary;
        }
        return acceleration;
    }

}
