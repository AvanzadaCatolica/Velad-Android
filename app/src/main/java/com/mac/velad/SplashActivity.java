package com.mac.velad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.mac.velad.settings.ProfileActivity;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class SplashActivity extends AppCompatActivity {

    public static final String FIRST_LAUNCH = "FIRST_LAUNCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean later = sharedPreferences.getBoolean(ProfileActivity.SHARED_PREFERENCES_PROFILE_LATER, true);
        Intent intent;
        if (later) {
            intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(FIRST_LAUNCH, true);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
