package com.easyswipecalendar.utils;

import com.easyswipecalendar.model.CalendarDate;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Created by sreedhu on 23/11/15.
 */
public class CalendarTimeUtils {

    /**
     * Return today's date in the format MM/dd/yyyy 00:00:00
     *
     * @return
     */
    public static LocalDateTime getTime() {
        Calendar cal = Calendar.getInstance();
        return generateRequiredDateInstance(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    public static LocalDateTime generateRequiredDateInstance(int year, int month, int dayOfMonth) {
        LocalDateTime date = new LocalDateTime();
        date = date.withDate(year, month, dayOfMonth);
        date = date.withTime(0, 0, 0, 0);
        return date;
    }

    /**
     * Get Day of week, 1 for Monday, 2 for Tuesday,...,7 for Sunday
     *
     * @return
     */
    public static int getDayOfWeek(LocalDateTime date) {
        return date.getDayOfWeek();
    }

    /**
     * Minus no(count) of days from today
     *
     * @param count
     * @return
     */
    public static LocalDateTime minusDateFromToday(int count) {
        LocalDateTime date = getTime();
        date = date.minusDays(count);
        return date;
    }

    /**
     * Minus no(count) of months from today
     *
     * @param count
     * @return
     */
    public static LocalDateTime minusMonthFromToday(int count) {
        LocalDateTime date = getTime();
        date = date.minusMonths(count);
        return date;
    }


    /**
     * Get first date item to be displayed in calendar view
     *
     * @param index
     * @param todaysDate
     * @return
     */
    public static LocalDateTime getStartDate(int index, LocalDateTime todaysDate) {
        if (index == 0)
            return todaysDate;
        //index-1, because calendar day starts from monday
        todaysDate = todaysDate.minusDays(index - 1);
        return todaysDate;
    }

    /**
     * Get end/last date item to be displayed in calendar view
     *
     * @param totalDaysCount
     * @param todayDate
     * @return
     */
    public static LocalDateTime getEndDateByDays(int totalDaysCount, LocalDateTime todayDate) {
        return todayDate.plusDays(totalDaysCount);
    }

    /**
     * Get end/last date item to be displayed in calendar view
     *
     * @param totalMonthsCount
     * @param todayDate
     * @return
     */
    public static LocalDateTime getEndDateByMonths(int totalMonthsCount, LocalDateTime todayDate) {
        return todayDate.plusMonths(totalMonthsCount);
    }


    /**
     * Return total week will be displayed in our calendar
     * is directly proportional to the total no.of pages in week view pager
     *
     * @param index
     * @param totalDays
     * @return
     */
    public static int getTotalWeeks(int index, int totalDays) {
        int pages = (totalDays + index) / 7;
        if ((totalDays + index) % 7 > 0) {
            pages++;
        }
        return pages;
    }

    /**
     * Returns all dates in the format of total weeks with days
     *
     * @param startDate
     * @param totalPages
     * @return
     */
    public static HashMap<Integer, ArrayList<CalendarDate>> getAllDates(LocalDateTime startDate, int totalPages) {
        HashMap<Integer, ArrayList<CalendarDate>> map = new HashMap<>();
        LocalDateTime local = startDate;
        for (int i = 0; i < totalPages; i++) {
            ArrayList<CalendarDate> dateLst = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                CalendarDate date = new CalendarDate();
                date.setDate(local);
                dateLst.add(date);
                local = local.plusDays(1);
            }
            map.put(i, dateLst);
        }
        return map;
    }

}
