package com.easyswipecalendar.model;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by sreedhu on 23/11/15.
 */
public class CalendarDate implements Serializable {
    private LocalDateTime mDate;

    public LocalDateTime getDate() {
        return mDate;
    }

    public void setDate(LocalDateTime date) {
        mDate = date;
    }


    @Override
    public boolean equals(Object obj) {
        // comparing only date from CalendarDate obj
        boolean retVal = false;
        if (obj instanceof CalendarDate) {
            LocalDateTime ptr = ((CalendarDate) obj).getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
            retVal = sdf.format(ptr.toDate()).equals(sdf.format(mDate.toDate()));
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (mDate != null ? mDate.hashCode() : 0);
        return hash;
    }
}
