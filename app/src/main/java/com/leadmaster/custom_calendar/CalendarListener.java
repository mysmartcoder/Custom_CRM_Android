package com.leadmaster.custom_calendar;

import java.util.Date;

/**
 * Created by Nilanchala on 9/14/15.
 */
public interface CalendarListener {
    void onDateSelected(Date date);

    void onMonthChanged(Date time);
}
