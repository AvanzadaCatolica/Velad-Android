package com.mac.velad.diary;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.dkharrat.nexusdialog.FormWithAppCompatActivity;
import com.github.dkharrat.nexusdialog.controllers.DatePickerController;
import com.github.dkharrat.nexusdialog.controllers.EditTextController;
import com.github.dkharrat.nexusdialog.controllers.FormSectionController;
import com.github.dkharrat.nexusdialog.controllers.SelectionController;
import com.mac.velad.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;

public class NoteActivity extends FormWithAppCompatActivity {

    private final static String NOTE_TEXT = "NOTE_TEXT";
    private final static String NOTE_DATE= "NOTE_DATE";
    private final static String NOTE_STATE = "NOTE_STATE";

    @Override
    protected void initForm() {
        FormSectionController section = new FormSectionController(this);
        section.addElement(new EditTextController(this, NOTE_TEXT, getString(R.string.note_form_field_note), null, true));
        section.addElement(new DatePickerController(this, NOTE_DATE, getString(R.string.note_form_field_date), true, new SimpleDateFormat("dd MMM yyyy")));
        section.addElement(new SelectionController(this, NOTE_STATE, getString(R.string.note_form_field_type), true, null, Arrays.asList(NoteState.states()), Arrays.asList(NoteState.values())));
        getFormController().addSection(section);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Note note = Note.getNote(this, bundle.getString(DiaryFragment.NOTE_UUID_EXTRA));
            getModel().setValue(NOTE_TEXT, note.getText());
            getModel().setValue(NOTE_DATE, note.getDate());
            getModel().setValue(NOTE_STATE, NoteState.fromPrint(note.getState()));
        } else {
            getModel().setValue(NOTE_DATE, new Date());
            getModel().setValue(NOTE_STATE, NoteState.REGULAR);
        }

        setTitle(getString(R.string.note_activity_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String text = (String) getModel().getValue(NOTE_TEXT);
        Date date = (Date) getModel().getValue(NOTE_DATE);
        NoteState state = (NoteState) getModel().getValue(NOTE_STATE);

        if (text == null || text.isEmpty()) {
            displayErrorMessage(getString(R.string.error_message_missing_field));
            return;
        }
        if (state == null) {
            displayErrorMessage(getString(R.string.error_message_missing_field));
            return;
        }

        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();

        Note note;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
           note = Note.getNote(this, bundle.getString(DiaryFragment.NOTE_UUID_EXTRA));
        } else {
            note = new Note();
        }

        note.setUUID(UUID.randomUUID().toString());
        note.setText(text);
        note.setDate(date);
        note.setState(state.toString());
        realm.copyToRealm(note);
        realm.commitTransaction();

        setResult(Activity.RESULT_OK);
        finish();
    }

    private void displayErrorMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .show();
    }

}
