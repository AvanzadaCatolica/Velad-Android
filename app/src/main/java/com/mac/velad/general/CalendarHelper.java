package com.mac.velad.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class CalendarHelper {

    public static final String SHARED_PREFERENCES_START_MONDAY = "SHARED_PREFERENCES_START_MONDAY";

    public static Calendar getInstance(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        boolean monday = sharedPreferences.getBoolean(SHARED_PREFERENCES_START_MONDAY, false);
        if (monday) {
            return Calendar.getInstance(new Locale("es"));
        } else {
            return Calendar.getInstance();
        }
    }

    private static Calendar blankCurrentCalendar(Context context) {
        Calendar calendar = getInstance(context);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar;
    }

    public static Date coldCurrentDate(Context context) {
        return blankCurrentCalendar(context).getTime();
    }

    public static Date coldCurrentStartWeekDate(Context context) {
        Calendar calendar = blankCurrentCalendar(context);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    public static Date coldCurrentEndWeekDate(Context context) {
        Calendar calendar = blankCurrentCalendar(context);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.add(Calendar.SECOND, -1);
        return  calendar.getTime();
    }

}
