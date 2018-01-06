package com.example.dell.library;



import com.example.dell.library.utils.DateUtils;

import java.util.Calendar;

/**
 * This class represents an event of a day. An instance of this class is returned when user click
 * a day cell. This class can be overridden to make calendar more functional. A list of instances of
 * this class can be passed to CalendarView object using setEvents() method.
 * <p>
 */

public class EventDay {
    private Calendar mDay;
    private String mtextView;
    private String mcountText;

    /**
     * @param day Calendar object which represents a date of the event
     */
    public EventDay(Calendar day) {
        mDay = day;
    }

    /**
     * @param day Calendar object which represents a date of the event
     */
    public EventDay(Calendar day, String textView, int countText) {
        DateUtils.setMidnight(day);
        mDay = day;
        mtextView = textView;
        if(textView.equals("completed"))
        {
            mcountText="";
        }else
        {
            mcountText=countText+"";
        }

    }

    /**
     * @return An image resource which will be displayed in the day row
     */
    public String getTextView() {
        return mtextView;
    }

    /**
     * @return Calendar object which represents a date of current event
     */
    public Calendar getCalendar() {
        return mDay;
    }

    public String getCountText(){return mcountText;}

}
