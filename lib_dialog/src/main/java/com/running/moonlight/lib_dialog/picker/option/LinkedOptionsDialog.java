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
import com.running.moonlight.lib_dialog.picker.listener.OnOptionsSelectListener;
import com.running.moonlight.lib_dialog.picker.listener.OnWheelViewsSetListener;
import com.running.moonlight.lib_dialog.picker.wheel.adapter.ArrayWheelAdapter;
import com.running.moonlight.lib_dialog.picker.wheel.view.OnWheelViewItemSelectedListener;
import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuheng on 2018/5/17.
 * 联动多项选择器，最多3项
 */
public class LinkedOptionsDialog<A extends Serializable, B extends Serializable, C extends Serializable>
        extends BaseDialogFragment {

    private WheelView mWheelView1, mWheelView2, mWheelView3;
    private ArrayWheelAdapter<A> mAdapter1;
    private ArrayWheelAdapter<B> mAdapter2;
    private ArrayWheelAdapter<C> mAdapter3;
    private ArrayList<A> mOptionList1;
    private ArrayList<ArrayList<B>> mOptionList2;
    private ArrayList<ArrayList<ArrayList<C>>> mOptionList3;
    private int mSelectPosition1, mSelectPosition2, mSelectPosition3;
    private OnWheelViewItemSelectedListener mItemSelectedListener1;
    private OnWheelViewItemSelectedListener mItemSelectedListener2;

    private String mTitle;

    private ViewConvertListener mConvertListener;
    private OnOptionsSelectListener mOptionsSelectListener;
    private OnWheelViewsSetListener mWheelViewsSetListener;

    /**
     * 需要调用{@link #setOptionsList(ArrayList, ArrayList, ArrayList)}方法设置数据源
     */
    public static <A extends IPickerViewEntity & Serializable,
            B extends IPickerViewEntity & Serializable,
            C extends IPickerViewEntity & Serializable> LinkedOptionsDialog<A, B, C> init() {
        LinkedOptionsDialog<A, B, C> linkedOptionsDialog = new LinkedOptionsDialog<>();
        linkedOptionsDialog.setAnimStyleId(R.style.SlideBottomAnimation);
        linkedOptionsDialog.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        linkedOptionsDialog.setGravity(Gravity.BOTTOM);
        return linkedOptionsDialog;
    }

    /**
     * 数据源为数字或String时使用
     */
    public static <A extends Serializable,
            B extends Serializable,
            C extends Serializable> LinkedOptionsDialog<A, B, C> init(ArrayList<A> optionList1,
                                                                      ArrayList<ArrayList<B>> optionList2,
                                                                      ArrayList<ArrayList<ArrayList<C>>> optionList3) {
        LinkedOptionsDialog<A, B, C> linkedOptionsDialog = new LinkedOptionsDialog<>();
        linkedOptionsDialog.setOptionsList(optionList1, optionList2, optionList3);
        linkedOptionsDialog.setAnimStyleId(R.style.SlideBottomAnimation);
        linkedOptionsDialog.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        linkedOptionsDialog.setGravity(Gravity.BOTTOM);
        return linkedOptionsDialog;
    }

    @Override
    public int initLayoutId() {
        return R.layout.view_picker_options;
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
                if (mOptionsSelectListener != null) {
                    mOptionsSelectListener.onOptionsSelect(mWheelView1.getCurrentItem(),
                            mWheelView2.getCurrentItem(), mWheelView3.getCurrentItem());
                }
                dialogFragment.dismiss();
            }
        });

        if (mConvertListener != null) {
            mConvertListener.convertView(holder, dialogFragment);
        }

        mWheelView1 = holder.getView(R.id.wheel_view1);
        mWheelView2 = holder.getView(R.id.wheel_view2);
        mWheelView3 = holder.getView(R.id.wheel_view3);

        if (mOptionList1 != null) {
            mAdapter1 = new ArrayWheelAdapter<>(mOptionList1);
            mWheelView1.setAdapter(mAdapter1);
            mWheelView1.setCurrentItem(mSelectPosition1);
        }

        if (mOptionList2 == null) {
            mWheelView2.setVisibility(View.GONE);
        } else {
            mWheelView2.setVisibility(View.VISIBLE);
            if (mSelectPosition1 <= mOptionList2.size() - 1) {
                mAdapter2 = new ArrayWheelAdapter<>(mOptionList2.get(mSelectPosition1));
                mWheelView2.setAdapter(mAdapter2);
                mWheelView2.setCurrentItem(mSelectPosition2);
            }
            mItemSelectedListener1 = new OnWheelViewItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    if (mOptionList2 != null) {
                        if (index <= mOptionList2.size() - 1) {
                            mAdapter2.setItems(mOptionList2.get(index));
                        } else {
                            mAdapter3.setItems(null);
                        }
                    }
                    // 第一项变化，二三项随着变化，重新置为第0个item居中
                    mWheelView2.setCurrentItem(0);
                    if (mOptionList3 != null && mItemSelectedListener2 != null) {
                        mItemSelectedListener2.onItemSelected(0);
                    }
                }
            };
            mWheelView1.setOnItemSelectedListener(mItemSelectedListener1);
        }

        if (mOptionList3 == null) {
            mWheelView3.setVisibility(View.GONE);
        } else {
            mWheelView3.setVisibility(View.VISIBLE);
            if (mSelectPosition1 <= mOptionList3.size() - 1
                    && mSelectPosition2 <= mOptionList3.get(mSelectPosition1).size() -1) {
                mAdapter3 = new ArrayWheelAdapter<>(mOptionList3.get(mSelectPosition1).get(mSelectPosition2));
                mWheelView3.setAdapter(mAdapter3);
                mWheelView3.setCurrentItem(mSelectPosition3);
            }
            mItemSelectedListener2 = new OnWheelViewItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    int select1 = mWheelView1.getCurrentItem();
                    if (mOptionList3 != null) {
                        if (select1 <= mOptionList3.size()
                                && index <= mOptionList3.get(select1).size() -1) {
                            mAdapter3.setItems(mOptionList3.get(select1).get(index));
                            mWheelView3.setCurrentItem(0);
                        } else  {
                            mAdapter3.setItems(null);
                        }
                    } else {
                        mAdapter3.setItems(null);
                    }
                }
            };
            mWheelView2.setOnItemSelectedListener(mItemSelectedListener2);
        }

        if (mWheelViewsSetListener != null) {
            mWheelViewsSetListener.setWheelViews(mWheelView1, mWheelView2, mWheelView3);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mOptionList1 = (ArrayList<A>) savedInstanceState.getSerializable(Constants.OPTION_LIST_1);
            mOptionList2 = (ArrayList<ArrayList<B>>) savedInstanceState.getSerializable(Constants.OPTION_LIST_2);
            mOptionList3 = (ArrayList<ArrayList<ArrayList<C>>>) savedInstanceState.getSerializable(Constants.OPTION_LIST_3);
            mSelectPosition1 = savedInstanceState.getInt(Constants.SELECT_POSITION_1, 0);
            mSelectPosition2 = savedInstanceState.getInt(Constants.SELECT_POSITION_2, 0);
            mSelectPosition3 = savedInstanceState.getInt(Constants.SELECT_POSITION_3, 0);
            mItemSelectedListener1 = savedInstanceState.getParcelable(Constants.WV_ITEM_SELECTED_LISTENER_1);
            mItemSelectedListener2 = savedInstanceState.getParcelable(Constants.WV_ITEM_SELECTED_LISTENER_2);
            mTitle = savedInstanceState.getString(Constants.TITLE);
            mConvertListener = savedInstanceState.getParcelable(Constants.VIEW_CONVERT_LISTENER);
            mOptionsSelectListener = savedInstanceState.getParcelable(Constants.OPTIONS_SELECT_LISTENER);
            mWheelViewsSetListener = savedInstanceState.getParcelable(Constants.WHEEL_VIEWS_SET_LISTENER);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mSelectPosition1 = mWheelView1.getCurrentItem();
        mSelectPosition2 = mWheelView2.getCurrentItem();
        mSelectPosition3 = mWheelView3.getCurrentItem();
        outState.putSerializable(Constants.OPTION_LIST_1, mOptionList1);
        outState.putSerializable(Constants.OPTION_LIST_2, mOptionList2);
        outState.putSerializable(Constants.OPTION_LIST_3, mOptionList3);
        outState.putInt(Constants.SELECT_POSITION_1, mSelectPosition1);
        outState.putInt(Constants.SELECT_POSITION_2, mSelectPosition2);
        outState.putInt(Constants.SELECT_POSITION_3, mSelectPosition3);
        outState.putParcelable(Constants.WV_ITEM_SELECTED_LISTENER_1, mItemSelectedListener1);
        outState.putParcelable(Constants.WV_ITEM_SELECTED_LISTENER_2, mItemSelectedListener2);
        outState.putString(Constants.TITLE, mTitle);
        outState.putParcelable(Constants.VIEW_CONVERT_LISTENER, mConvertListener);
        outState.putParcelable(Constants.OPTIONS_SELECT_LISTENER, mOptionsSelectListener);
        outState.putParcelable(Constants.WHEEL_VIEWS_SET_LISTENER, mWheelViewsSetListener);
    }

    public LinkedOptionsDialog<A, B, C> setOptionsList(ArrayList<A> optionList1, ArrayList<ArrayList<B>> optionList2,
                                                       ArrayList<ArrayList<ArrayList<C>>> optionList3) {
        this.mOptionList1 = optionList1;
        this.mOptionList2 = optionList2;
        this.mOptionList3 = optionList3;
        return this;
    }

    public LinkedOptionsDialog<A, B, C> setSelectPosition1(int selectPosition1) {
        this.mSelectPosition1 = selectPosition1;
        return this;
    }

    public LinkedOptionsDialog<A, B, C> setSelectPosition2(int selectPosition2) {
        this.mSelectPosition2 = selectPosition2;
        return this;
    }

    public LinkedOptionsDialog<A, B, C> setSelectPosition3(int selectPosition3) {
        this.mSelectPosition3 = selectPosition3;
        return this;
    }

    public LinkedOptionsDialog<A, B, C> setSelectPositions(int position1, int position2, int position3) {
        this.mSelectPosition1 = position1;
        this.mSelectPosition2 = position2;
        this.mSelectPosition3 = position3;
        return this;
    }

    public LinkedOptionsDialog<A, B, C> setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public LinkedOptionsDialog<A, B, C> setConvertListener(ViewConvertListener convertListener) {
        this.mConvertListener = convertListener;
        return this;
    }

    public LinkedOptionsDialog<A, B, C> setOptionsSelectListener(OnOptionsSelectListener optionsSelectListener) {
        this.mOptionsSelectListener = optionsSelectListener;
        return this;
    }

    public LinkedOptionsDialog<A, B, C> setWheelViewsSetListener(OnWheelViewsSetListener wheelViewsSetListener) {
        this.mWheelViewsSetListener = wheelViewsSetListener;
        return this;
    }
}
