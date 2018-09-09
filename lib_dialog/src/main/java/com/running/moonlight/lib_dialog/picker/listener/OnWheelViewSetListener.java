package com.running.moonlight.lib_dialog.picker.listener;

import android.os.Parcel;
import android.os.Parcelable;

import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;


/**
 * Created by liuheng on 2018/5/21.
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 * 单项选择器的WheelView的设置回调, 可以设置WheelView的字体大小、字体颜色、分隔线、是否循环、item显示位置、单位
 */
public abstract class OnWheelViewSetListener implements Parcelable {

    public abstract void setWheelView(WheelView wheelView);

    public OnWheelViewSetListener() {}

    protected OnWheelViewSetListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OnWheelViewSetListener> CREATOR = new Creator<OnWheelViewSetListener>() {
        @Override
        public OnWheelViewSetListener createFromParcel(Parcel in) {
            return new OnWheelViewSetListener(in) {
                @Override
                public void setWheelView(WheelView wheelView) {

                }
            };
        }

        @Override
        public OnWheelViewSetListener[] newArray(int size) {
            return new OnWheelViewSetListener[size];
        }
    };
}
