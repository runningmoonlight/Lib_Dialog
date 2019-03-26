package com.running.moonlight.lib_dialog.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.running.moonlight.lib_dialog.R;
import com.running.moonlight.lib_dialog.utils.ScreenUtils;


/**
 * Created by liuheng on 2018/5/16.
 * 自定义View的dialog的base类
 */
public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = BaseDialogFragment.class.getSimpleName();

    private static final String H_MARGIN = "h_margin";
    private static final String V_MARGIN = "v_margin";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String GRAVITY = "gravity";
    private static final String LOCATION_X = "location_x";
    private static final String LOCATION_Y = "location_y";
    private static final String DIM_AMOUNT = "dim_amount";
    private static final String OUT_CANCELABLE = "out_cancelable";
    private static final String ANIM_STYLE_ID = "anim_style_id";
    private static final String STYLE_ID = "style_id";
    private static final String LAYOUT_ID = "layout_id";

    private int mHMargin;//水平方向边距
    private int mVMargin;//垂直方向边距
    private int mWidth;//宽度
    private int mHeight;//高度
    private int mGravity;//显示位置
    /*
     * lp.x与lp.y表示相对于原始位置的偏移.
     * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
     * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
     * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
     * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
     * 当参数值包含Gravity.CENTER_HORIZONTAL时,
       对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
     * 当参数值包含Gravity.CENTER_VERTICAL时,
       对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
     * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL.
     */
    private int mX;//相对于原始位置的x坐标偏移
    private int mY;//相对于原始位置的y坐标偏移
    private float mDimAmount = 0.3F;//灰度值
    private boolean mOutCancelable = true;//点击外部是否取消
    @StyleRes
    private int mAnimStyleId;//动画
    @StyleRes
    private int mStyleId;//dialog风格
    @LayoutRes
    protected int mLayoutId;//dialog布局

    public abstract int initLayoutId();

    public abstract void convertView(ViewHolder holder, BaseDialogFragment dialogFragment);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mHMargin = savedInstanceState.getInt(H_MARGIN);
            mVMargin = savedInstanceState.getInt(V_MARGIN);
            mWidth = savedInstanceState.getInt(WIDTH);
            mHeight = savedInstanceState.getInt(HEIGHT);
            mGravity = savedInstanceState.getInt(GRAVITY);
            mX = savedInstanceState.getInt(LOCATION_X);
            mY = savedInstanceState.getInt(LOCATION_Y);
            mDimAmount = savedInstanceState.getFloat(DIM_AMOUNT);
            mOutCancelable = savedInstanceState.getBoolean(OUT_CANCELABLE);
            mAnimStyleId = savedInstanceState.getInt(ANIM_STYLE_ID);
            mStyleId = savedInstanceState.getInt(STYLE_ID);
            mLayoutId = savedInstanceState.getInt(LAYOUT_ID);
        }
        setStyle(DialogFragment.STYLE_NO_TITLE, mStyleId == 0 ? R.style.LibDialog : mStyleId);
        mLayoutId = initLayoutId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutId, container, false);
        convertView(ViewHolder.create(view), this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = mDimAmount;

            if (mGravity != 0) {
                lp.gravity = mGravity;
            }
            if (mX != 0) {
                lp.x = mX;
            }
            if (mY != 0) {
                lp.y = mY;
            }

            // dialog宽度
            lp.width = getSize(mWidth, mHMargin);
            // dialog高度
            lp.height = getSize(mHeight, mVMargin);

            // dialog动画
            if (mAnimStyleId != 0) {
                window.setWindowAnimations(mAnimStyleId);
            }

            window.setAttributes(lp);
        }
        setCancelable(mOutCancelable);
    }

    /**
     * 根据设置的高宽，转换为dialog的高宽
     * @param size 设置的高宽
     * @return dialog高宽
     */
    private int getSize(int size, int margin) {
        int result;
        // 不设置，默认为wrap_content
        if (size == 0 || size == WindowManager.LayoutParams.WRAP_CONTENT) {
            result = WindowManager.LayoutParams.WRAP_CONTENT;
        }// match_parent设置为全屏
        else if (size == WindowManager.LayoutParams.MATCH_PARENT) {
            result = ScreenUtils.getScreenWidth(getContext()) - 2 * ScreenUtils.dp2px(getContext(), margin);
        }// 具体数值，dp转换为px
        else {
            result = ScreenUtils.dp2px(getContext(), mWidth);
        }
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(H_MARGIN, mHMargin);
        outState.putInt(V_MARGIN, mVMargin);
        outState.putInt(WIDTH, mWidth);
        outState.putInt(HEIGHT, mHeight);
        outState.putInt(GRAVITY, mGravity);
        outState.putInt(LOCATION_X, mX);
        outState.putInt(LOCATION_Y, mY);
        outState.putFloat(DIM_AMOUNT, mDimAmount);
        outState.putBoolean(OUT_CANCELABLE, mOutCancelable);
        outState.putInt(ANIM_STYLE_ID, mAnimStyleId);
        outState.putInt(STYLE_ID, mStyleId);
        outState.putInt(LAYOUT_ID, mLayoutId);
    }

    /**
     * 自定义显示dialogFragment的方法，防止重复添加
     * @param manager manager
     * @param tag tag
     * @return BaseDialogFragment，因为返回值不一样，不重写show方法
     */
    public BaseDialogFragment showDialog(FragmentManager manager, String tag) {
        manager.executePendingTransactions();//确保之前的dialog操作执行完成
        if (this.isAdded()) {//防止重复添加的错误
            return this;
        }

        //DialogFragment的show方法默认调用commit()，可能会导致IllegalStateException : Can not perform
        // this action after onSaveInstanceSate；
        //DialogFragment没有commitAllowingStateLoss()方法，为防止异常，这里将异常捕获；
        //如果DialogFragment在Activity的生命周期方法中调用，建议DialogFragment在Activity#onCreate()
        // 或者Activity#onPostResume()或者FragmentActivity#onResumeFragments()中调用，这样确保状态
        // 被保存，不会抛出异常
        try {
            super.show(manager, tag);
        } catch (IllegalStateException ignore) {
            //异常忽略
        }
        return this;
    }
    public BaseDialogFragment setHMargin(int hMargin) {
        this.mHMargin = hMargin;
        return this;
    }

    public BaseDialogFragment setVMargin(int vMargin) {
        this.mVMargin = vMargin;
        return this;
    }

    public BaseDialogFragment setWidth(int width) {
        this.mWidth = width;
        return this;
    }

    public BaseDialogFragment setHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public BaseDialogFragment setGravity(int gravity) {
        this.mGravity = gravity;
        return this;
    }

    public BaseDialogFragment setX(int x) {
        this.mX = x;
        return this;
    }

    public BaseDialogFragment setY(int y) {
        this.mY = y;
        return this;
    }

    public BaseDialogFragment setDimAmount(float dimAmount) {
        this.mDimAmount = dimAmount;
        return this;
    }

    public BaseDialogFragment setOutCancelable(boolean outCancelable) {
        this.mOutCancelable = outCancelable;
        return this;
    }

    public BaseDialogFragment setAnimStyleId(int animStyleId) {
        this.mAnimStyleId = animStyleId;
        return this;
    }

    public BaseDialogFragment setStyleId(int styleId) {
        this.mStyleId = styleId;
        return this;
    }
}
