package com.running.moonlight.lib_dialog.picker.listener;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuheng on 2018/5/18.
 * 多项选择器的回调
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 */
public abstract class OnOptionsSelectListener implements Parcelable {

    public abstract void onOptionsSelect(int position1, int position2, int position3);

    public OnOptionsSelectListener() {};

    protected OnOptionsSelectListener(Parcel in) {
    }

    public static final Creator<OnOptionsSelectListener> CREATOR = new Creator<OnOptionsSelectListener>() {
        @Override
        public OnOptionsSelectListener createFromParcel(Parcel in) {
            return new OnOptionsSelectListener(in) {
                @Override
                public void onOptionsSelect(int position1, int position2, int position3) {

                }
            };
        }

        @Override
        public OnOptionsSelectListener[] newArray(int size) {
            return new OnOptionsSelectListener[size];
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
