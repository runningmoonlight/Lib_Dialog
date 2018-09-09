package com.running.moonlight.lib_dialog.base;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by liuheng on 2018/5/16.
 * dialog视图中的view的管理类，可通过id设置view的背景、字体颜色、字体大小、点击事件
 */
public class ViewHolder {

    private SparseArray<View> views;
    private View convertView;

    private ViewHolder(View view) {
        convertView = view;
        views = new SparseArray<>();
    }

    public static ViewHolder create(View view) {
        return new ViewHolder(view);
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }

    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setText(int viewId, @StringRes int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
    }

    public void setTextColor(int viewId, @ColorInt int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        view.setOnClickListener(clickListener);
    }

    public void setBackgroundResource(int viewId, @DrawableRes int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    public void setBackgroundColor(int viewId, @ColorInt int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
    }
}
