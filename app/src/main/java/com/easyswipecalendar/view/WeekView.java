package com.easyswipecalendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyswipecalendar.R;
import com.easyswipecalendar.SwipeApp;
import com.easyswipecalendar.events.OnDateSelectedEvent;
import com.easyswipecalendar.model.CalendarDate;
import com.easyswipecalendar.utils.AppConstants;
import com.squareup.otto.Subscribe;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;

/**
 * Created by sreedhu on 23/11/15.
 */
public class WeekView extends FrameLayout implements View.OnClickListener {

    private DateView mDate1;
    private DateView mDate2;
    private DateView mDate3;
    private DateView mDate4;
    private DateView mDate5;
    private DateView mDate6;
    private DateView mDate7;

    private ArrayList<CalendarDate> mDateList;
    private long mStartDate;
    private long mEndDate;
    private LinearLayout mLinearLayoutDaysContainer;
    private static final String TODAY_KEY = "today_key";
    private LocalDateTime mTodayDate;
    private SwipeCalendarViewCallback mCallback;

    public WeekView(final Context context) {
        super(context);
        initViews();
    }

    public WeekView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public WeekView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeekView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    public void setCallback(SwipeCalendarViewCallback callback) {
        mCallback = callback;
        onEvent(mCallback.getSelectedDate());
    }

    public void setArguments(Bundle args) {
        mTodayDate = (LocalDateTime) args.getSerializable(AppConstants.Keys.TODAY);
        mDateList = (ArrayList<CalendarDate>) args.getSerializable(AppConstants.Keys.DATE_LIST);
        mStartDate = args.getLong(AppConstants.Keys.START_DATE);
        mEndDate = args.getLong(AppConstants.Keys.END_DATE);
        populateDate();
        setTodayKey();
    }

    private void initViews() {
        try {
            SwipeApp.getBus().register(this);
        } catch (IllegalArgumentException e) {
            //bus has already been registered
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_week_viewpager, this, true);
        mLinearLayoutDaysContainer = (LinearLayout) view.findViewById(R.id.dates_lay);
        mDate1 = (DateView) view.findViewById(R.id.date1);
        mDate2 = (DateView) view.findViewById(R.id.date2);
        mDate3 = (DateView) view.findViewById(R.id.date3);
        mDate4 = (DateView) view.findViewById(R.id.date4);
        mDate5 = (DateView) view.findViewById(R.id.date5);
        mDate6 = (DateView) view.findViewById(R.id.date6);
        mDate7 = (DateView) view.findViewById(R.id.date7);

        mDate1.setOnClickListener(this);
        mDate2.setOnClickListener(this);
        mDate3.setOnClickListener(this);
        mDate4.setOnClickListener(this);
        mDate5.setOnClickListener(this);
        mDate6.setOnClickListener(this);
        mDate7.setOnClickListener(this);
    }

    private void populateDate() {
        mDate1.setText(String.valueOf(mDateList.get(0).getDate().getDayOfMonth()));
        mDate2.setText(String.valueOf(mDateList.get(1).getDate().getDayOfMonth()));
        mDate3.setText(String.valueOf(mDateList.get(2).getDate().getDayOfMonth()));
        mDate4.setText(String.valueOf(mDateList.get(3).getDate().getDayOfMonth()));
        mDate5.setText(String.valueOf(mDateList.get(4).getDate().getDayOfMonth()));
        mDate6.setText(String.valueOf(mDateList.get(5).getDate().getDayOfMonth()));
        mDate7.setText(String.valueOf(mDateList.get(6).getDate().getDayOfMonth()));
    }

