package com.easyswipecalendar.presenter;

import com.easyswipecalendar.model.CalendarCoreData;
import com.easyswipecalendar.model.CalendarMode;
import com.easyswipecalendar.utils.CalendarTimeUtils;
import com.easyswipecalendar.view.SwipeCalendarView;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

/**
 * Created by sreedhu on 8/3/16.
 */
public class SwipeCalendarPresenter {

    public void init(SwipeCalendarView swipeCalendarView) {
        swipeCalendarView.initViews();
    }

    public void initCalendar(SwipeCalendarView swipeCalendarView, int pastDaysCount, int futureDaysCount,
                             CalendarMode calendarMode) {
        swipeCalendarView.initCalendar(getCoreCalendarData(pastDaysCount, futureDaysCount, calendarMode), calendarMode);
    }

    public CalendarCoreData getCoreCalendarData(int pastLimit, int futureLimit, CalendarMode calendarMode) {
        CalendarCoreData coreData = new CalendarCoreData();
        int totalLimit = pastLimit + futureLimit;
        LocalDateTime firstDate = null;
        LocalDateTime endDate = null;
        switch (calendarMode) {
            case MONTHS:
                firstDate = CalendarTimeUtils.minusMonthFromToday(pastLimit);
                endDate = CalendarTimeUtils.getEndDateByMonths(totalLimit, firstDate);
                break;
            case DAYS:
                firstDate = CalendarTimeUtils.minusDateFromToday(pastLimit);
                endDate = CalendarTimeUtils.getEndDateByDays(totalLimit, firstDate);
                break;
        }
        int totalDays = Days.daysBetween(firstDate.toLocalDate(), endDate.toLocalDate()).getDays();
        int dayOfWeek = CalendarTimeUtils.getDayOfWeek(firstDate);
        LocalDateTime startDate = CalendarTimeUtils.getStartDate(dayOfWeek, firstDate);
        int totalWeeks = CalendarTimeUtils.getTotalWeeks(dayOfWeek, totalDays);
        coreData.setDateMap(CalendarTimeUtils.getAllDates(startDate, totalWeeks));
        coreData.setFirstDate(firstDate);
        coreData.setDayOfWeek(dayOfWeek);
        coreData.setStartDate(startDate);
        coreData.setEndDate(endDate);
        coreData.setTotalWeeks(totalWeeks);
        return coreData;
    }
}
