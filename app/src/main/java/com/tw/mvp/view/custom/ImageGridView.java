package com.tw.mvp.view.custom;

import android.content.Context;
import android.util.AttributeSet;

public class ImageGridView extends android.widget.GridView {

	public ImageGridView(Context context) {
		super(context);
	}

	public ImageGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