    private void setTodayKey() {
        mDate1.setContentDescription("");
        mDate2.setContentDescription("");
        mDate3.setContentDescription("");
        mDate4.setContentDescription("");
        mDate5.setContentDescription("");
        mDate6.setContentDescription("");
        mDate7.setContentDescription("");
        if (mDateList.get(0).getDate().equals(mTodayDate)) {
            mDate1.setContentDescription(TODAY_KEY);
        } else if (mDateList.get(1).getDate().equals(mTodayDate)) {
            mDate2.setContentDescription(TODAY_KEY);
        } else if (mDateList.get(2).getDate().equals(mTodayDate)) {
            mDate3.setContentDescription(TODAY_KEY);
        } else if (mDateList.get(3).getDate().equals(mTodayDate)) {
            mDate4.setContentDescription(TODAY_KEY);
        } else if (mDateList.get(4).getDate().equals(mTodayDate)) {
            mDate5.setContentDescription(TODAY_KEY);
        } else if (mDateList.get(5).getDate().equals(mTodayDate)) {
            mDate6.setContentDescription(TODAY_KEY);
        } else if (mDateList.get(6).getDate().equals(mTodayDate)) {
            mDate7.setContentDescription(TODAY_KEY);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            SwipeApp.getBus().register(this);
        } catch (IllegalArgumentException e) {
            //bus has already been registered
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SwipeApp.getBus().unregister(this);
    }

    private void greyOutAdditionalDays() {
        verifyWithinPlusOrMinusDateLimit(mDate1, mDateList.get(0).getDate().toDate().getTime());
        verifyWithinPlusOrMinusDateLimit(mDate2, mDateList.get(1).getDate().toDate().getTime());
        verifyWithinPlusOrMinusDateLimit(mDate3, mDateList.get(2).getDate().toDate().getTime());
        verifyWithinPlusOrMinusDateLimit(mDate4, mDateList.get(3).getDate().toDate().getTime());
        verifyWithinPlusOrMinusDateLimit(mDate5, mDateList.get(4).getDate().toDate().getTime());
        verifyWithinPlusOrMinusDateLimit(mDate6, mDateList.get(5).getDate().toDate().getTime());
        verifyWithinPlusOrMinusDateLimit(mDate7, mDateList.get(6).getDate().toDate().getTime());
    }

    private void verifyWithinPlusOrMinusDateLimit(TextView view, long date) {
        if (date >= mStartDate && date <= mEndDate) {
            view.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.date_black_color_selector));
        } else {
            view.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.date_grey_color_selector));
        }
    }


    private void setDateColorLogic(boolean isLeftToRightSwipe) {
        if (isTwoMonthInThisWeek()) {
            //if two months dates are present in a week
            CalendarDate date = new CalendarDate();
            date.setDate(mCallback.getSelectedDate().getDate());
            boolean isDateSelectedPresent = mDateList.contains(date);
            if (isDateSelectedPresent) {
                //Logic for if selected date is in this week
                //Then enable dates of selected date's month
                setDateColorOnMonthSelected(mDate1, mDateList.get(0).getDate());
                setDateColorOnMonthSelected(mDate2, mDateList.get(1).getDate());
                setDateColorOnMonthSelected(mDate3, mDateList.get(2).getDate());
                setDateColorOnMonthSelected(mDate4, mDateList.get(3).getDate());
                setDateColorOnMonthSelected(mDate5, mDateList.get(4).getDate());
                setDateColorOnMonthSelected(mDate6, mDateList.get(5).getDate());
                setDateColorOnMonthSelected(mDate7, mDateList.get(6).getDate());
            } else {
                //Enable dates based on swipe direction
                //If left to right swipe then enable 0th index date's month
                //If right to left swipe then enable 6th index date's month
                String selectedMonth;
                if (isLeftToRightSwipe) {
                    selectedMonth = mDateList.get(0).getDate().toString(SwipeCalendarView.MONTH_FORMAT);
                } else {
                    selectedMonth = mDateList.get(6).getDate().toString(SwipeCalendarView.MONTH_FORMAT);
                }

                setDateColorOnSwipe(mDate1, mDateList.get(0).getDate(), selectedMonth);
                setDateColorOnSwipe(mDate2, mDateList.get(1).getDate(), selectedMonth);
                setDateColorOnSwipe(mDate3, mDateList.get(2).getDate(), selectedMonth);
                setDateColorOnSwipe(mDate4, mDateList.get(3).getDate(), selectedMonth);
                setDateColorOnSwipe(mDate5, mDateList.get(4).getDate(), selectedMonth);
                setDateColorOnSwipe(mDate6, mDateList.get(5).getDate(), selectedMonth);
                setDateColorOnSwipe(mDate7, mDateList.get(6).getDate(), selectedMonth);
            }
        } else {
            //Disable dates out of plus/minus date limit
            greyOutAdditionalDays();
        }
    }


    private void setDateColorOnMonthSelected(TextView view, LocalDateTime date) {
        if (mCallback.getSelectedDate().getDate().toString(SwipeCalendarView.MONTH_FORMAT)
                    .equals(date.toString(SwipeCalendarView.MONTH_FORMAT))) {
            if (date.toDate().getTime() >= mStartDate && date.toDate().getTime() <= mEndDate) {
                view.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.date_black_color_selector));
            } else {
                view.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.date_grey_color_selector));
            }
        } else {
            view.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.date_grey_color_selector));
        }
    }


    private void setDateColorOnSwipe(TextView view, LocalDateTime date, String selectedMonth) {
        if (selectedMonth.equals(date.toString(SwipeCalendarView.MONTH_FORMAT))) {
            if (date.toDate().getTime() >= mStartDate && date.toDate().getTime() <= mEndDate) {
                view.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.date_black_color_selector));
            } else {
                view.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.date_grey_color_selector));
            }
        } else {
            view.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.date_grey_color_selector));
        }
    }

    @Subscribe
    public void onEvent(OnDateSelectedEvent event) {
        setDateColorLogic(mCallback.isLeftToRightSwipe());
        makeSelected(event.getDate());
    }

    private void resetDateViews() {
        mDate1.setSelected(false);
        mDate2.setSelected(false);
        mDate3.setSelected(false);
        mDate4.setSelected(false);
        mDate5.setSelected(false);
        mDate6.setSelected(false);
        mDate7.setSelected(false);
    }

    private void makeSelected(LocalDateTime localDateTime) {
        CalendarDate date = new CalendarDate();
        date.setDate(localDateTime);
        resetDateViews();
        if (mDateList.contains(date)) {
            int index = mDateList.indexOf(date);

            switch (index) {
                case 0:
                    //changes date background color
                    mDate1.setSelected(true);
                    //changes date text color to white
                    mDate1.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.white));
                    break;
                case 1:
                    mDate2.setSelected(true);
                    mDate2.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.white));
                    break;
                case 2:
                    mDate3.setSelected(true);
                    mDate3.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.white));
                    break;
                case 3:
                    mDate4.setSelected(true);
                    mDate4.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.white));
                    break;
                case 4:
                    mDate5.setSelected(true);
                    mDate5.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.white));
                    break;
                case 5:
                    mDate6.setSelected(true);
                    mDate6.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.white));
                    break;
                case 6:
                    mDate7.setSelected(true);
                    mDate7.setTextColor(ContextCompat.getColor(getContext(),
                            R.color.white));
                    break;
            }
        }
        setDateColorOnCurrentDatePage(localDateTime.equals(mTodayDate));
    }

    private void setDateColorOnCurrentDatePage(boolean isCurrentDateSelected) {
        final int childCount = mLinearLayoutDaysContainer.getChildCount();
        DateView dateTextView = null;
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            dateTextView = (DateView) mLinearLayoutDaysContainer.getChildAt(childIndex);
            if (TODAY_KEY.equals(dateTextView.getContentDescription())) {
                break;
            } else {
                dateTextView = null;
            }
        }
        if (dateTextView != null) {
            if (!isCurrentDateSelected) {
                dateTextView.setBackgroundResource(R.drawable.date_today_unselected_bg_selector);
                dateTextView.setTextColor(ContextCompat.getColorStateList(getContext(),
                        R.color.date_blush_color_selector));
            } else {
                dateTextView.setBackgroundResource(R.drawable.date_bg_selector);
                dateTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        }
    }

    private boolean isTwoMonthInThisWeek() {
        if (mDateList.get(6).getDate().toDate().getTime() > mEndDate) {
            return !mDateList.get(0).getDate().toString(SwipeCalendarView.MONTH_FORMAT)
                            .equals(new LocalDateTime(mEndDate).toString(SwipeCalendarView.MONTH_FORMAT));
        } else {
            return !mDateList.get(0).getDate().toString(SwipeCalendarView.MONTH_FORMAT)
                            .equals(mDateList.get(6).getDate().toString(SwipeCalendarView.MONTH_FORMAT));
        }
    }

    @Override
    public void onClick(final View v) {
        LocalDateTime selectedDate = null;
        int checkedId = v.getId();

        if (checkedId == R.id.date1) {
            selectedDate = mDateList.get(0).getDate();
        } else if (checkedId == R.id.date2) {
            selectedDate = mDateList.get(1).getDate();
        } else if (checkedId == R.id.date3) {
            selectedDate = mDateList.get(2).getDate();
        } else if (checkedId == R.id.date4) {
            selectedDate = mDateList.get(3).getDate();
        } else if (checkedId == R.id.date5) {
            selectedDate = mDateList.get(4).getDate();
        } else if (checkedId == R.id.date6) {
            selectedDate = mDateList.get(5).getDate();
        } else if (checkedId == R.id.date7) {
            selectedDate = mDateList.get(6).getDate();
        }

        if (selectedDate != null && selectedDate.toDate().getTime() >= mStartDate
                    && selectedDate.toDate().getTime() <= mEndDate) {
            mCallback.setSelectedDate(selectedDate);
            SwipeApp.getBus().post(mCallback.getSelectedDate());
        }
    }
}
