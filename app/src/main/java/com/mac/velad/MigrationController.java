package com.mac.velad;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mac.velad.today.BasicPoint;
import com.mac.velad.today.Encouragement;
import com.mac.velad.today.groups.Group;
import com.mac.velad.today.WeekDay;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class MigrationController {

    private static final String SHARED_PREFERENCES_DATABASE_SEEDED = "SHARED_PREFERENCES_DATABASE_SEEDED";

    private static boolean isDatabaseSeeded(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_DATABASE_SEEDED, false);
    }

    public static void seedDatabase(Context context) {
        if (isDatabaseSeeded(context)) {
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREFERENCES_DATABASE_SEEDED, true);
        editor.apply();

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        Group group = realm.createObject(Group.class);
        group.setUUID(UUID.randomUUID().toString());
        group.setName(context.getString(R.string.default_group_name));
        group.setCreatedAt(new Date());
        realm.copyToRealm(group);

        List<String> names = Arrays.asList(context.getResources().getStringArray(R.array.basic_points_seed));
        DateFormatSymbols symbols = new DateFormatSymbols(new Locale("es"));
        for (String name : names) {
            BasicPoint basicPoint = realm.createObject(BasicPoint.class);
            basicPoint.setUUID(UUID.randomUUID().toString());
            basicPoint.setName(name);
            basicPoint.setDescription("");
            basicPoint.setEnabled(true);
            for (String symbol : symbols.getWeekdays()) {
                if (symbol.isEmpty()) {
                    continue;
                }
                WeekDay weekDay = new WeekDay();
                weekDay.setUUID(UUID.randomUUID().toString());
                weekDay.setName(symbol);
                basicPoint.getWeekDays().add(weekDay);
            }
            realm.copyToRealm(basicPoint);
            group.getBasicPoints().add(basicPoint);
        }

        Encouragement encouragement = new Encouragement();
        encouragement.setUUID(UUID.randomUUID().toString());
        encouragement.setEnabled(true);
        encouragement.setPercentage(50);
        realm.copyToRealm(encouragement);

        realm.commitTransaction();
    }


}
