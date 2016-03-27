package com.mac.velad.general;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mac.velad.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends Fragment {

    private Date selectedDate;
    private DateFormat dateFormat = new DateFormat("dd 'de' MMMM");

    private DatePickerFragmentListener listener;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance() {
        DatePickerFragment fragment = new DatePickerFragment();
        return fragment;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public String getTitle() {
        return dateFormat.format(selectedDate);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSelectedDate();
    }

    private void setupSelectedDate() {
        Calendar calendar = CalendarHelper.getInstance(getContext());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        selectedDate = calendar.getTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        ImageButton buttonNext = (ImageButton) view.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNext();
            }
        });
        ImageButton buttonPrevious = (ImageButton) view.findViewById(R.id.button_previous);
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrevious();
            }
        });
        TextView textViewReturn = (TextView) view.findViewById(R.id.text_view_return);
        textViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToToday();
            }
        });
        updateDisplayedText(view);
    }

    private void returnToToday() {
        setupSelectedDate();
        updateDisplayedText(getView());
        listener.onPickDate(this);
    }

    private void updateDisplayedText(View container) {
        TextView textViewDate = (TextView) container.findViewById(R.id.text_view_date);
        textViewDate.setText(getTitle());

        TextView textViewReturn = (TextView) container.findViewById(R.id.text_view_return);
        if (!isCurrentSelectionToday()) {
            textViewReturn.setVisibility(View.VISIBLE);
            textViewReturn.setText(getString(R.string.date_picker_return_today_title));
        } else {
            textViewReturn.setVisibility(View.GONE);
        }
    }

    private boolean isCurrentSelectionToday() {
        Calendar calendarToday = CalendarHelper.getInstance(getContext());
        Calendar calendarSelected = CalendarHelper.getInstance(getContext());
        calendarSelected.setTime(selectedDate);
        return calendarToday.get(Calendar.DAY_OF_MONTH) == calendarSelected.get(Calendar.DAY_OF_MONTH) &&
                calendarToday.get(Calendar.MONTH) == calendarSelected.get(Calendar.MONTH) &&
                calendarToday.get(Calendar.YEAR) == calendarSelected.get(Calendar.YEAR);
    }

    private void showNext() {
        Calendar calendar = CalendarHelper.getInstance(getContext());
        calendar.setTime(selectedDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDate = calendar.getTime();


        updateDisplayedText(getView());
        listener.onPickDate(this);
    }

    private void showPrevious() {
        Calendar calendar = CalendarHelper.getInstance(getContext());
        calendar.setTime(selectedDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        selectedDate = calendar.getTime();


        updateDisplayedText(getView());
        listener.onPickDate(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getParentFragment();
        if (fragment instanceof DatePickerFragmentListener) {
            listener = (DatePickerFragmentListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement DatePickerFragmentListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.onPickDate(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface DatePickerFragmentListener {
        void onPickDate(DatePickerFragment fragment);
    }
}
