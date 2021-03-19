package com.nateprat.university.mobileplatformdevelopment.core.listeners;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.chip.Chip;
import com.nateprat.university.mobileplatformdevelopment.core.concurrency.ThreadPools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerChip implements View.OnFocusChangeListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Chip mChip;
    private Calendar mCalendar;
    private SimpleDateFormat mFormat;
    private DatePickerChip mChipBefore;
    private DatePickerChip mChipAfter;
    private Runnable onDateSetRunnable;

    public DatePickerChip(Chip mChip, String format, Runnable onDateSetRunnable) {
        this.mChip = mChip;
        mChip.setOnFocusChangeListener(this);
        mChip.setOnClickListener(this);
        mFormat = new SimpleDateFormat(format, Locale.getDefault());
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
            mCalendar.setTime(new Date());
        }
        this.onDateSetRunnable = onDateSetRunnable;
        this.mChip.setText(mFormat.format(mCalendar.getTime()));
    }

    public void setDate(Date date) {
        this.mCalendar.setTime(date);
        this.mChip.setText(mFormat.format(date));
    }

    public void setmChipBefore(DatePickerChip mChipBefore) {
        this.mChipBefore = mChipBefore;
    }

    public void setmChipAfter(DatePickerChip mChipAfter) {
        this.mChipAfter = mChipAfter;
    }

    public Chip getmChip() {
        return mChip;
    }

    public Calendar getmCalendar() {
        return mCalendar;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        this.mChip.setText(mFormat.format(mCalendar.getTime()));

        if (onDateSetRunnable != null) ThreadPools.getInstance().submitTask(onDateSetRunnable);
    }

    @Override
    public void onClick(View v) {
        showPicker(v);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showPicker(v);
        }
    }

    private void showPicker(View view) {
        if (mCalendar == null)
            mCalendar = Calendar.getInstance();

        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(view.getContext(), this, year, month, day);
        if (mChipBefore != null) {
            dpd.getDatePicker().setMinDate(mChipBefore.getmCalendar().getTimeInMillis() - 1000L);
        }
        if (mChipAfter != null) {
            dpd.getDatePicker().setMaxDate(mChipAfter.getmCalendar().getTimeInMillis() + 1000L);
        }
        dpd.show();
    }

}
