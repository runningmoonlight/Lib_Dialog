package com.running.moonlight.lib_dialog.picker.wheel.adapter;

import com.running.moonlight.lib_dialog.picker.wheel.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * The simple Array wheel adapter
 *
 * @param <T> the element type
 */
public class ArrayWheelAdapter<T> implements WheelAdapter {

	// items
	private List<T> items = new ArrayList<>();

	private WheelView wheelView;

	/**
	 * Contructor
	 *
	 * @param items the items
	 */
	public ArrayWheelAdapter(List<T> items) {
		this.items.addAll(items);
	}

	@Override
	public Object getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index);
		}
		return "";
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	@Override
	public int indexOf(Object o) {
		return items.indexOf(o);
	}

	public void setItems(List<T> items) {
		this.items.clear();
		if (items != null) {
			this.items.addAll(items);
		}
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