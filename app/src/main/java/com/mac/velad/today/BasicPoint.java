package com.mac.velad.today;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class BasicPoint extends RealmObject {

    @PrimaryKey
    private String UUID;
    private String name;
    private boolean enabled;
    private String description;
    private RealmList<WeekDay> weekDays;

    public static List<String> weekDaySymbols(BasicPoint basicPoint) {
        List<String> symbols = new ArrayList<>();
        for (WeekDay weekDay : basicPoint.getWeekDays()) {
            symbols.add(weekDay.getName());
        }
        return symbols;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RealmList<WeekDay> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(RealmList<WeekDay> weekDays) {
        this.weekDays = weekDays;
    }
}
