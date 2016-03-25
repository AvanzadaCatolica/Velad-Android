package com.mac.velad.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.dkharrat.nexusdialog.FormWithAppCompatActivity;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.mac.velad.R;

import java.util.UUID;

import io.realm.Realm;

/**
 * Created by ruenzuo on 25/03/16.
 */
public class ProfileActivity extends FormWithAppCompatActivity {

    private final static String PROFILE_NAME = "PROFILE_NAME";
    private final static String PROFILE_CIRCLE = "PROFILE_CIRCLE";
    private final static String PROFILE_GROUP = "PROFILE_GROUP";
    public final static String SHARED_PREFERENCES_PROFILE_LATER = "com.velad.SHARED_PREFERENCES_PROFILE_LATER";

    @Override
    protected void initForm() {
        FormSectionController section = new FormSectionController(this);
        section.addElement(new EditTextController(this, PROFILE_NAME, getString(R.string.profile_form_field_name), null, false));
        section.addElement(new EditTextController(this, PROFILE_CIRCLE, getString(R.string.profile_form_field_circle), null, false));
        section.addElement(new EditTextController(this, PROFILE_GROUP, getString(R.string.profile_form_field_group), null, false));

        getFormController().addSection(section);

        Profile profile = Profile.getProfile(this);
        if (profile != null) {
            getModel().setValue(PROFILE_NAME, profile.getName());
            getModel().setValue(PROFILE_CIRCLE, profile.getCircle());
            getModel().setValue(PROFILE_GROUP, profile.getGroup());
        }

        setTitle(getString(R.string.profile_activity_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean later = sharedPref.getBoolean(SHARED_PREFERENCES_PROFILE_LATER, true);

        MenuInflater inflater = getMenuInflater();
        if (later) {
            inflater.inflate(R.menu.profile_menu, menu);
        } else {
            inflater.inflate(R.menu.form_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save:
                saveProfile();
                return true;
            case R.id.menu_item_later:
                later();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveProfile() {
        String name = (String) getModel().getValue(PROFILE_NAME);
        String circle = (String) getModel().getValue(PROFILE_CIRCLE);
        String group = (String) getModel().getValue(PROFILE_GROUP);

        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();

        Profile profile = Profile.getProfile(this);
        if (profile == null) {
            profile = new Profile();
            profile.setUUID(UUID.randomUUID().toString());
        }
        profile.setName(name == null ? "" : name);
        profile.setCircle(circle == null ? "" : circle);
        profile.setGroup(group == null ? "" : group);

        realm.copyToRealm(profile);
        realm.commitTransaction();

        later();
    }

    private void later() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREFERENCES_PROFILE_LATER, false);
        editor.commit();
        finish();
    }

}
