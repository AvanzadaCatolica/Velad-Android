package com.mac.velad.settings;

import android.content.Context;

import com.mac.velad.R;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ruenzuo on 25/03/16.
 */
public class Profile extends RealmObject {

    @PrimaryKey
    private String UUID;
    private String name;
    private String circle;
    private String group;

    public static Profile getProfile(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Profile> result = realm.where(Profile.class).findAll();
        return result.size() > 0 ? result.first() : null;
    }

    public static String profileInformation(Context context, Profile profile) {
        StringBuilder builder = new StringBuilder();
        if (profile.getName() != null && !profile.getName().isEmpty()) {
            builder.append(String.format(context.getString(R.string.profile_information_name_format), profile.getName()));
        }
        if (profile.getCircle() != null && !profile.getCircle().isEmpty()) {
            builder.append(String.format(context.getString(R.string.profile_information_circle_format), profile.getCircle()));
        }
        if (profile.getGroup() != null && !profile.getGroup().isEmpty()) {
            builder.append(String.format(context.getString(R.string.profile_information_group_format), profile.getGroup()));
        }
        builder.append("\n");

        return builder.toString();
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

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
