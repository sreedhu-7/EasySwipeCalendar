package com.easyswipecalendar.view;

import com.easyswipecalendar.events.OnDateSelectedEvent;

import org.joda.time.LocalDateTime;

/**
 * Created by nishantshah on 23/07/16.
 */
public interface SwipeCalendarViewCallback {

    OnDateSelectedEvent getSelectedDate();

    boolean isLeftToRightSwipe();

    void setSelectedDate(LocalDateTime selectedDate);
}
