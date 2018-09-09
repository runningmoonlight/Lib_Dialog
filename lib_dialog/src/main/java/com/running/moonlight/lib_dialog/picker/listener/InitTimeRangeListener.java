package com.running.moonlight.lib_dialog.picker.listener;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by liuheng on 2018/5/21.
 * 时间选择器初始化起始和终止时间的回调，只支持设置年月日
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 */
public abstract class InitTimeRangeListener implements Parcelable {

    public abstract Calendar getStartCalendar();

    public abstract Calendar getEndCalendar();

    public abstract Calendar getSelectedCalendar();

    public InitTimeRangeListener() {}

    protected InitTimeRangeListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InitTimeRangeListener> CREATOR = new Creator<InitTimeRangeListener>() {
        @Override
        public InitTimeRangeListener createFromParcel(Parcel in) {
            return new InitTimeRangeListener(in) {
                @Override
                public Calendar getStartCalendar() {
                    return null;
                }

                @Override
                public Calendar getEndCalendar() {
                    return null;
                }

                @Override
                public Calendar getSelectedCalendar() {
                    return null;
                }
            };
        }

        @Override
        public InitTimeRangeListener[] newArray(int size) {
            return new InitTimeRangeListener[size];
        }
    };
}
