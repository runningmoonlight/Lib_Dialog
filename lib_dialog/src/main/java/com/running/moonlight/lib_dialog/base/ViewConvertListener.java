package com.running.moonlight.lib_dialog.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuheng on 2018/5/16.
 * 自定义Dialog的view的样式的listener
 */
public abstract class ViewConvertListener implements Parcelable{

    public abstract void convertView(ViewHolder holder, BaseDialogFragment dialogFragment);

    public ViewConvertListener() {}

    protected ViewConvertListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ViewConvertListener> CREATOR = new Creator<ViewConvertListener>() {
        @Override
        public ViewConvertListener createFromParcel(Parcel in) {
            return new ViewConvertListener(in) {
                @Override
                public void convertView(ViewHolder holder, BaseDialogFragment dialogFragment) {

                }
            };
        }

        @Override
        public ViewConvertListener[] newArray(int size) {
            return new ViewConvertListener[size];
        }
    };
}
