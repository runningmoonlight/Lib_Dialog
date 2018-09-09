package com.running.moonlight.lib_dialog.picker.wheel.view;

import java.util.TimerTask;

/**
 * 惯性滑动
 */
public final class InertiaTimerTask extends TimerTask {

    private float mVelocityY;//速度的记录值
    private final float mOriginalVelocityY;//初始速度
    private final WheelView mWheelView;

    InertiaTimerTask(WheelView wheelView, float originalVelocityY) {
        super();
        this.mWheelView = wheelView;
        this.mOriginalVelocityY = originalVelocityY;
        this.mVelocityY = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (mVelocityY == Integer.MAX_VALUE) {
            if (Math.abs(mOriginalVelocityY) > 2000F) {
                if (mOriginalVelocityY > 0.0F) {
                    mVelocityY = 2000F;
                } else {
                    mVelocityY = -2000F;
                }
            } else {
                mVelocityY = mOriginalVelocityY;
            }
        }
        if (Math.abs(mVelocityY) >= 0.0F && Math.abs(mVelocityY) <= 20F) {
            mWheelView.cancelFuture();
            mWheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int dy = (int) ((mVelocityY) / 100F);
        mWheelView.mTotalScrollY = mWheelView.mTotalScrollY - dy;
        if (!mWheelView.mLoop) {
            float itemHeight = mWheelView.mItemHeight;
            float top = (-mWheelView.mInitPosition) * itemHeight;
            float bottom = (mWheelView.getItemsCount() - 1 - mWheelView.mInitPosition) * itemHeight;
            if(mWheelView.mTotalScrollY - itemHeight * 0.25 < top){
                top = mWheelView.mTotalScrollY + dy;
            } else if(mWheelView.mTotalScrollY + itemHeight * 0.25 > bottom){
                bottom = mWheelView.mTotalScrollY + dy;
            }

            if (mWheelView.mTotalScrollY <= top){
                mVelocityY = 40F;
                mWheelView.mTotalScrollY = (int)top;
            } else if (mWheelView.mTotalScrollY >= bottom) {
                mWheelView.mTotalScrollY = (int)bottom;
                mVelocityY = -40F;
            }
        }
        if (mVelocityY < 0.0F) {
            mVelocityY = mVelocityY + 20F;
        } else {
            mVelocityY = mVelocityY - 20F;
        }
        mWheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }

}
