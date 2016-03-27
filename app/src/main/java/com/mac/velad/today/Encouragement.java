package com.mac.velad.today;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class Encouragement extends RealmObject {

    @PrimaryKey
    private String UUID;
    private boolean enabled;
    private float percentage;

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

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
