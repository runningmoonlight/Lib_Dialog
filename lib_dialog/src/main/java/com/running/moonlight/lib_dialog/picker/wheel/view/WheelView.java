package com.running.moonlight.lib_dialog.picker.wheel.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.running.moonlight.lib_dialog.R;
import com.running.moonlight.lib_dialog.picker.entity.IPickerViewEntity;
import com.running.moonlight.lib_dialog.picker.wheel.adapter.WheelAdapter;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 3d滚轮控件
 * update by liuheng at 2018/05/08.
 *
 */
public class WheelView extends View {

    public enum ACTION {
        // 点击，滑翔(滑到尽头)，拖拽事件
        CLICK, FLING, DRAG
    }

    private static final String TAG = "WheelView";

    private Context mContext;
    Handler mHandler;
    private GestureDetector mGestureDetector;
    private OnWheelViewItemSelectedListener mOnItemSelectedListener;

    private ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint mOuterTextPaint;
    private Paint mCenterTextPaint;
    private Paint mDividerPaint;

    private WheelAdapter mAdapter;

    // ------------------------------代码可设置的属性----------------------
    private String mLabel;// 附加单位
    private int mTextSize;// 选项的文字大小
    private int mOutTextColor = 0xffa8a8a8;
    private int mCenterTextColor = 0xff2a2a2a;
    private int mDividerColor = 0xffd5d5d5;

    boolean mLoop;// item是否循环
    private int mGravity = Gravity.CENTER;
    private float mLineSpaceMultiplier = LINE_SPACE_MULTIPLIER;// 条目间距倍数，限制为1.0~2.0
    // ---------------------------------------------------------------

    //	private int mMaxTextWidth;//目前没用
    private int mMaxTextHeight;
    float mItemHeight;// 每行高度

    private static final float LINE_SPACE_MULTIPLIER = 1.4F;

    private float mFirstLineY;// 第一条分隔线Y坐标值
    private float mSecondLineY;// 第二条分隔线Y坐标
    private float mCenterY;// 中间Y坐标

    int mTotalScrollY;// 滚动总高度y值
    int mInitPosition;// 默认选中item的位置
    private int mSelectedItem;// 选中的Item
    private int mPreCurrentIndex;//滑动中实际选中的item

    private int mVisibleItemCount = 11;// 显示几个条目

    private int mMeasuredHeight;
    private int mMeasuredWidth;

    private int mHalfCircumference;// 半圆周长
    private int mRadius;// 半径

    private int mOffset;
    private float mActionY;
    private long mActionDownTime;
    private int  mMarkPosition;// 用于标记Event事件开始时的选中item

    private static final int FLING_VELOCITY = 5;// 修改这个值可以改变滑行速度
    private int mWidthMeasureSpec;

