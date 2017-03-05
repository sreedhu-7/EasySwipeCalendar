package com.easyswipecalendar.view;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyswipecalendar.R;
import com.easyswipecalendar.SwipeApp;
import com.easyswipecalendar.events.OnDateSelectedEvent;
import com.easyswipecalendar.model.CalendarCoreData;
import com.easyswipecalendar.model.CalendarDate;
import com.easyswipecalendar.model.CalendarMode;
import com.easyswipecalendar.presenter.SwipeCalendarPresenter;
import com.easyswipecalendar.utils.CalendarTimeUtils;
import com.squareup.otto.Subscribe;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sreedhu on 19/11/15.
 */
public class SwipeCalendarView extends ViewGroup implements ViewPager.OnPageChangeListener, View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        SwipeCalendarViewCallback {
    private OnDateSelectedEvent mDateEventSelected;
    private TextView mTodayTextView;
    private TextView mTodayRightTextView;
    private Button mMonthTextView;
    private LinearLayout mDaysLay;
    private WeekViewPager mPager;
    private View mLine;

    private int mPastLimit = 10;
    private int mFutureLimit = 10;

    private int mTodayWeekPage;
    private FragmentManager mFragmentManager;
    private int mPreviousWeekPage = -1;
    private int mCurrentWeekPage = -1;

    private OnDateClickListener mOnDateClickListener;
    private OnMonthClickListener mOnMonthClickListener;
    private SwipeCalendarPresenter mSwipeCalendarPresenter;
    private CalendarCoreData mCalendarCoreData;
    private LocalDateTime mTargetDate = CalendarTimeUtils.getTime();
    private LocalDateTime mDateDialogFocusDate;

    private static final int DAYS_IN_WEEK = 7;
    private LocalDateTime mTodayDate;
    public static final String MONTH_FORMAT = "MMMM";
    public static final String MONTH_DISPLAY_FORMAT = "MMM YYYY";
    private LinearLayout.LayoutParams mLayoutParams;
    private int mScrollState;

