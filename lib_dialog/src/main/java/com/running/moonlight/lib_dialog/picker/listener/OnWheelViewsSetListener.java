package com.running.moonlight.lib_dialog.picker.listener;

import android.os.Parcel;
import android.os.Parcelable;

import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;


/**
 * Created by liuheng on 2018/5/18.
 * 多项选择器，设置WheelView的回调
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 * 可设置WheelView的字体大小、字体颜色、分隔线、是否循环、item显示位置、单位
 */
public abstract class OnWheelViewsSetListener implements Parcelable {

    public abstract void setWheelViews(WheelView wheelView1, WheelView wheelView2, WheelView wheelView3);

    public OnWheelViewsSetListener() {};

    protected OnWheelViewsSetListener(Parcel in) {
    }

    public static final Creator<OnWheelViewsSetListener> CREATOR = new Creator<OnWheelViewsSetListener>() {
        @Override
        public OnWheelViewsSetListener createFromParcel(Parcel in) {
            return new OnWheelViewsSetListener(in) {
                @Override
                public void setWheelViews(WheelView wheelView1, WheelView wheelView2, WheelView wheelView3) {

                }
            };
        }

        @Override
        public OnWheelViewsSetListener[] newArray(int size) {
            return new OnWheelViewsSetListener[size];
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
