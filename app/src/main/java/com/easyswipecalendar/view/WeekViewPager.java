package com.easyswipecalendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class WeekViewPager extends ViewPager {

    private static final int PAGER_PAGE_LIMIT = 3;

    public WeekViewPager(Context context) {
        super(context);
        setOffscreenPageLimit(PAGER_PAGE_LIMIT);
    }

    public WeekViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOffscreenPageLimit(PAGER_PAGE_LIMIT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }
        int heightMeasureSpec1 = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec1);
    }
}
