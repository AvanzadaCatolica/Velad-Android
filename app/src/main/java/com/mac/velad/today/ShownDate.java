package com.mac.velad.today;

import android.content.Context;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 27/03/16.
 */
public class ShownDate extends RealmObject {

    @PrimaryKey
    private String UUID;
    private Date date;

    public static ShownDate getShowDate(Context context, Date date) {
        Realm realm = Realm.getInstance(context);
        RealmResults<ShownDate> result = realm.where(ShownDate.class).equalTo("date", date).findAll();
        return result.size() > 0 ? result.first() : null;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
