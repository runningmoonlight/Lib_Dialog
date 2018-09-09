package com.running.moonlight.lib_dialog.base;

import android.app.Dialog;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuheng on 2018/5/21.
 * 获取已定义的dialog的回调
 * 保存状态需要实现Parcelable接口来序列化，所以使用抽象类
 */
public abstract class InitDialogListener implements Parcelable {

    public abstract Dialog create();

    public InitDialogListener() {};

    protected InitDialogListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InitDialogListener> CREATOR = new Creator<InitDialogListener>() {
        @Override
        public InitDialogListener createFromParcel(Parcel in) {
            return new InitDialogListener(in) {
                @Override
                public Dialog create() {
                    return null;
                }
            };
        }

        @Override
        public InitDialogListener[] newArray(int size) {
            return new InitDialogListener[size];
        }
    };
}
