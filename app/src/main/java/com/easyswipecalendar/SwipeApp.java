package com.easyswipecalendar;

import com.squareup.otto.Bus;

public class SwipeApp {

    private static Bus sBusInstance = new Bus();

    public static Bus getBus() {
        return sBusInstance;
    }
}
