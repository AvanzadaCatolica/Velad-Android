package com.mac.velad.diary;

import android.content.Context;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 20/03/16.
 */
public class Confession extends RealmObject {

    @PrimaryKey
    private String UUID;

    private Date date;

    public static Confession getLastConfession(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Confession> result = realm.where(Confession.class).findAll();
        result.sort("date", Sort.DESCENDING);
        return result.first();
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
