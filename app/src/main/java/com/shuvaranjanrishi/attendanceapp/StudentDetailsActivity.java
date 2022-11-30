package com.shuvaranjanrishi.attendanceapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StudentDetailsActivity extends AppCompatActivity {

    private static final String TAG = StudentDetailsActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton backBtn;
    private TextView titleTv, dateTv, rollTv, nameTv, totalPresentDayTv, totalAbsentDayTv, totalClassTv;
    private DonutProgress donutProgress;
    private MyDBHelper dbHelper;
    private CustomCalendar customCalendar;
    //intent data
    private String name, className;
    private long sid;
    private int roll, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        initViews();

        initVariables();

        getIntentData();

        setCalender();

        initListeners();
    }

    private void setCalender() {
        HashMap<Object, Property> descHashmap = new HashMap<>();
        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.default_view;
        defaultProperty.dateTextViewResource = R.id.textview;
        descHashmap.put("default", defaultProperty);

        Property currentProperty = new Property();
        currentProperty.layoutResource = R.layout.current_view;
        currentProperty.dateTextViewResource = R.id.textview;
        descHashmap.put("current", currentProperty);

        Property presentProperty = new Property();
        presentProperty.layoutResource = R.layout.present_view;
        presentProperty.dateTextViewResource = R.id.textview;
        descHashmap.put("present", presentProperty);

        Property absentProperty = new Property();
        absentProperty.layoutResource = R.layout.absent_view;
        absentProperty.dateTextViewResource = R.id.textview;
        descHashmap.put("absent", absentProperty);

        Property currentAndPresentProperty = new Property();
        currentAndPresentProperty.layoutResource = R.layout.current_and_present_view;
        currentAndPresentProperty.dateTextViewResource = R.id.textview;
        descHashmap.put("currentAndPresent", currentAndPresentProperty);

        Property currentAndAbsentProperty = new Property();
        currentAndAbsentProperty.layoutResource = R.layout.current_and_absent_view;
        currentAndAbsentProperty.dateTextViewResource = R.id.textview;
        descHashmap.put("currentAndAbsent", currentAndAbsentProperty);

        customCalendar.setMapDescToProp(descHashmap);

        HashMap<Integer, Object> dateHashmap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        int totalClass = 0, totalPresent = 0, totalAbsent = 0;
        String cDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "".length() == 1 ? "0" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
        String currentDate = cDay + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);

        for (int j = 1; j <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); j++) {
            String day = String.valueOf(j);
            if (day.length() == 1) day = "0" + day;
            String date = day + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
            String status = dbHelper.getStatus(sid, date);

            Log.d(TAG, "sid: " + sid + " status: " + status + " day: " + j + " date: " + date);
            if (status == null) dateHashmap.put(Calendar.DAY_OF_MONTH, "default");
            else {
                if (status.equals("P")) {
                    totalClass++;
                    totalPresent++;
                    dateHashmap.put(j, "present");
                }
                if (status.equals("A")) {
                    totalClass++;
                    totalAbsent++;
                    dateHashmap.put(j, "absent");
                }
                if (date.equals(currentDate) && status.equals("P")) {
                    dateHashmap.put(j, "currentAndPresent");
                }
                if (date.equals(currentDate) && status.equals("A")) {
                    dateHashmap.put(j, "currentAndAbsent");
                }
            }
        }

        customCalendar.setDate(calendar, dateHashmap);

        totalPresentDayTv.setText("Total Present: " + totalPresent);
        totalAbsentDayTv.setText("Total Absent: " + totalAbsent);
        totalClassTv.setText("Total Class: " + totalClass);
        if (totalClass > 0) {
            float totalPercentage = ((Float.valueOf(totalPresent) / Float.valueOf(totalClass)) * 100);
            donutProgress.setProgress(Float.parseFloat(new DecimalFormat("0.00").format(totalPercentage)));
        } else {
            donutProgress.setProgress(0);
        }
    }

    private Map<Integer, Object>[] loadStatusInCalender(Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        arr[0] = new HashMap<>();

        int totalClass = 0, totalPresent = 0, totalAbsent = 0;
        String cDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "".length() == 1 ? "0" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
        String currentDate = cDay + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);

        for (int j = 1; j <= newMonth.getActualMaximum(Calendar.DAY_OF_MONTH); j++) {
            String day = String.valueOf(j);
            if (day.length() == 1) day = "0" + day;
            String date = day + "-" + (newMonth.get(Calendar.MONTH) + 1) + "-" + newMonth.get(Calendar.YEAR);
            String status = dbHelper.getStatus(sid, date);
            Log.d(TAG, "sid: " + sid + " status: " + status + " day: " + j + " date: " + date);
            Log.d(TAG, "currentDAte: " + currentDate + " date: " + date);
            if (status == null) arr[0].put(Calendar.DAY_OF_MONTH, "default");
            else {
                if (status.equals("P")) {
                    totalClass++;
                    totalPresent++;
                    arr[0].put(j, "present");
                }
                if (status.equals("A")) {
                    totalClass++;
                    totalAbsent++;
                    arr[0].put(j, "absent");
                }
                if (date.equals(currentDate) && status.equals("P")) {
                    arr[0].put(j, "currentAndPresent");
                }
                if (date.equals(currentDate) && status.equals("A")) {
                    arr[0].put(j, "currentAndAbsent");
                }
            }
        }

        customCalendar.setDate(newMonth, arr[0]);

        totalPresentDayTv.setText("Total Present: " + totalPresent);
        totalAbsentDayTv.setText("Total Absent: " + totalAbsent);
        totalClassTv.setText("Total Class: " + totalClass);
        if (totalClass > 0) {
            float totalPercentage = ((Float.valueOf(totalPresent) / Float.valueOf(totalClass)) * 100);
            donutProgress.setProgress(Float.parseFloat(new DecimalFormat("0.00").format(totalPercentage)));
        } else {
            donutProgress.setProgress(0);
        }

        return arr;
    }

    private void getIntentData() {
        sid = getIntent().getLongExtra("SID", -1);
        roll = getIntent().getIntExtra("ROLL", -1);
        name = getIntent().getStringExtra("NAME");

        rollTv.setText("" + roll);
        nameTv.setText("" + name);
    }

    private void initViews() {
        titleTv = findViewById(R.id.titleTv);
        dateTv = findViewById(R.id.dateTv);
        backBtn = findViewById(R.id.backBtn);
        rollTv = findViewById(R.id.rollTv);
        nameTv = findViewById(R.id.nameTv);
        donutProgress = findViewById(R.id.donutProgress);
        totalPresentDayTv = findViewById(R.id.totalPresentDayTv);
        totalAbsentDayTv = findViewById(R.id.totalAbsentDayTv);
        totalClassTv = findViewById(R.id.totalClassTv);
        customCalendar = findViewById(R.id.customCalendar);
    }

    private void initVariables() {
        mActivity = StudentDetailsActivity.this;
        dbHelper = new MyDBHelper(mActivity);
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> onBackPressed());

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, (whichButton, newMonth) -> {

            Log.e(TAG, "PREVIOUS,whichButton : " + whichButton);
            Log.e(TAG, "newMonth : " + newMonth);

            return loadStatusInCalender(newMonth);
        });

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, (whichButton, newMonth) -> {

            Log.e(TAG, "NEXT,whichButton : " + whichButton);
            Log.e(TAG, "newMonth : " + newMonth);

            return loadStatusInCalender(newMonth);
        });
    }
}