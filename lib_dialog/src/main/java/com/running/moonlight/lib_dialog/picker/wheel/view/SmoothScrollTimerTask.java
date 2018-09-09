package com.running.moonlight.lib_dialog.picker.wheel.view;

import java.util.TimerTask;

/**
 * 平滑滚动
 */
final class SmoothScrollTimerTask extends TimerTask {

    private int mTotalOffset;
    private int mRealOffset;
    private int mOffset;
    private final WheelView mWheelView;

    SmoothScrollTimerTask(WheelView loopview, int offset) {
        this.mWheelView = loopview;
        this.mOffset = offset;
        mTotalOffset = Integer.MAX_VALUE;
        mRealOffset = 0;
    }

    @Override
    public final void run() {
        if (mTotalOffset == Integer.MAX_VALUE) {
            mTotalOffset = mOffset;
        }
        //把要滚动的范围细分成十小份，按是小份单位来重绘
        mRealOffset = (int) ((float) mTotalOffset * 0.1F);

        if (mRealOffset == 0) {
            if (mTotalOffset < 0) {
                mRealOffset = -1;
            } else {
                mRealOffset = 1;
            }
        }

        if (Math.abs(mTotalOffset) <= 1) {
            mWheelView.cancelFuture();
            mWheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
        } else {
            mWheelView.mTotalScrollY = mWheelView.mTotalScrollY + mRealOffset;

            //这里如果不是循环模式，则点击空白位置需要回滚，不然就会出现选到－1 item的 情况
            if (!mWheelView.mLoop) {
                float itemHeight = mWheelView.mItemHeight;
                float top = (float) (-mWheelView.mInitPosition) * itemHeight;
                float bottom = (float) (mWheelView.getItemsCount() - 1 - mWheelView.mInitPosition) * itemHeight;
                if (mWheelView.mTotalScrollY <= top|| mWheelView.mTotalScrollY >= bottom) {
                    mWheelView.mTotalScrollY = mWheelView.mTotalScrollY - mRealOffset;
                    mWheelView.cancelFuture();
                    mWheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
                    return;
                }
            }
            mWheelView.mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
            mTotalOffset = mTotalOffset - mRealOffset;
        }
    }
}
