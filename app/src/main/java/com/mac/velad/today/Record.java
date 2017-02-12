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
public class Record extends RealmObject {

    @PrimaryKey
    private String UUID;
    private Date date;
    private BasicPoint basicPoint;
    private String notes;

    public static Record getRecord(BasicPoint basicPoint, Date date) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Record> result = realm.where(Record.class).equalTo("date", date).equalTo("basicPoint.UUID", basicPoint.getUUID()).findAll();
        return result.size() > 0 ? result.first() : null;
    }

    public static RealmResults<Record> getRecords(BasicPoint basicPoint, Date startDate, Date endDate) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Record> result = realm.where(Record.class).between("date", startDate, endDate).equalTo("basicPoint.UUID", basicPoint.getUUID()).findAll();
        return result;
    }

    public static int getRecordsCount(Date date) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Record> result = realm.where(Record.class).equalTo("date", date).findAll();
        return result.size();
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

    public BasicPoint getBasicPoint() {
        return basicPoint;
    }

    public void setBasicPoint(BasicPoint basicPoint) {
        this.basicPoint = basicPoint;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
