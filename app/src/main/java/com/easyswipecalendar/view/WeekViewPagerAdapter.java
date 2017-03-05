package com.easyswipecalendar.view;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyswipecalendar.R;
import com.easyswipecalendar.model.CalendarDate;
import com.easyswipecalendar.utils.AppConstants;
import com.easyswipecalendar.utils.CalendarTimeUtils;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by sreedhu on 23/11/15.
 */
public class WeekViewPagerAdapter extends PagerAdapter {
    private int mTotalWeeks;
    private HashMap<Integer, ArrayList<CalendarDate>> mPageDateList;
    private long mStartDate;
    private long mEndDate;
    private LocalDateTime mTodayDate;
    private SwipeCalendarViewCallback mCallback;

    public WeekViewPagerAdapter(int totalWeeks, HashMap<Integer, ArrayList<CalendarDate>> dateList,
                                long startDate, long endDate, SwipeCalendarViewCallback callback) {
        mTotalWeeks = totalWeeks;
        mPageDateList = dateList;
        mStartDate = startDate;
        mEndDate = endDate;
        mTodayDate = CalendarTimeUtils.getTime();
        mCallback = callback;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.Keys.TODAY, mTodayDate);
        args.putSerializable(AppConstants.Keys.DATE_LIST, mPageDateList.get(position));
        args.putLong(AppConstants.Keys.START_DATE, mStartDate);
        args.putLong(AppConstants.Keys.END_DATE, mEndDate);
        LayoutInflater inflater = LayoutInflater.from(collection.getContext());
        WeekView weekView = (WeekView) inflater.inflate(R.layout.item_week_viewpager, collection, false);
        weekView.setArguments(args);
        weekView.setCallback(mCallback);
        collection.addView(weekView);
        return weekView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


    @Override
    public int getCount() {
        return mTotalWeeks;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }
}
