package com.running.moonlight.lib_dialog.picker.wheel.view;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuheng on 2018/5/21.
 * WheelView选中item时的listener
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 */
public abstract class OnWheelViewItemSelectedListener implements Parcelable {

    public abstract void onItemSelected(int index);

    public OnWheelViewItemSelectedListener() {}

    protected OnWheelViewItemSelectedListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OnWheelViewItemSelectedListener> CREATOR = new Creator<OnWheelViewItemSelectedListener>() {
        @Override
        public OnWheelViewItemSelectedListener createFromParcel(Parcel in) {
            return new OnWheelViewItemSelectedListener(in) {
                @Override
                public void onItemSelected(int index) {

                }
            };
        }

        @Override
        public OnWheelViewItemSelectedListener[] newArray(int size) {
            return new OnWheelViewItemSelectedListener[size];
        }
    };
}
