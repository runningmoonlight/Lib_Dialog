package com.running.moonlight.lib_dialog.picker.time;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.running.moonlight.lib_dialog.R;
import com.running.moonlight.lib_dialog.base.BaseDialogFragment;
import com.running.moonlight.lib_dialog.base.ViewConvertListener;
import com.running.moonlight.lib_dialog.base.ViewHolder;
import com.running.moonlight.lib_dialog.picker.entity.Constants;
import com.running.moonlight.lib_dialog.picker.listener.InitTimeRangeListener;
import com.running.moonlight.lib_dialog.picker.listener.OnTimeSelectListener;
import com.running.moonlight.lib_dialog.picker.listener.OnTimeWheelViewsSetListener;
import com.running.moonlight.lib_dialog.picker.wheel.adapter.NumericWheelAdapter;
import com.running.moonlight.lib_dialog.picker.wheel.view.OnWheelViewItemSelectedListener;
import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by liuheng on 2018/5/18.
 * 时间选择器，支持年、月、日、时、分、秒
 */
public class TimeDialog extends BaseDialogFragment {

    private WheelView mYearWv, mMonthWv, mDayWv, mHourWv,mMinuteWv, mSecondWv;
    private NumericWheelAdapter mYearAdapter, mMonthAdapter, mDayAdapter;
    private boolean[] mWvVisibility = {true, true, true, false, false, false};
    private int mStartYear = 1900;
    private int mEndYear = 2100;
    private int mStartMonth = 1;
    private int mEndMonth = 12;
    private int mStartDay = 1;
    private int mEndDay = 31;

    /*
     * 这里selected的年月日时分秒，是传入的时间，不是item的index
     * 这些字段只在第一次初始化的时候有用
     * 比如2018/05/19 10:02:21
     * 对应2018、4、19、10、2、21
     * 注意：月份从0开始
     */
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;
    private int mSelectedHour;
    private int mSelectedMinute;
    private int mSelectedSecond;

    private List<String> mBigMonthList = Arrays.asList("1", "3", "5", "7", "8", "10", "12");
    private List<String> mSmallMonthList = Arrays.asList("4", "6", "9", "11");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String mTitle;

    private ViewConvertListener mConvertListener;
    private OnTimeSelectListener mTimeSelectListener;
    private OnTimeWheelViewsSetListener mTimeWheelViewsSetListener;
    private InitTimeRangeListener mInitRangeListener;

