package com.easyswipecalendar.events;

import org.joda.time.LocalDateTime;

/**
 * Created by sreedhu on 24/11/15.
 */
public class OnDateSelectedEvent {
    private LocalDateTime mDate;

    public OnDateSelectedEvent(LocalDateTime date) {
        this.mDate = date;
    }

    public LocalDateTime getDate() {
        return mDate;
    }

}
