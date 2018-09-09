package com.running.moonlight.lib_dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.running.moonlight.lib_dialog.base.BaseDialogFragment;
import com.running.moonlight.lib_dialog.base.ViewConvertListener;
import com.running.moonlight.lib_dialog.base.ViewHolder;


/**
 * Created by liuheng on 2018/5/16.
 * 布局简单的自定义Dialog
 */
public class CustomDialog extends BaseDialogFragment {

    private static final String VIEW_CONVERT_LISTENER = "view_convert_listener";

    private ViewConvertListener mConvertListener;

    public static CustomDialog init() {
        return new CustomDialog();
    }

    @Override
    public int initLayoutId() {
        return mLayoutId;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialogFragment dialogFragment) {
        if (mConvertListener != null) {
            mConvertListener.convertView(holder, dialogFragment);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mConvertListener = savedInstanceState.getParcelable(VIEW_CONVERT_LISTENER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(VIEW_CONVERT_LISTENER, mConvertListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mConvertListener = null;
    }

    public CustomDialog setLayoutId(@LayoutRes int layoutId) {
        this.mLayoutId = layoutId;
        return this;
    }

    public CustomDialog setConvertListener(ViewConvertListener convertListener) {
        this.mConvertListener = convertListener;
        return this;
    }
}
