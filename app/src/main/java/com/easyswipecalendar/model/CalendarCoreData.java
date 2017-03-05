package com.easyswipecalendar.model;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sreedhu on 8/3/16.
 */
public class CalendarCoreData {
    private LocalDateTime mFirstDate;
    private int mDayOfWeek;
    private LocalDateTime mStartDate;
    private LocalDateTime mEndDate;
    private int mTotalWeeks;
    private HashMap<Integer, ArrayList<CalendarDate>> mDateMap;

    public LocalDateTime getFirstDate() {
        return mFirstDate;
    }

    public void setFirstDate(LocalDateTime firstDate) {
        mFirstDate = firstDate;
    }

    public int getDayOfWeek() {
        return mDayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        mDayOfWeek = dayOfWeek;
    }

    public void setStartDate(LocalDateTime startDate) {
        mStartDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return mEndDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        mEndDate = endDate;
    }

    public int getTotalWeeks() {
        return mTotalWeeks;
    }

    public void setTotalWeeks(int totalWeeks) {
        mTotalWeeks = totalWeeks;
    }

    public HashMap<Integer, ArrayList<CalendarDate>> getDateMap() {
        return mDateMap;
    }

    public void setDateMap(HashMap<Integer, ArrayList<CalendarDate>> dateMap) {
        mDateMap = dateMap;
    }
}
