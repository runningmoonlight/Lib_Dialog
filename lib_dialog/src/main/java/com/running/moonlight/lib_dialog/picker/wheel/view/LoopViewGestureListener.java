package com.running.moonlight.lib_dialog.picker.wheel.view;

import android.view.MotionEvent;

final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final WheelView mWheelView;

    LoopViewGestureListener(WheelView wheelView) {
        mWheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mWheelView.scrollBy(velocityY);
        return true;
    }
}
