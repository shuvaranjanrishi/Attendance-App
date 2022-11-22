package com.shuvaranjanrishi.attendanceapp;

/*
    Created by Shuva Ranjan Rishi on 11/19/2022
*/

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class MyCalender extends DialogFragment {

    private final Calendar calendar = Calendar.getInstance();
    private OnClickListener listener;

    public interface OnClickListener{
        void onOkClickListener(int year,int month,int day);
    }

    public void setListener(OnClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),((view,year,month,day)->{
            if (listener!=null){
                listener.onOkClickListener(year,month,day);
            }
        }),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setDate(int year,int month,int day){
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
    }

    public String getDate(){
        return DateFormat.format("dd-MM-yyyy",calendar).toString();
    }
}
