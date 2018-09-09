package com.running.moonlight.lib_dialog.picker.listener;

import android.os.Parcel;
import android.os.Parcelable;

import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;


/**
 * Created by liuheng on 2018/5/21.
 * 时间选择器中WheelView的设置的回调
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 */
public abstract class OnTimeWheelViewsSetListener implements Parcelable {

    public abstract void setTimeWheelViews(WheelView yearWv, WheelView monthWv, WheelView dayWv, WheelView hourWv,
                                           WheelView minuteWv, WheelView secondWv);

    public OnTimeWheelViewsSetListener() {};

    protected OnTimeWheelViewsSetListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OnTimeWheelViewsSetListener> CREATOR = new Creator<OnTimeWheelViewsSetListener>() {
        @Override
        public OnTimeWheelViewsSetListener createFromParcel(Parcel in) {
            return new OnTimeWheelViewsSetListener(in) {
                @Override
                public void setTimeWheelViews(WheelView yearWv, WheelView monthWv, WheelView dayWv, WheelView hourWv, WheelView minuteWv, WheelView secondWv) {

                }
            };
        }

        @Override
        public OnTimeWheelViewsSetListener[] newArray(int size) {
            return new OnTimeWheelViewsSetListener[size];
        }
    };
}
