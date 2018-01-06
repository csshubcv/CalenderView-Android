package com.example.dell.library.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.example.dell.library.CalendarView;
import com.example.dell.library.R;
import com.example.dell.library.utils.CalendarProperties;
import com.example.dell.library.utils.DateUtils;
import com.example.dell.library.utils.DayColorsUtils;
import com.example.dell.library.utils.SelectedDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class is responsible for loading a one day cell.
 * <p>
 */

class CalendarDayAdapter extends ArrayAdapter<Date> {
    private CalendarPageAdapter mCalendarPageAdapter;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mMonth;
    private Calendar mToday = DateUtils.getCalendar();

    private CalendarProperties mCalendarProperties;

    String OutputData = "";

    CalendarDayAdapter(CalendarPageAdapter calendarPageAdapter, Context context, CalendarProperties calendarProperties,
                       ArrayList<Date> dates, int month) {
        super(context, calendarProperties.getItemLayoutResource(), dates);
        mCalendarPageAdapter = calendarPageAdapter;
        mContext = context;
        mCalendarProperties = calendarProperties;
        mMonth = month < 0 ? 11 : month;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = mLayoutInflater.inflate(mCalendarProperties.getItemLayoutResource(), parent, false);
        }

        TextView dayLabel = (TextView) view.findViewById(R.id.dayLabel);
        TextView textIcon = (TextView) view.findViewById(R.id.textIcon);

        Calendar day = new GregorianCalendar();
        day.setTime(getItem(position));


        // Loading an text of the event
        if (textIcon != null) {
            loadText(textIcon, day);
        }

        if (isSelectedDay(day)) {
            // Set view for all SelectedDays
            Stream.of(mCalendarPageAdapter.getSelectedDays())
                    .filter(selectedDay -> selectedDay.getCalendar().equals(day))
                    .findFirst().ifPresent(selectedDay -> selectedDay.setView(dayLabel));

            DayColorsUtils.setSelectedDayColors(mContext, dayLabel, mCalendarProperties.getSelectionColor());
        } else if (isCurrentMonthDay(day) && isActiveDay(day)) { // Setting current month day color
            DayColorsUtils.setCurrentMonthDayColors(mContext, day, mToday, dayLabel, mCalendarProperties.getTodayLabelColor());
        } else { // Setting not current month day color
            DayColorsUtils.setDayColors(dayLabel, ContextCompat.getColor(mContext,
                    R.color.nextMonthDayColor), Typeface.NORMAL, R.drawable.background_transparent);
        }

        dayLabel.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
        return view;
    }

    private void loadText(TextView textIcon, Calendar day) {
        if (mCalendarProperties.getEventDays() == null || mCalendarProperties.getCalendarType() != CalendarView.CLASSIC) {
            textIcon.setVisibility(View.GONE);
            return;
        }
        Stream.of(mCalendarProperties.getEventDays()).filter(eventDate ->
                eventDate.getCalendar().equals(day)).findFirst().executeIfPresent(eventDay -> {

            OutputData = eventDay.getCountText()+" "+eventDay.getTextView();
            textIcon.setText(OutputData);


            // If a day doesn't belong to current month then image is transparent
            if (!isCurrentMonthDay(day) || !isActiveDay(day)) {
                textIcon.setAlpha(0.2f);
            }

        });
    }

    private boolean isSelectedDay(Calendar day) {
        return mCalendarProperties.getCalendarType() != CalendarView.CLASSIC && day.get(Calendar.MONTH) == mMonth
                && mCalendarPageAdapter.getSelectedDays().contains(new SelectedDay(day));
    }

    private boolean isCurrentMonthDay(Calendar day) {
        return day.get(Calendar.MONTH) == mMonth;
    }

    private boolean isActiveDay(Calendar day) {
        return !((mCalendarProperties.getMinimumDate() != null && day.before(mCalendarProperties.getMinimumDate()))
                || (mCalendarProperties.getMaximumDate() != null && day.after(mCalendarProperties.getMaximumDate())));
    }

}
