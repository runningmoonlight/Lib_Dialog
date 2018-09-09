package com.running.moonlight.lib_dialog.picker.listener;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuheng on 2018/5/21.
 * 单项选择器的回调
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 */
public abstract class OnOptionSelectListener implements Parcelable {

    public abstract void onOptionSelect(int option);

    public OnOptionSelectListener() {}

    protected OnOptionSelectListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OnOptionSelectListener> CREATOR = new Creator<OnOptionSelectListener>() {
        @Override
        public OnOptionSelectListener createFromParcel(Parcel in) {
            return new OnOptionSelectListener(in) {
                @Override
                public void onOptionSelect(int option) {

                }
            };
        }

        @Override
        public OnOptionSelectListener[] newArray(int size) {
            return new OnOptionSelectListener[size];
        }
    };
}
