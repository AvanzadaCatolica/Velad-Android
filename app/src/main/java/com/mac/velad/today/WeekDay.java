package com.mac.velad.today;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class WeekDay extends RealmObject {

    @PrimaryKey
    private String UUID;
    private String name;

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
}
