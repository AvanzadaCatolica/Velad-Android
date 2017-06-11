package com.mac.velad.today;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class Encouragement extends RealmObject {

    @PrimaryKey
    private String UUID;
    private boolean enabled;
    private int percentage;

    public static Encouragement getEncouragement() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Encouragement> result = realm.where(Encouragement.class).findAll();
        return result.first();
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

}
