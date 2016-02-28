package com.mac.velad.diary;

import android.content.Context;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 13/02/16.
 */
public class Note extends RealmObject {

    @PrimaryKey
    private String UUID;

    private String text;
    private String state;
    private Date date;

    public static RealmResults<Note> getAll(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Note> result = realm.where(Note.class).findAll();
        result.sort("date", Sort.DESCENDING);
        return result;
    }

    public static Note getNote(Context context, String UUID) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Note> result = realm.where(Note.class).equalTo("UUID", UUID).findAll();
        return result.first();
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