    public static TimeDialog init() {
        TimeDialog timeDialog = new TimeDialog();
        // 设置默认时间为当前时间
        timeDialog.setSelectedTime(Calendar.getInstance());

        timeDialog.setAnimStyleId(R.style.SlideBottomAnimation);
        timeDialog.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        timeDialog.setGravity(Gravity.BOTTOM);
        return timeDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mWvVisibility = savedInstanceState.getBooleanArray(Constants.WV_VISIBILITY);
            mStartYear = savedInstanceState.getInt(Constants.START_YEAR, 1900);
            mEndYear = savedInstanceState.getInt(Constants.END_YEAR, 2100);
            mStartMonth = savedInstanceState.getInt(Constants.START_MONTH, 1);
            mEndMonth = savedInstanceState.getInt(Constants.END_MONTH, 12);
            mStartDay = savedInstanceState.getInt(Constants.START_DAY, 1);
            mEndDay = savedInstanceState.getInt(Constants.END_DAY, 31);
            mSelectedYear = savedInstanceState.getInt(Constants.SELECTED_YEAR);
            mSelectedMonth = savedInstanceState.getInt(Constants.SELECTED_MONTH);
            mSelectedDay = savedInstanceState.getInt(Constants.SELECTED_DAY);
            mSelectedHour = savedInstanceState.getInt(Constants.SELECTED_HOUR);
            mSelectedMinute = savedInstanceState.getInt(Constants.SELECTED_MINUTE);
            mSelectedSecond = savedInstanceState.getInt(Constants.SELECTED_SECOND);
            mTitle = savedInstanceState.getString(Constants.TITLE);
            mTimeSelectListener = savedInstanceState.getParcelable(Constants.TIME_SELECT_LISTENER);
            mTimeWheelViewsSetListener = savedInstanceState.getParcelable(Constants.TIME_WHEEL_VIEWS_SET_LISTENER);
            mInitRangeListener = savedInstanceState.getParcelable(Constants.TIME_INIT_RANGE_LISTENER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mSelectedYear = mYearWv.getCurrentItem() + mStartYear;
        mSelectedMonth = mMonthWv.getCurrentItem() - 1 + mStartMonth;
        mSelectedDay = mDayWv.getCurrentItem() + mStartDay;
        mSelectedHour = mHourWv.getCurrentItem();
        mSelectedMinute = mMinuteWv.getCurrentItem();
        mSelectedSecond = mSecondWv.getCurrentItem();
        outState.putBooleanArray(Constants.WV_VISIBILITY, mWvVisibility);
        outState.putInt(Constants.START_YEAR, mStartYear);
        outState.putInt(Constants.END_YEAR, mEndYear);
        outState.putInt(Constants.START_MONTH, mStartMonth);
        outState.putInt(Constants.END_MONTH, mEndMonth);
        outState.putInt(Constants.START_DAY, mStartDay);
        outState.putInt(Constants.END_DAY, mEndDay);
        outState.putInt(Constants.SELECTED_YEAR, mSelectedYear);
        outState.putInt(Constants.SELECTED_MONTH, mSelectedMonth);
        outState.putInt(Constants.SELECTED_DAY, mSelectedDay);
        outState.putInt(Constants.SELECTED_HOUR, mSelectedHour);
        outState.putInt(Constants.SELECTED_MINUTE, mSelectedMinute);
        outState.putInt(Constants.SELECTED_SECOND, mSelectedSecond);
        outState.putString(Constants.TITLE, mTitle);
        outState.putParcelable(Constants.TIME_SELECT_LISTENER, mTimeSelectListener);
        outState.putParcelable(Constants.TIME_WHEEL_VIEWS_SET_LISTENER, mTimeWheelViewsSetListener);
        outState.putParcelable(Constants.TIME_INIT_RANGE_LISTENER, mInitRangeListener);
    }

    @Override
    public int initLayoutId() {
        return R.layout.view_picker_time;
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
                if (mTimeSelectListener != null) {
                    mTimeSelectListener.onTimeSelect(getTime());
                }
                dialogFragment.dismiss();
            }
        });
        if (mConvertListener != null) {
            mConvertListener.convertView(holder, dialogFragment);
        }

        if (mInitRangeListener != null) {
            setRangeTime(mInitRangeListener.getStartCalendar(), mInitRangeListener.getEndCalendar());
            setSelectedTime(mInitRangeListener.getSelectedCalendar());
        }

        mYearWv = holder.getView(R.id.wv_year);
        mMonthWv = holder.getView(R.id.wv_month);
        mDayWv = holder.getView(R.id.wv_day);
        mHourWv = holder.getView(R.id.wv_hour);
        mMinuteWv = holder.getView(R.id.wv_minute);
        mSecondWv = holder.getView(R.id.wv_second);

        mYearAdapter = new NumericWheelAdapter(mStartYear, mEndYear);
        mMonthAdapter = new NumericWheelAdapter(mStartMonth, mEndMonth);
        mDayAdapter = new NumericWheelAdapter(1, 31);

        //年
        mYearWv.setLabel(getString(R.string.picker_time_year));
        mYearWv.setAdapter(mYearAdapter);
        mYearWv.setCurrentItem(mSelectedYear - mStartYear);
        mYearWv.setOnItemSelectedListener(new OnWheelViewItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int selectYear = index + mStartYear;
                int monthIndex = mMonthWv.getCurrentItem();
                int selectMonth;
                int dayIndex = mDayWv.getCurrentItem();
                int selectDay;
                if (mStartYear == mEndYear) {
                    mMonthAdapter.setValueRange(mStartMonth, mEndMonth);
                    if (monthIndex > mMonthAdapter.getItemsCount() - 1) {
                        monthIndex = mMonthAdapter.getItemsCount() - 1;
                        mMonthWv.setCurrentItem(monthIndex);
                    }
                    selectMonth = monthIndex + mStartMonth - 1;
                    if (mStartMonth == mEndMonth) {
                        selectDay = dayIndex + mStartDay;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, mStartDay, mEndDay);
                    } else if (selectMonth == mStartMonth - 1) {
                        selectDay = dayIndex + mStartDay;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, mStartDay, 31);
                    } else if (selectMonth == mEndMonth - 1) {
                        selectDay = dayIndex + 1;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, mEndDay);
                    } else {
                        selectDay = dayIndex + 1;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, 31);
                    }
                } else if (selectYear == mStartYear) {
                    mMonthAdapter.setValueRange(mStartMonth, 12);
                    if (monthIndex > mMonthAdapter.getItemsCount() - 1) {
                        monthIndex = mMonthAdapter.getItemsCount() - 1;
                        mMonthWv.setCurrentItem(monthIndex);
                    }
                    selectMonth = monthIndex + mStartMonth - 1;
                    if (selectMonth == mStartMonth - 1) {
                        selectDay = dayIndex + mStartDay;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, mStartDay, 31);
                    } else {
                        selectDay = dayIndex + 1;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, 31);
                    }
                } else if (selectYear == mEndYear) {
                    mMonthAdapter.setValueRange(1, mEndMonth);
                    if (monthIndex > mMonthAdapter.getItemsCount() - 1) {
                        monthIndex = mMonthAdapter.getItemsCount() - 1;
                        mMonthWv.setCurrentItem(monthIndex);
                    }
                    selectMonth = monthIndex + mStartMonth - 1;
                    selectDay = dayIndex + 1;
                    if (selectMonth == mEndMonth - 1) {
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, mEndDay);
                    } else {
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, 31);
                    }
                } else {
                    mMonthAdapter.setValueRange(1, 12);
                    selectMonth = monthIndex + mStartMonth - 1;
                    selectDay = dayIndex + 1;
                    setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, 31);
                }
            }
        });

        // 月
        mMonthWv.setLabel(getString(R.string.picker_time_month));
        mMonthWv.setAdapter(mMonthAdapter);
        if (mStartYear == mEndYear) {
            mMonthAdapter.setValueRange(mStartMonth, mEndMonth);
            mMonthWv.setCurrentItem(mSelectedMonth + 1 - mStartMonth);
        } else if (mSelectedYear == mStartYear) {
            mMonthAdapter.setValueRange(mStartMonth, 12);
            mMonthWv.setCurrentItem(mSelectedMonth + 1 - mStartMonth);
        } else if (mSelectedYear == mEndYear) {
            mMonthAdapter.setValueRange(1, mEndMonth);
            mMonthWv.setCurrentItem(mSelectedMonth > mEndMonth ? mEndMonth : mSelectedMonth);
        } else {
            mMonthAdapter.setValueRange(1, 12);
            mMonthWv.setCurrentItem(mSelectedMonth);
        }
        mMonthWv.setOnItemSelectedListener(new OnWheelViewItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int selectYear = mStartYear + mYearWv.getCurrentItem();
                int selectMonth;
                int dayIndex = mDayWv.getCurrentItem();
                int selectDay;
                if (mStartYear == mEndYear) {
                    selectMonth = index + mStartMonth - 1;
                    if (mStartMonth == mEndMonth) {
                        selectDay = dayIndex + mStartDay;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, mStartDay, mEndDay);
                    } else if (selectMonth == mStartMonth - 1) {
                        selectDay = dayIndex + mStartDay;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, mStartDay, 31);
                    } else if (selectMonth == mEndMonth - 1) {
                        selectDay = dayIndex + 1;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, mEndDay);
                    } else {
                        selectDay = dayIndex + 1;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, 31);
                    }
                } else if (selectYear == mStartYear) {
                    selectMonth = index + mStartMonth - 1;
                    if (selectMonth == mStartMonth - 1) {
                        selectDay = dayIndex + mStartDay;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, mStartDay, 31);
                    } else {
                        selectDay = dayIndex + 1;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, 31);
                    }
                } else if (selectYear == mEndYear) {
                    selectMonth = index;
                    if (selectMonth == mEndMonth - 1) {
                        selectDay = dayIndex + 1;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, mEndDay);
                    } else {
                        selectDay = dayIndex + 1;
                        setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, 31);
                    }
                } else {
                    selectMonth = index;
                    selectDay = dayIndex + 1;
                    setDayAdapterAndCurrentItem(selectYear, selectMonth, selectDay, 1, 31);
                }
            }
        });

        // 日
        mDayWv.setLabel(getString(R.string.picker_time_day));
        mDayWv.setAdapter(mDayAdapter);
        if (mStartYear == mEndYear && mStartMonth == mEndMonth) {
            setDayAdapterAndCurrentItem(mSelectedYear, mSelectedMonth, mSelectedDay, mStartDay, mEndDay);
        } else if (mSelectedYear == mStartYear && mSelectedMonth + 1 == mStartMonth) {
            setDayAdapterAndCurrentItem(mSelectedYear, mSelectedMonth, mSelectedDay, mStartDay, 31);
        } else if (mSelectedYear == mEndYear && mSelectedMonth + 1 == mEndMonth) {
            setDayAdapterAndCurrentItem(mSelectedYear, mSelectedMonth, mSelectedDay, 1, mEndDay);
        } else {
            setDayAdapterAndCurrentItem(mSelectedYear, mSelectedMonth, mSelectedDay, 1, 31);
        }

        // 时
        mHourWv.setLabel(getString(R.string.picker_time_hour));
        mHourWv.setAdapter(new NumericWheelAdapter(0, 23));
        mHourWv.setCurrentItem(mSelectedHour);

        // 分
        mMinuteWv.setLabel(getString(R.string.picker_time_minute));
        mMinuteWv.setAdapter(new NumericWheelAdapter(0, 59));
        mMinuteWv.setCurrentItem(mSelectedMinute);

        // 秒
        mSecondWv.setLabel(getString(R.string.picker_time_second));
        mSecondWv.setAdapter(new NumericWheelAdapter(0, 59));
        mSecondWv.setCurrentItem(mSelectedSecond);

        mYearWv.setVisibility(mWvVisibility[0] ? View.VISIBLE : View.GONE);
        mMonthWv.setVisibility(mWvVisibility[1] ? View.VISIBLE : View.GONE);
        mDayWv.setVisibility(mWvVisibility[2] ? View.VISIBLE : View.GONE);
        mHourWv.setVisibility(mWvVisibility[3] ? View.VISIBLE : View.GONE);
        mMinuteWv.setVisibility(mWvVisibility[4] ? View.VISIBLE : View.GONE);
        mSecondWv.setVisibility(mWvVisibility[5] ? View.VISIBLE : View.GONE);

        if (mTimeWheelViewsSetListener != null) {
            mTimeWheelViewsSetListener.setTimeWheelViews(mYearWv, mMonthWv, mDayWv, mHourWv, mMinuteWv, mSecondWv);
        }
    }

    // 是否是闰年
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    private void setDayAdapterAndCurrentItem(int selectYear, int selectMonth, int selectDay, int startDay, int endDay) {
        if (mBigMonthList.contains(String.valueOf(selectMonth + 1))) {
            mDayAdapter.setValueRange(startDay, endDay > 31 ? 31 : endDay);
        } else if (mSmallMonthList.contains(String.valueOf(selectMonth + 1))) {
            mDayAdapter.setValueRange(startDay, endDay > 30 ? 30 : endDay);
        } else {
            if (isLeapYear(selectYear)) {
                mDayAdapter.setValueRange(startDay, endDay > 29 ? 29 : endDay);
            } else {
                mDayAdapter.setValueRange(startDay, endDay > 28 ? 28 : endDay);
            }
        }

        int dayIndex = selectDay - startDay;
        if (dayIndex > mDayAdapter.getItemsCount() - 1) {
            dayIndex = mDayAdapter.getItemsCount() - 1;
        }
        mDayWv.setCurrentItem(dayIndex);
    }

    private Date getTime() {
        StringBuilder sb = new StringBuilder();
        sb.append(getWvText(mYearWv)).append("-")
                .append(getWvText(mMonthWv)).append("-")
                .append(getWvText(mDayWv)).append(" ")
                .append(getWvText(mHourWv)).append(":")
                .append(getWvText(mMinuteWv)).append(":")
                .append(getWvText(mSecondWv));
        Date date = null;
        try {
            date = DATE_FORMAT.parse(sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String getWvText(WheelView wheelView) {
        String text = "00";
        if (wheelView.getAdapter() != null) {
            text = String.valueOf(wheelView.getAdapter().getItem(wheelView.getCurrentItem()));
        }
        return text;
    }

    public TimeDialog setMvVisibility(boolean year, boolean month, boolean day, boolean hour,
                                      boolean minute, boolean second) {
        mWvVisibility[0] = year;
        mWvVisibility[1] = month;
        mWvVisibility[2] = day;
        mWvVisibility[3] = hour;
        mWvVisibility[4] = minute;
        mWvVisibility[5] = second;
        return this;
    }

    public TimeDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public TimeDialog setInitTimeRangeListener(InitTimeRangeListener initRangeListener) {
        this.mInitRangeListener = initRangeListener;
        return this;
    }

    public TimeDialog setConvertListener(ViewConvertListener convertListener) {
        this.mConvertListener = convertListener;
        return this;
    }

    public TimeDialog setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.mTimeSelectListener = timeSelectListener;
        return this;
    }

    public TimeDialog setOnTimeWheelViewsSetListener(OnTimeWheelViewsSetListener timeWheelViewsSetListener) {
        this.mTimeWheelViewsSetListener = timeWheelViewsSetListener;
        return this;
    }

    /**
     * 设置默认时间
     * @param calendar 默认时间
     * @return timeDialog
     */
    public TimeDialog setSelectedTime(Calendar calendar) {
        if (calendar != null) {
            this.mSelectedYear = calendar.get(Calendar.YEAR);
            this.mSelectedMonth = calendar.get(Calendar.MONTH);
            this.mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
            this.mSelectedHour = calendar.get(Calendar.HOUR_OF_DAY);
            this.mSelectedMinute = calendar.get(Calendar.MINUTE);
            this.mSelectedSecond = calendar.get(Calendar.SECOND);
        }
        return this;
    }

    public TimeDialog setSelectedTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setSelectedTime(calendar);
        return this;
    }

    /**
     * 设置时间范围
     * @param startCalendar 起始时间
     * @param endCalendar 结束时间
     * @return timeDialog
     */
    public TimeDialog setRangeTime(Calendar startCalendar, Calendar endCalendar) {
        if (startCalendar != null) {
            mStartYear = startCalendar.get(Calendar.YEAR);
            mStartMonth = startCalendar.get(Calendar.MONTH) + 1;
            mStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        }
        if (endCalendar != null) {
            mEndYear = endCalendar.get(Calendar.YEAR);
            mEndMonth = endCalendar.get(Calendar.MONTH) + 1;
            mEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        }

        if (mStartYear > mEndYear || (mStartYear == mEndYear && mStartMonth > mEndMonth)
                || (mStartYear == mEndYear && mStartMonth == mEndMonth && mStartDay > mEndDay)) {
            throw new IllegalArgumentException(String.format("起始时间为%4d/%2d/%2d, 结束时间为%4d/%2d%2d,"
                    + "起始时间不能小于结束时间", mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay));
        }

        return this;
    }

    public TimeDialog setRangeTime(Date startDate, Date endDate) {
        Calendar startCalendar = null;
        Calendar endCalendar = null;
        if (startDate != null) {
            startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
        }
        if (endDate != null) {
            endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
        }
        setRangeTime(startCalendar, endCalendar);
        return this;
    }
}
