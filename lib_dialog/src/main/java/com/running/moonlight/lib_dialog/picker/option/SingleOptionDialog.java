package com.running.moonlight.lib_dialog.picker.option;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.running.moonlight.lib_dialog.R;
import com.running.moonlight.lib_dialog.base.BaseDialogFragment;
import com.running.moonlight.lib_dialog.base.ViewConvertListener;
import com.running.moonlight.lib_dialog.base.ViewHolder;
import com.running.moonlight.lib_dialog.picker.entity.Constants;
import com.running.moonlight.lib_dialog.picker.entity.IPickerViewEntity;
import com.running.moonlight.lib_dialog.picker.listener.OnOptionSelectListener;
import com.running.moonlight.lib_dialog.picker.listener.OnWheelViewSetListener;
import com.running.moonlight.lib_dialog.picker.wheel.adapter.ArrayWheelAdapter;
import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuheng on 2018/5/17.
 * 单项选择器
 */
public class SingleOptionDialog<T extends Serializable> extends BaseDialogFragment {

    private WheelView mWheelView;
    private ArrayWheelAdapter<T> mAdapter;
    private ArrayList<T> mOptionList;//WheelView数据源
    private int mSelectPosition;//居中item的位置

    private String mTitle;//标题

    private ViewConvertListener mConvertListener;
    private OnOptionSelectListener mOptionSelectListener;
    private OnWheelViewSetListener mWheelViewSetListener;

    /**
     * 需要调用{@link #setOptionList}方法设置数据源
     * @param <T> 数据实现了{@link IPickerViewEntity}接口
     * @return 单项选择器
     */
    public static <T extends IPickerViewEntity & Serializable> SingleOptionDialog<T> init() {
        SingleOptionDialog<T> optionDialog = new SingleOptionDialog<>();
        optionDialog.setAnimStyleId(R.style.SlideBottomAnimation);
        optionDialog.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        optionDialog.setGravity(Gravity.BOTTOM);
        return optionDialog;
    }

    /**
     * 数据源为数字或String时使用
     * @param optionList 数字或String的list
     * @return 单项选择器
     */
    public static <T extends Serializable> SingleOptionDialog<T> init(ArrayList<T> optionList) {
        SingleOptionDialog<T> optionDialog = new SingleOptionDialog<>();
        optionDialog.setOptionList(optionList);
        optionDialog.setAnimStyleId(R.style.SlideBottomAnimation);
        optionDialog.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        optionDialog.setGravity(Gravity.BOTTOM);
        return optionDialog;
    }

    @Override
    public int initLayoutId() {
        return R.layout.view_picker_single_option;
    }

    @Override
    public void convertView(ViewHolder holder, final BaseDialogFragment dialogFragment) {

        holder.setText(R.id.tv_title, mTitle);
        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });
        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOptionSelectListener != null) {
                    mOptionSelectListener.onOptionSelect(mWheelView.getCurrentItem());
                }
                dialogFragment.dismiss();
            }
        });

        if (mConvertListener != null) {
            mConvertListener.convertView(holder, dialogFragment);
        }

        mWheelView = holder.getView(R.id.wheel_view);
        if (mOptionList != null) {
            mAdapter = new ArrayWheelAdapter<>(mOptionList);
            mWheelView.setAdapter(mAdapter);
            mWheelView.setCurrentItem(mSelectPosition);
        }

        if (mWheelViewSetListener != null) {
            mWheelViewSetListener.setWheelView(mWheelView);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mOptionList = (ArrayList<T>) savedInstanceState.getSerializable(Constants.OPTION_LIST);
            mSelectPosition = savedInstanceState.getInt(Constants.SELECT_POSITION, 0);
            mTitle = savedInstanceState.getString(Constants.TITLE);
            mConvertListener = savedInstanceState.getParcelable(Constants.VIEW_CONVERT_LISTENER);
            mOptionSelectListener = savedInstanceState.getParcelable(Constants.OPTION_SELECT_LISTENER);
            mWheelViewSetListener = savedInstanceState.getParcelable(Constants.WHEEL_VIEW_SET_LISTENER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mSelectPosition = mWheelView.getCurrentItem();
        outState.putSerializable(Constants.OPTION_LIST, mOptionList);
        outState.putInt(Constants.SELECT_POSITION, mSelectPosition);
        outState.putString(Constants.TITLE, mTitle);
        outState.putParcelable(Constants.VIEW_CONVERT_LISTENER, mConvertListener);
        outState.putParcelable(Constants.OPTION_SELECT_LISTENER, mOptionSelectListener);
        outState.putParcelable(Constants.WHEEL_VIEW_SET_LISTENER, mWheelViewSetListener);
    }

    public SingleOptionDialog<T> setOptionList(ArrayList<T> optionList) {
        this.mOptionList = optionList;
        return this;
    }

    public SingleOptionDialog<T> setSelectPosition(int selectPosition) {
        this.mSelectPosition = selectPosition;
        return this;
    }

    public SingleOptionDialog<T> setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public SingleOptionDialog<T> setConvertListener(ViewConvertListener convertListener) {
        this.mConvertListener = convertListener;
        return this;
    }

    public SingleOptionDialog<T> setOptionSelectListener(OnOptionSelectListener optionSelectListener) {
        this.mOptionSelectListener = optionSelectListener;
        return this;
    }

    public SingleOptionDialog<T> setWheelViewSetListener(OnWheelViewSetListener wheelViewSetListener) {
        this.mWheelViewSetListener = wheelViewSetListener;
        return this;
    }
}
