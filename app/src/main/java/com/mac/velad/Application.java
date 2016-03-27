package com.mac.velad;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MigrationController.seedDatabase(getApplicationContext());
    }
}
