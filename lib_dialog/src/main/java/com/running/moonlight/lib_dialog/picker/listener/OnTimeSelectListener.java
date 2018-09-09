package com.running.moonlight.lib_dialog.picker.listener;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by liuheng on 2018/5/21.
 * 时间选择器的回调
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 */
public abstract class OnTimeSelectListener implements Parcelable {

    public abstract void onTimeSelect(Date date);

    public OnTimeSelectListener() {};

    protected OnTimeSelectListener(Parcel in) {
    }

    public static final Creator<OnTimeSelectListener> CREATOR = new Creator<OnTimeSelectListener>() {
        @Override
        public OnTimeSelectListener createFromParcel(Parcel in) {
            return new OnTimeSelectListener(in) {
                @Override
                public void onTimeSelect(Date date) {

                }
            };
        }

        @Override
        public OnTimeSelectListener[] newArray(int size) {
            return new OnTimeSelectListener[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
