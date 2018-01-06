package com.example.dell.calenderexample.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.dell.calenderexample.R;
import com.example.dell.library.CalendarView;
import com.example.dell.library.EventDay;
import com.example.dell.library.networking.JsonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Mateusz Kornakiewicz on 26.05.2017.
 */

public class CalendarFragment extends Fragment {
    private View rootView;
    private JsonData datastr = new JsonData();
    private CalendarView calendarView;
    List<EventDay> events = new ArrayList<>();

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calendar_activity, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(R.string.title);
        calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
        calData();
        return rootView;
    }

    private void calData() {
        try {
            JSONObject jsonObj = new JSONObject(datastr.getJsonString());
            JSONArray jsondata = jsonObj.optJSONArray("row");

            for (int i = 0; i < jsondata.length(); i++) {
                JSONObject rowsdata = jsondata.getJSONObject(i);

                JSONArray childrens = rowsdata.getJSONArray("event");
                for (int j = 0; j < childrens.length(); j++) {
                    JSONObject scheduledata = childrens.getJSONObject(j);
                    String date = scheduledata.getString("date");
                    String status = scheduledata.getString("status");//Status from Json

                    Date dates = new Date();  // to get the date
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate = df1.format(dates.getTime());
                    Date todaydate=df1.parse(formattedDate);
                    String convertedDate =df1.format(df.parse(date));
                    Date strDate = df1.parse(convertedDate); //Json date

                    Calendar cal = toCalendar(strDate);
                    if (strDate.before(todaydate)) {
                        int count=0;

                       /* //Adding status from Json to EventDay
                        events.add(new EventDay(cal,status,count)); */

                        //Here status will be added based on dates.
                        String statusb="completed";
                        events.add(new EventDay(cal,statusb,count));
                        calendarView.setEvents(events);
                    }
                    else  if(todaydate.equals(strDate))
                    {
                        JSONArray child2 = scheduledata.getJSONArray("schedule");
                        int count=0;

                        /* //Adding status from Json to EventDay
                        events.add(new EventDay(cal,status,count)); */

                        //Here status will be added based on dates.
                        String statust="open";
                        for (int k = 0; k < child2.length(); k++) {
                            count=count+1;
                        }

                        events.add(new EventDay(cal, statust,count));
                        calendarView.setEvents(events);
                    }
                    else if(strDate.after(todaydate)) {
                        JSONArray child2 = scheduledata.getJSONArray("schedule");
                        int count=0;

                         /* //Adding status from Json to EventDay
                        events.add(new EventDay(cal,status,count)); */

                        //Here status will be added based on dates.
                        String statusa="occurance";
                        for (int k = 0; k < child2.length(); k++) {
                            count=count+1;
                        }

                        events.add(new EventDay(cal,statusa,count));
                        calendarView.setEvents(events);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home)
        {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
