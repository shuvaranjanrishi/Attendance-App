package com.shuvaranjanrishi.attendanceapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentDetailsActivity extends AppCompatActivity {

    private static final String TAG = StudentDetailsActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton backBtn;
    private TextView titleTv, dateTv;
    private List<String> dateList;
    private MyDBHelper dbHelper;
    private MyCalender calender;
    //intent data
    private String className, subjectName;
    private long cid, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        initViews();

        initVariables();

        getIntentData();

        getDateList();

        populateListView();

        initListeners();
    }

    private void getIntentData() {
        cid = getIntent().getLongExtra("CID", -1);
    }

    private void getDateList() {
        Cursor cursor = dbHelper.getDistinctMonths(cid);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("STATUS_DATE"));
            dateList.add(date.substring(3));
        }
    }

    private void populateListView() {
    }

    private void initViews() {
        titleTv = findViewById(R.id.titleTv);
        dateTv = findViewById(R.id.dateTv);
        backBtn = findViewById(R.id.backBtn);
    }

    private void initVariables() {
        mActivity = StudentDetailsActivity.this;
        dbHelper = new MyDBHelper(mActivity);
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