    private int mCenterContentStart;// 中间选中文字开始绘制位置
    private int mOutContentStart;// 非中间文字开始绘制位置
    private float mCenterContentOffsetY = 6F;// 中间文字居中需要此偏移值
    private int mCenterContentOffsetX;// 中间文字X偏移量，防止label和文字内容重叠
    private static final float CONTENT_SCALE = 0.8F;// 非中间文字则用此控制高度，压扁形成3d错觉

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initAttribute(attrs);
        initFlingField();
        initCenterContentOffset();
        initPaints();
    }

    private void initAttribute(AttributeSet attrs) {
        if (mTextSize == 0)
            mTextSize = getResources().getDimensionPixelSize(R.dimen.picker_wv_textsize);
        //处理xml中的自定义属性
        if (attrs != null) {
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.WheelView, 0, 0);
            for (int i = 0, n = a.getIndexCount(); i < n; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.WheelView_wvGravity) {
                    mGravity = a.getInt(attr, Gravity.CENTER);
                } else if (attr == R.styleable.WheelView_wvOuterTextColor) {
                    mOutTextColor = a.getColor(attr, mOutTextColor);
                } else if (attr == R.styleable.WheelView_wvCenterTextColor) {
                    mCenterTextColor = a.getColor(attr, mCenterTextColor);
                } else if (attr == R.styleable.WheelView_wvDividerColor) {
                    mDividerColor = a.getColor(attr, mDividerColor);
                } else if (attr == R.styleable.WheelView_wvTextSize) {
                    mTextSize = a.getDimensionPixelSize(attr, mTextSize);
                } else if (attr == R.styleable.WheelView_wvLineSpaceMultiplier) {
                    mLineSpaceMultiplier = a.getFloat(attr, mLineSpaceMultiplier);
                }
            }
            a.recycle();
        }
    }

    private void initFlingField() {
        mHandler = new MessageHandler(this);
        mGestureDetector = new GestureDetector(mContext, new LoopViewGestureListener(this));
        mGestureDetector.setIsLongpressEnabled(false);
    }

    private void initCenterContentOffset() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density; // 屏幕密度比（0.75/1.0/1.5/2.0/3.0）

        if (density < 1) {//根据密度不同进行适配
            mCenterContentOffsetY = 2.4F;
        } else if (1 <= density && density < 2) {
            mCenterContentOffsetY = 3.6F;
        } else if (1 <= density && density < 2) {
            mCenterContentOffsetY = 4.5F;
        } else if (2 <= density && density < 3) {
            mCenterContentOffsetY = 6.0F;
        } else if (density >= 3) {
            mCenterContentOffsetY = density * 2.5F;
        }
    }

    private void initPaints() {
        mOuterTextPaint = new Paint();
        mOuterTextPaint.setColor(mOutTextColor);
        mOuterTextPaint.setAntiAlias(true);
        mOuterTextPaint.setTypeface(Typeface.MONOSPACE);
        mOuterTextPaint.setTextSize(mTextSize);

        mCenterTextPaint = new Paint();
        mCenterTextPaint.setColor(mCenterTextColor);
        mCenterTextPaint.setAntiAlias(true);
        mCenterTextPaint.setTextScaleX(1.1F);
        mCenterTextPaint.setTypeface(Typeface.MONOSPACE);
        mCenterTextPaint.setTextSize(mTextSize);

        mDividerPaint = new Paint();
        mDividerPaint.setColor(mDividerColor);
        mDividerPaint.setAntiAlias(true);

        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mWidthMeasureSpec = widthMeasureSpec;
        remeasure();
        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
    }

    private void remeasure() {
        if (mAdapter == null) {
            return;
        }

        measureTextMaxWidthAndHeight();

        // 半圆周长 = item高度 * (可见item数量 - 1)
        mHalfCircumference = (int) (mItemHeight * (mVisibleItemCount - 1));
        // 控件高度 = 圆直径 = 圆周长 / 圆周率
        mMeasuredHeight = (int) ((mHalfCircumference * 2) / Math.PI);
        // 半径 = 直径 / 2
        mRadius = mMeasuredHeight / 2;
        // 控件宽度，支持weight
        mMeasuredWidth = MeasureSpec.getSize(mWidthMeasureSpec);
        // 计算两条横线和控件中间点的Y位置
        mFirstLineY = (mMeasuredHeight - mItemHeight) / 2.0F;
        mSecondLineY = (mMeasuredHeight + mItemHeight) / 2.0F;
        mCenterY = mSecondLineY - (mItemHeight - mMaxTextHeight) / 2.0F - mCenterContentOffsetY;
    }

    /**
     * 计算Text的最大宽度和高度
     */
    private void measureTextMaxWidthAndHeight() {
        Rect rect = new Rect();
//		for (int i = 0; i < mAdapter.getItemsCount(); i++) {
//			String s1 = getContentText(mAdapter.getItem(i));
//			mCenterTextPaint.getTextBounds(s1, 0, s1.length(), rect);
//			int textWidth = rect.width();
//			if (textWidth > mMaxTextWidth) {
//				mMaxTextWidth = textWidth;
//			}
//		}
        mCenterTextPaint.getTextBounds("\u661F\u671F", 0, 2, rect); // 星期
        mMaxTextHeight = rect.height();
        mItemHeight = mLineSpaceMultiplier * mMaxTextHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAdapter == null)
            return;

        // 根据滑动距离计算新的mPreCurrentIndex的值
        resetItemIndex();
        // 根据mPreCurrentIndex的值计算需要绘制的数据，
        // 获取数据和绘制都有for循环，本来可以写在一起，考虑代码的阅读性，分开写
        String[] contents = resetItemData();

        // 跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
        int itemHeightOffset = (int) (mTotalScrollY % mItemHeight);
        // 中间两条横线
        canvas.drawLine(0.0F, mFirstLineY, mMeasuredWidth, mFirstLineY, mDividerPaint);
        canvas.drawLine(0.0F, mSecondLineY, mMeasuredWidth, mSecondLineY, mDividerPaint);
        // 单位的Label
        if (!TextUtils.isEmpty(mLabel)) {
            int labelWidth = getTextWidth(mCenterTextPaint, mLabel);//防止文字内容和label重叠
            mCenterContentOffsetX = labelWidth / 2;
            int drawRightContentStart = mMeasuredWidth - labelWidth;
            // 靠右并留出空隙
            canvas.drawText(mLabel, drawRightContentStart - mCenterContentOffsetY, mCenterY, mCenterTextPaint);
        }
        int counter = 0;
        while (counter < mVisibleItemCount) {
            canvas.save();
            // L(弧长)=α（弧度）* r(半径) （弧度制）
            // 求弧度--> (L * π ) / (π * r) (弧长X派/半圆周长)
            float itemHeight = mMaxTextHeight * mLineSpaceMultiplier;
            double radian = ((itemHeight * counter - itemHeightOffset) * Math.PI) / mHalfCircumference;
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            float angle = (float) (90D - (radian / Math.PI) * 180D);
            // 九十度以上的不绘制
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                String contentText = contents[counter];
                // 计算开始绘制的位置
                measuredCenterContentStart(contentText);
                measuredOutContentStart(contentText);
                float translateY = (float) (mRadius - Math.cos(radian) * mRadius - (Math.sin(radian) * mMaxTextHeight) / 2D);
                // 根据Math.sin(radian)来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= mFirstLineY && mMaxTextHeight + translateY >= mFirstLineY) {
                    // 条目经过第一条线
                    canvas.save();
                    canvas.clipRect(0, 0, mMeasuredWidth, mFirstLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * CONTENT_SCALE);
                    canvas.drawText(contentText, mOutContentStart, mMaxTextHeight, mOuterTextPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mFirstLineY - translateY, mMeasuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1F);
                    canvas.drawText(contentText, mCenterContentStart, mMaxTextHeight - mCenterContentOffsetY,
                            mCenterTextPaint);
                    canvas.restore();
                } else if (translateY <= mSecondLineY && mMaxTextHeight + translateY >= mSecondLineY) {
                    // 条目经过第二条线
                    canvas.save();
                    canvas.clipRect(0, 0, mMeasuredWidth, mSecondLineY - translateY);
                    canvas.scale(1.0F, (float) Math.sin(radian) * 1.0F);
                    canvas.drawText(contentText, mCenterContentStart, mMaxTextHeight - mCenterContentOffsetY,
                            mCenterTextPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mSecondLineY - translateY, mMeasuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * CONTENT_SCALE);
                    canvas.drawText(contentText, mOutContentStart,
                            mMaxTextHeight, mOuterTextPaint);
                    canvas.restore();
                } else if (translateY >= mFirstLineY && mMaxTextHeight + translateY <= mSecondLineY) {
                    // 中间条目
                    canvas.clipRect(0, 0, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(contentText, mCenterContentStart, mMaxTextHeight - mCenterContentOffsetY,
                            mCenterTextPaint);
                    // 设置选中项
                    mSelectedItem = mPreCurrentIndex - (mVisibleItemCount / 2 - counter);
                } else {
                    // 其他条目
                    canvas.save();
                    canvas.clipRect(0, 0, mMeasuredWidth, (int) (itemHeight));
                    canvas.scale(1.0F, (float) Math.sin(radian) * CONTENT_SCALE);
                    canvas.drawText(contentText, mOutContentStart, mMaxTextHeight, mOuterTextPaint);
                    canvas.restore();
                }
                canvas.restore();
            }
            counter++;
        }
    }

    private void resetItemIndex() {
        int change = (int) (mTotalScrollY / mItemHeight);
        int itemCount = mAdapter.getItemsCount();
        try {
            mPreCurrentIndex = mInitPosition + change % itemCount;
        } catch (ArithmeticException e) {
            Log.i(TAG, "出错了！mAdapter.getItemsCount() == 0，联动数据不匹配");
        }
        if (mLoop) {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = itemCount + mPreCurrentIndex;
            }
            if (mPreCurrentIndex > itemCount - 1) {
                mPreCurrentIndex = mPreCurrentIndex - itemCount;
            }
        } else {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = 0;
            }
            if (mPreCurrentIndex > itemCount - 1) {
                mPreCurrentIndex = itemCount - 1;
            }
        }
    }

    private String[] resetItemData() {
        // 可见item绘制内容的数组
        String[] contents = new String[mVisibleItemCount];
        int count = 0;
        while (count < mVisibleItemCount) {
            // 索引值，即当前在控件中间的item看作数据源的中间，计算出相对应的index值
            int index = mPreCurrentIndex - (mVisibleItemCount / 2 - count);
            // 如果循环，需要使用循环数据源对应的index
            if (mLoop) {
                index = getLoopMappingIndex(index);
                contents[count] = getContentText(mAdapter.getItem(index));
            } else {
                if (index < 0) {
                    contents[count] = "";
                } else if (index > mAdapter.getItemsCount() - 1) {
                    contents[count] = "";
                } else {
                    contents[count] = getContentText(mAdapter.getItem(index));
                }
            }
            count++;
        }
        return contents;
    }

    // 精确计算文字宽度
    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = mGestureDetector.onTouchEvent(event);
        boolean isIgnore = false;//控制是否重绘UI

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActionDownTime = System.currentTimeMillis();
                cancelFuture();
                mActionY = event.getRawY();
                mMarkPosition = mSelectedItem;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = mActionY - event.getRawY();
                mActionY = event.getRawY();
                mTotalScrollY = (int) (mTotalScrollY + dy);
                // 边界处理
                if (!mLoop) {
                    float top = -mInitPosition * mItemHeight;
                    float bottom = (mAdapter.getItemsCount() - 1 - mInitPosition) * mItemHeight;

                    if ((mTotalScrollY <= top && dy < 0) || (mTotalScrollY >= bottom && dy > 0)) {
                        mTotalScrollY -= dy;
                        isIgnore = true;
                    } else {
                        isIgnore = false;
                    }
                }
                if (!isIgnore) {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                if (!eventConsumed) {

                    float y = event.getY();
                    double l = Math.acos((mRadius - y) / mRadius) * mRadius;
                    int circlePosition = (int) ((l + mItemHeight / 2) / mItemHeight);
                    float extraOffset = (mTotalScrollY % mItemHeight + mItemHeight) % mItemHeight;
                    // extraOffset常常偏差几个像素，导致点击事件会滑动到上一个item去，这里作校正
                    if (extraOffset < 5F || (extraOffset > mItemHeight - 5F && extraOffset < mItemHeight)) {
                        extraOffset = 0F;
                    }
                    mOffset = (int) ((circlePosition - mVisibleItemCount / 2) * mItemHeight - extraOffset);

                    if ((System.currentTimeMillis() - mActionDownTime) > 120) {
                        // 处理拖拽事件
                        smoothScroll(ACTION.DRAG);
                    } else {
                        // 处理条目点击事件
                        smoothScroll(ACTION.CLICK);
                    }
                }
                break;
        }
        return true;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DRAG) {
            mOffset = (int) ((mTotalScrollY % mItemHeight + mItemHeight) % mItemHeight);
            if ((float) mOffset > mItemHeight / 2.0F) {
                mOffset = (int) (mItemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        // 停止的时候，位置有偏移，不是全部都能正确停止到中间位置的，这里把文字位置挪回中间去
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset),
                0, 10, TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY),
                0, FLING_VELOCITY, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    /**
     * 设置是否循环滚动
     */
    public final void setCyclic(boolean loop) {
        this.mLoop = loop;
    }

    /**
     * 设置文字大小
     * @param size 文字大小
     */
    public final void setTextSize(float size) {
        if (size > 0.0F) {
            mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size,
                    mContext.getResources().getDisplayMetrics());
            mOuterTextPaint.setTextSize(mTextSize);
            mCenterTextPaint.setTextSize(mTextSize);
        }
    }

    /**
     * 设置居中的item
     * @param currentItem 居中item的position
     */
    public final void setCurrentItem(int currentItem) {
        this.mInitPosition = currentItem;
        mTotalScrollY = 0;// 回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
        invalidate();
    }

    public final void setOnItemSelectedListener(OnWheelViewItemSelectedListener OnItemSelectedListener) {
        this.mOnItemSelectedListener = OnItemSelectedListener;
    }

    /**
     * 设置WheelView的Adapter
     * @param adapter 数据适配器
     */
    public final void setAdapter(WheelAdapter adapter) {
        this.mAdapter = adapter;
        adapter.registerWheelView(this);
        remeasure();
        invalidate();
    }

    /**
     * 数据改变时的更新UI
     */
    public void onDataChanged() {
        invalidate();
    }

    public final WheelAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 获取当前居中的item的position
     * @return 居中item的position
     */
    public final int getCurrentItem() {
        return mSelectedItem;
    }

    /**
     * 点击非居中的item，会滑到对应的item；
     * 点击居中的item，也会回调这个方法，因此增加mSelectedItem是否改变的判断
     */
    protected final void onItemSelected() {
        if (mOnItemSelectedListener != null && mMarkPosition != mSelectedItem) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOnItemSelectedListener.onItemSelected(mSelectedItem);
                }
            }, 200L);
        }
    }

    // 递归计算出对应的index
    private int getLoopMappingIndex(int index) {
        if (index < 0) {
            index = index + mAdapter.getItemsCount();
            index = getLoopMappingIndex(index);
        } else if (index > mAdapter.getItemsCount() - 1) {
            index = index - mAdapter.getItemsCount();
            index = getLoopMappingIndex(index);
        }
        return index;
    }

    private String getContentText(Object item) {
        if (null == item) {
            return "";
        } else if (item instanceof IPickerViewEntity) {
            return ((IPickerViewEntity) item).getPickerViewText();
        } else if (item instanceof Integer) {
            return String.format(Locale.getDefault(), "%d", (int) item);
        }
        return item.toString();
    }

    private void measuredCenterContentStart(String content) {
        Rect rect = new Rect();
        mCenterTextPaint.getTextBounds(content, 0, content.length(), rect);

        switch (mGravity) {
            case Gravity.CENTER:
                mCenterContentStart = (int) ((mMeasuredWidth - rect.width()) * 0.5) - mCenterContentOffsetX;
                break;
            case Gravity.LEFT:
                mCenterContentStart = 0;
                break;
            case Gravity.RIGHT:
                mCenterContentStart = mMeasuredWidth - rect.width() - mCenterContentOffsetX * 2;
                break;
        }
    }

    private void measuredOutContentStart(String content) {
        Rect rect = new Rect();
        mOuterTextPaint.getTextBounds(content, 0, content.length(), rect);
        switch (mGravity) {
            case Gravity.CENTER:
                mOutContentStart = (int) ((mMeasuredWidth - rect.width()) * 0.5) - mCenterContentOffsetX;
                break;
            case Gravity.LEFT:
                mOutContentStart = 0;
                break;
            case Gravity.RIGHT:
                mOutContentStart = mMeasuredWidth - rect.width() - mCenterContentOffsetX * 2;
                break;
        }
    }

    /**
     * 获取Item个数
     */
    public int getItemsCount() {
        return mAdapter != null ? mAdapter.getItemsCount() : 0;
    }

    /**
     * 附加在右边的单位
     */
    public void setLabel(String label) {
        this.mLabel = label;
    }

    /**
     * 设置item内容的位置
     * @param gravity 居中、居右、居左
     */
    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    public void setOutTextColor(@ColorInt int outTextColor) {
        mOuterTextPaint.setColor(outTextColor);
    }

    public void setCenterTextColor(@ColorInt int centerTextColor) {
        mCenterTextPaint.setColor(centerTextColor);
    }

    public void setDividerColor(@ColorInt int dividerColor) {
        mDividerPaint.setColor(dividerColor);
    }

    private void setLineSpaceMultiplier(float lineSpaceMultiplier) {
        if (lineSpaceMultiplier <1F || lineSpaceMultiplier > 2F) {
            throw new IllegalArgumentException("LineSpaceMultiplier限制为1.0~2.0之间");
        }
        this.mLineSpaceMultiplier = lineSpaceMultiplier;
    }
}
