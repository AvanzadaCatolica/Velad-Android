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
import java.util.Locale;

public class DateIntervalPickerFragment extends Fragment {

    private Date selectedStartDate;
    private Date selectedEndDate;
    private DateFormat dateFormatEnd = new DateFormat("dd 'de' MMMM");
    private DateFormat dateFormatStart = new DateFormat("dd 'al' ");

    private DateIntervalPickerFragmentListener listener;

    public DateIntervalPickerFragment() {
        // Required empty public constructor
    }

    public static DateIntervalPickerFragment newInstance() {
        DateIntervalPickerFragment fragment = new DateIntervalPickerFragment();
        return fragment;
    }

    public Date getSelectedStartDate() {
        return selectedStartDate;
    }

    public Date getSelectedEndDate() {
        return selectedEndDate;
    }

    public String getTitle() {
        return dateFormatStart.format(selectedStartDate) + dateFormatEnd.format(selectedEndDate);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSelectedDate();
    }

    private void setupSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        selectedStartDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.add(Calendar.SECOND, -1);
        selectedEndDate = calendar.getTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_interval_picker, container, false);
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
        listener.onPickDateInterval(this);
    }

    private void updateDisplayedText(View container) {
        TextView textViewDate = (TextView) container.findViewById(R.id.text_view_date);
        textViewDate.setText(getTitle());

        TextView textViewReturn = (TextView) container.findViewById(R.id.text_view_return);
        if (!isTodayInCurrentIntervalSelection()) {
            textViewReturn.setVisibility(View.VISIBLE);
            textViewReturn.setText(getString(R.string.date_interval_picker_return_weekly_title));
        } else {
            textViewReturn.setVisibility(View.GONE);
        }
    }

    private boolean isTodayInCurrentIntervalSelection() {
        Date today = new Date();
        return selectedStartDate.before(today) && selectedEndDate.after(today);
    }

    private void showNext() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedStartDate);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        selectedStartDate = calendar.getTime();

        calendar = Calendar.getInstance();
        calendar.setTime(selectedEndDate);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        selectedEndDate = calendar.getTime();

        updateDisplayedText(getView());
        listener.onPickDateInterval(this);
    }

    private void showPrevious() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedStartDate);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        selectedStartDate = calendar.getTime();

        calendar = Calendar.getInstance();
        calendar.setTime(selectedEndDate);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        selectedEndDate = calendar.getTime();

        updateDisplayedText(getView());
        listener.onPickDateInterval(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getParentFragment();
        if (fragment instanceof DateIntervalPickerFragmentListener) {
            listener = (DateIntervalPickerFragmentListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.onPickDateInterval(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface DateIntervalPickerFragmentListener {
        void onPickDateInterval(DateIntervalPickerFragment fragment);
    }
}
