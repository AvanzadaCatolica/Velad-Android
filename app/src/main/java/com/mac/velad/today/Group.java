package com.mac.velad.today;

import android.content.Context;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class Group extends RealmObject {

    @PrimaryKey
    private String UUID;
    private String name;
    private RealmList<BasicPoint> basicPoints;
    private Date createdAt;

    public static RealmResults<Group> getAll(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Group> result = realm.where(Group.class).findAll();
        result.sort("createdAt", Sort.DESCENDING);
        return result;
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

    public RealmList<BasicPoint> getBasicPoints() {
        return basicPoints;
    }

    public void setBasicPoints(RealmList<BasicPoint> basicPoints) {
        this.basicPoints = basicPoints;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