    public SwipeCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        SwipeApp.getBus().register(this);
        mSwipeCalendarPresenter = new SwipeCalendarPresenter();
        mSwipeCalendarPresenter.init(this);
    }

    public SwipeCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar_swipeview, this, true);
        mTodayTextView = (TextView) findViewById(R.id.text_today);
        mTodayRightTextView = (TextView) findViewById(R.id.text_today_right);
        mMonthTextView = (Button) findViewById(R.id.text_month);
        mMonthTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextPrimaryNormal));
        VectorDrawableCompat dropDownIcon = VectorDrawableCompat.create(getResources(), R.drawable.vc_arrow_drop_down,
                null);
        mDaysLay = (LinearLayout) findViewById(R.id.days_lay);
        mPager = (WeekViewPager) findViewById(R.id.pager_date);
        mLine = findViewById(R.id.view_line);
        mTodayTextView.setOnClickListener(this);
        mTodayRightTextView.setOnClickListener(this);
        mMonthTextView.setOnClickListener(this);
        assignVectorDrawable();
    }

    private void assignVectorDrawable() {
        VectorDrawableCompat vectorLeftArrow = VectorDrawableCompat.create(getResources(),
                R.drawable.vc_today_arrow_left, null);
        VectorDrawableCompat vectorRightArrow = VectorDrawableCompat.create(getResources(),
                R.drawable.vc_today_arrow_right, null);
        mTodayTextView.setCompoundDrawablesWithIntrinsicBounds(vectorLeftArrow, null, null, null);
        mTodayRightTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, vectorRightArrow, null);
    }


    /**
     * Sets up the Calendar
     * Computes total dates, total pages in viewpager, today's date as default selected date,
     * viewpager's default selected position will be the today's date page
     *
     * @param calendarCoreData
     */
    public void initCalendar(CalendarCoreData calendarCoreData, CalendarMode calendarMode) {
        mCalendarCoreData = calendarCoreData;
        mTodayDate = CalendarTimeUtils.getTime();
        mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        WeekViewPagerAdapter adapter =
                new WeekViewPagerAdapter(mCalendarCoreData.getTotalWeeks(),
                        mCalendarCoreData.getDateMap(),
                        mCalendarCoreData.getFirstDate().toDate().getTime(),
                        mCalendarCoreData.getEndDate().toDate().getTime(), this);
        mPager.setAdapter(adapter);
        mPager.addOnPageChangeListener(this);

        if (calendarMode == CalendarMode.MONTHS) {
            int diffInDays = Days.daysBetween(mCalendarCoreData.getFirstDate().toLocalDate(),
                    mTodayDate.toLocalDate()).getDays();
            mTodayWeekPage = (diffInDays + (calendarCoreData.getDayOfWeek() - 1)) / DAYS_IN_WEEK;
        } else {
            mTodayWeekPage = (mPastLimit + (calendarCoreData.getDayOfWeek() - 1)) / DAYS_IN_WEEK;
        }
        if (isToday()) {
            selectToday();
        } else {
            selectProvidedDate(mTargetDate);
        }

    }

    private boolean isToday() {
        return mTargetDate == null || mTargetDate.equals(mTodayDate);
    }

    /**
     * @param pastLimit
     * @param futureLimit
     */
    public void setCalendarParameters(FragmentManager fragmentManager,
                                      int pastLimit, int futureLimit, CalendarMode calendarMode) {
        mFragmentManager = fragmentManager;
        if (pastLimit >= 0) {
            mPastLimit = Math.abs(pastLimit);
        }
        if (futureLimit >= 0) {
            mFutureLimit = Math.abs(futureLimit);
        }
        mSwipeCalendarPresenter.initCalendar(this, mPastLimit, mFutureLimit, calendarMode);
    }


    /**
     * @param pastLimit
     * @param futureLimit
     */
    public void setCalendarParameters(FragmentManager fragmentManager, Calendar targetDate,
                                      int pastLimit, int futureLimit, CalendarMode calendarMode) {
        mFragmentManager = fragmentManager;
        if (targetDate != null) {
            mTargetDate = CalendarTimeUtils.generateRequiredDateInstance(targetDate.get(Calendar.YEAR),
                    targetDate.get(Calendar.MONTH) + 1,
                    targetDate.get(Calendar.DAY_OF_MONTH));
        }
        if (pastLimit > 0) {
            mPastLimit = Math.abs(pastLimit);
        }
        if (futureLimit > 0) {
            mFutureLimit = Math.abs(futureLimit);
        }
        mSwipeCalendarPresenter.initCalendar(this, mPastLimit, mFutureLimit, calendarMode);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthUsed = 0;
        int heightUsed = 4;

        measureChildWithMargins(mTodayTextView, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(mTodayTextView);

        measureChildWithMargins(mMonthTextView, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(mMonthTextView);

        measureChildWithMargins(mTodayRightTextView, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mTodayTextView);

        measureChildWithMargins(mDaysLay, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mDaysLay);

        measureChildWithMargins(mPager, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mPager);

        measureChildWithMargins(mLine, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mLine);

        int heightSize = MeasureSpec.getSize(heightUsed);
        setMeasuredDimension(widthSize, heightSize);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft() + (16 - getPaddingLeft());

        int currentTop = 4;
        layoutView(mTodayTextView, paddingLeft, currentTop,
                mTodayTextView.getMeasuredWidth(), mTodayTextView.getMeasuredHeight());

        final int monthX = ((r - mMonthTextView.getMeasuredWidth()) / 2);
        layoutView(mMonthTextView, monthX, currentTop,
                mMonthTextView.getMeasuredWidth(), mMonthTextView.getMeasuredHeight());

        layoutView(mTodayRightTextView, getMeasuredWidth() - mTodayRightTextView.getMeasuredWidth() - paddingLeft,
                currentTop, mTodayRightTextView.getMeasuredWidth(), mTodayRightTextView.getMeasuredHeight());
        currentTop += getHeightWithMargins(mTodayTextView);

        mDaysLay.setLayoutParams(mLayoutParams);
        layoutView(mDaysLay, paddingLeft, currentTop, mDaysLay.getMeasuredWidth(), mDaysLay.getMeasuredHeight());
        currentTop += getHeightWithMargins(mDaysLay);

        layoutView(mPager, paddingLeft, currentTop, mPager.getMeasuredWidth(), mPager.getMeasuredHeight());
        currentTop += getHeightWithMargins(mPager);

        layoutView(mLine, 0, currentTop, mLine.getMeasuredWidth(), mLine.getMeasuredHeight());
    }

    private void layoutView(View view, int left, int top, int width, int height) {
        MarginLayoutParams margins = (MarginLayoutParams) view.getLayoutParams();
        final int leftWithMargins = left + margins.leftMargin;
        final int topWithMargins = top + margins.topMargin;
        view.layout(leftWithMargins, topWithMargins, leftWithMargins + width, topWithMargins + height);
    }

    private int getHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private int getMeasuredWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        todayTextDisplayLogic();
        if (mCurrentWeekPage != position) {
            mPreviousWeekPage = mCurrentWeekPage;
            mCurrentWeekPage = position;
            if (mScrollState == ViewPager.SCROLL_STATE_SETTLING) {
                LocalDateTime currentSelectedDate = getSelectedDate().getDate();
                if (mCurrentWeekPage > mPreviousWeekPage) {
                    currentSelectedDate = currentSelectedDate.plusDays(DAYS_IN_WEEK);
                } else {
                    currentSelectedDate = currentSelectedDate.minusDays(DAYS_IN_WEEK);
                }
                if (currentSelectedDate.toDate().getTime() < mCalendarCoreData.getFirstDate().toDate().getTime()) {
                    currentSelectedDate = mCalendarCoreData.getFirstDate();
                } else if (currentSelectedDate.toDate().getTime() > mCalendarCoreData.getEndDate().toDate().getTime()) {
                    currentSelectedDate = mCalendarCoreData.getEndDate();
                }
                setSelectedDate(currentSelectedDate);
                SwipeApp.getBus().post(mDateEventSelected);
            }
        }
        monthTextDisplayLogic(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
    }


    /**
     * Show today button if selected date is not today or currently displaying week is not today
     */

    private void todayTextDisplayLogic() {
        if (mPager.getCurrentItem() > mTodayWeekPage) {
            mTodayTextView.setVisibility(VISIBLE);
            mTodayRightTextView.setVisibility(GONE);
        } else if (mPager.getCurrentItem() < mTodayWeekPage) {
            mTodayTextView.setVisibility(GONE);
            mTodayRightTextView.setVisibility(VISIBLE);
        } else {
            mTodayTextView.setVisibility(GONE);
            mTodayRightTextView.setVisibility(GONE);
        }
    }


    private void monthTextDisplayLogic(int position) {
        LocalDateTime selectedDate = getSelectedDate().getDate();
        CalendarDate calendarDate = new CalendarDate();
        calendarDate.setDate(selectedDate);
        if (mCalendarCoreData.getDateMap().get(position).contains(calendarDate)) {
            //Display selected date's month, while selected date's week is on display
            mMonthTextView.setText(selectedDate.toString(MONTH_DISPLAY_FORMAT));
            mDateDialogFocusDate = getSelectedDate().getDate();
        } else if (mCurrentWeekPage > position) {
            //if user swiping from left to right show first index date's month
            rightToLeftSwipe(position);
        } else {
            //if user swiping from right to left show last index date's month
            leftToRightSwipe(position);
        }
    }

    private void leftToRightSwipe(int position) {
        LocalDateTime lhs = mCalendarCoreData.getDateMap().get(position).get(0).getDate();
        mDateDialogFocusDate = lhs;
        String currentMonth = mMonthTextView.getText().toString();

        if (!currentMonth.equals(lhs.toString(MONTH_FORMAT))) {
            mMonthTextView.setText(lhs.toString(MONTH_FORMAT));
        }
    }

    private void rightToLeftSwipe(int position) {
        LocalDateTime rhs = mCalendarCoreData.getDateMap().get(position).get(6).getDate();
        mDateDialogFocusDate = rhs;
        String currentMonth = mMonthTextView.getText().toString();

        if (!currentMonth.equals(rhs.toString(MONTH_FORMAT))) {
            mMonthTextView.setText(rhs.toString(MONTH_FORMAT));
        }
    }

    /**
     * Selects today's date
     * called on click of today
     */
    private void selectToday() {
        mCurrentWeekPage = mTodayWeekPage;
        mPreviousWeekPage = mCurrentWeekPage;
        setSelectedDate(mTodayDate);
        mPager.setCurrentItem(mTodayWeekPage, false);
        onPageSelected(mTodayWeekPage);
        SwipeApp.getBus().post(mDateEventSelected);
    }

    /**
     * Event when a date is clicked
     *
     * @param event
     */
    @Subscribe
    public void onEvent(OnDateSelectedEvent event) {
        mDateEventSelected = event;
        todayTextDisplayLogic();
        monthTextDisplayLogic(mCurrentWeekPage);
        if (mOnDateClickListener != null) {
            mOnDateClickListener.onDateClick(event.getDate().toDate());
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text_today || id == R.id.text_today_right) {
            selectToday();
        } else if (id == R.id.text_month) {
            handleMonthClick();
        }
    }

    public void handleMonthClick() {
        if (mDateDialogFocusDate != null) {
            showDateDialog(mDateDialogFocusDate);
        } else {
            showDateDialog(mTodayDate);
        }
    }

    private void showDateDialog(LocalDateTime dialogDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme, this,
                dialogDate.getYear(),
                dialogDate.getMonthOfYear() - 1,
                dialogDate.getDayOfMonth()
        );
        Calendar minDate = Calendar.getInstance();
        minDate.set(mCalendarCoreData.getFirstDate().getYear(),
                mCalendarCoreData.getFirstDate().getMonthOfYear() - 1,
                mCalendarCoreData.getFirstDate().getDayOfMonth());
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(mCalendarCoreData.getEndDate().getYear(),
                mCalendarCoreData.getEndDate().getMonthOfYear() - 1,
                mCalendarCoreData.getEndDate().getDayOfMonth());
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    public void selectProvidedDate(LocalDateTime date) {
        float diffInDays = Days.daysBetween(mTodayDate.toLocalDate(), date.toLocalDate()).getDays();
        int pagesToNavigate;
        int targetPagerItem;
        if (diffInDays > 0) {
            diffInDays += CalendarTimeUtils.getDayOfWeek(mTodayDate);
            pagesToNavigate = (int) Math.ceil(diffInDays / DAYS_IN_WEEK);
            targetPagerItem = mTodayWeekPage + (pagesToNavigate - 1);
        } else {
            diffInDays = Math.abs(diffInDays);
            diffInDays += ((DAYS_IN_WEEK - CalendarTimeUtils.getDayOfWeek(mTodayDate)) + 1);
            pagesToNavigate = (int) Math.ceil(diffInDays / DAYS_IN_WEEK);
            targetPagerItem = mTodayWeekPage - (pagesToNavigate - 1);
        }
        mCurrentWeekPage = targetPagerItem;
        mPreviousWeekPage = targetPagerItem;
        setSelectedDate(date);
        mPager.setCurrentItem(targetPagerItem, false);
        onPageSelected(targetPagerItem);
        SwipeApp.getBus().post(mDateEventSelected);
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

    @Override
    public void setSelectedDate(LocalDateTime targetDate) {
        mDateEventSelected = new OnDateSelectedEvent(targetDate);
    }

    @Override
    public OnDateSelectedEvent getSelectedDate() {
        return mDateEventSelected;
    }

    @Override
    public boolean isLeftToRightSwipe() {
        return mPreviousWeekPage < mCurrentWeekPage;
    }


    /**
     * Register a callback to be invoked when this date is clicked.
     */
    public void setOnDateClickListener(@Nullable OnDateClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }
        mOnDateClickListener = l;
    }

    /**
     * Register a callback to be invoked when date through month view is clicked.
     */
    public void setOnMonthClickListener(OnMonthClickListener m) {
        mOnMonthClickListener = m;
    }

    @Override
    public void onDateSet(DatePicker datePicker, final int year, final int monthOfYear, final int dayOfMonth) {
        selectProvidedDate(CalendarTimeUtils.generateRequiredDateInstance(year, monthOfYear + 1, dayOfMonth));
        if (mOnMonthClickListener != null) {
            mOnMonthClickListener.onMonthClick();
        }
    }


    /**
     * Interface definition for a callback to be invoked when a date_view is clicked.
     */
    public interface OnDateClickListener {
        /**
         * Called when a date has been clicked.
         *
         * @param date The date that was clicked.
         */
        void onDateClick(Date date);
    }

    /**
     * Interface definition for a callback to be invoked when a month view is clicked.
     */
    public interface OnMonthClickListener {
        //TODO Have to remove this seperate callback when event bus will be replaced by listeners
        void onMonthClick();
    }
}
