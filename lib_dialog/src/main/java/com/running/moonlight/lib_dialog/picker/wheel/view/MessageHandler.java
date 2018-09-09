package com.running.moonlight.lib_dialog.picker.wheel.view;

import android.os.Handler;
import android.os.Message;

final class MessageHandler extends Handler {
    public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    public static final int WHAT_SMOOTH_SCROLL = 2000;
    public static final int WHAT_ITEM_SELECTED = 3000;

    private final WheelView mWheelView;

    MessageHandler(WheelView wheelView) {
        this.mWheelView = wheelView;
    }

    @Override
    public final void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_INVALIDATE_LOOP_VIEW:
                mWheelView.invalidate();
                break;

            case WHAT_SMOOTH_SCROLL:
                mWheelView.smoothScroll(WheelView.ACTION.FLING);
                break;

            case WHAT_ITEM_SELECTED:
                mWheelView.onItemSelected();
                break;
        }
    }

}
