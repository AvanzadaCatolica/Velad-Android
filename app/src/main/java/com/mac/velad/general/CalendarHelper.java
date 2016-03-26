package com.mac.velad.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
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

}
