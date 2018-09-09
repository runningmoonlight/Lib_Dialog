package com.running.moonlight.lib_dialog.picker.wheel.adapter;


import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;


/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter {

	/** The default min value */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;

	// Values
	private int minValue;
	private int maxValue;

	private WheelView wheelView;

	/**
	 * Default constructor
	 */
	public NumericWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor
	 *
	 * @param minValue
	 *            the wheel min value
	 * @param maxValue
	 *            the wheel max value
	 */
	public NumericWheelAdapter(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public Object getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			return minValue + index;
		}
		return 0;
	}

	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1;
	}

	@Override
	public int indexOf(Object o) {
		return (int) o - minValue;
	}

	public void setValueRange(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		notifyDataSetChanged();
	}

	@Override
	public void registerWheelView(WheelView wheelView) {
		this.wheelView = wheelView;
	}

	@Override
	public void notifyDataSetChanged() {
		if (wheelView != null) {
			wheelView.onDataChanged();
		}
	}
}
