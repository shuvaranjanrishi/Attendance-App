package com.shuvaranjanrishi.attendanceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StudentDetailsActivity extends AppCompatActivity {

    private static final String TAG = StudentDetailsActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton backBtn;
    private TextView titleTv, dateTv,rollTv,nameTv, totalPresentDayTv, totalAbsentDayTv, totalClassTv;
    private DonutProgress donutProgress;
    private List<String> dateList;
    private MyDBHelper dbHelper;
    private MyCalender myCalender;
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

//        getDateList();

        setCalender();

        populateListView();

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

        customCalendar.setMapDescToProp(descHashmap);
//        customCalendar.setOnNavigationButtonClickedListener(1, new OnNavigationButtonClickedListener() {
//            @Override
//            public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
//                return new Map[0];
//            }
//        });

        HashMap<Integer, Object> dateHashmap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        int totalClass = 0, totalPresent = 0, totalAbsent = 0;

        for (int j = 1; j <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); j++) {
            String day = String.valueOf(j);
            if (day.length() == 1) day = "0" + day;
//            String date = day + "-" + month;
//            calender
            String date = day + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
            String status = dbHelper.getStatus(sid, date);

            Log.d(TAG, "sid: " + sid + " status: " + status + " day: " + j + " date: " + date);
            if (status == null) dateHashmap.put(Calendar.DAY_OF_MONTH, "default");
            else if (status.equals("P")) {
                totalClass++;
                totalPresent++;
                dateHashmap.put(j, "present");
            } else {
                totalClass++;
                totalAbsent++;
                dateHashmap.put(j, "absent");
            }
        }

        dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");
//        dateHashmap.put(1,"default");
//        dateHashmap.put(2,"present");
//        dateHashmap.put(3,"absent");
        customCalendar.setDate(calendar, dateHashmap);

        totalPresentDayTv.setText("Total Present: " + totalPresent);
        totalAbsentDayTv.setText("Total Absent: " + totalAbsent);
        totalClassTv.setText("Total Class: " + totalClass);
        float totalPercentage = ((Float.valueOf(totalPresent) / Float.valueOf(totalClass)) * 100);
        donutProgress.setProgress(Float.parseFloat(new DecimalFormat("0.00").format(totalPercentage)));

    }

    private void getIntentData() {
        sid = getIntent().getLongExtra("SID", -1);
        roll = getIntent().getIntExtra("ROLL", -1);
        name = getIntent().getStringExtra("NAME");

        rollTv.setText(""+roll);
        nameTv.setText(""+name);
    }

//    private void getDateList() {
//        Cursor cursor = dbHelper.getDistinctMonths(cid);
//        while (cursor.moveToNext()) {
//            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("STATUS_DATE"));
//            dateList.add(date.substring(3));
//        }
//    }

    private void populateListView() {
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
        myCalender = new MyCalender();
        dateList = new ArrayList();
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        long[] rollArray = getIntent().getLongArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        Intent intent = new Intent(mActivity, SheetActivity.class);
        intent.putExtra("idArray", idArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("nameArray", nameArray);
        intent.putExtra("month", dateList.get(position));
        startActivity(intent);
    }
}