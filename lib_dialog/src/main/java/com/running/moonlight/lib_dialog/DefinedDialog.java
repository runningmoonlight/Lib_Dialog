package com.running.moonlight.lib_dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.running.moonlight.lib_dialog.base.InitDialogListener;


/**
 * Created by liuheng on 2018/5/17.
 * 使用系统dialog或者已定义Dialog的基础类
 */
public class DefinedDialog extends DialogFragment {

    private static final String INIT_DIALOG_LISTENER = "init_dialog_listener";

    private InitDialogListener mInitDialogListener;

    public static DefinedDialog init() {
        return new DefinedDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mInitDialogListener == null)
            throw new IllegalArgumentException("InitDialogListener为null，必须先调用setDialogListener()设置");

        return mInitDialogListener.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mInitDialogListener = savedInstanceState.getParcelable(INIT_DIALOG_LISTENER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INIT_DIALOG_LISTENER, mInitDialogListener);
    }

    public DefinedDialog setInitDialogListener(InitDialogListener dialogListener) {
        this.mInitDialogListener = dialogListener;
        return this;
    }

    /**
     * 自定义显示dialogFragment的方法，防止重复添加
     * @param manager
     * @param tag
     * @return
     */
    public DefinedDialog showDialog(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        if (this.isAdded()) {
            ft.remove(this).commit();
        }
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
        return this;
    }
}
