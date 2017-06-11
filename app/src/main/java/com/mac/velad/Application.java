package com.mac.velad;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        MigrationController.seedDatabase(getApplicationContext());
    }
}
