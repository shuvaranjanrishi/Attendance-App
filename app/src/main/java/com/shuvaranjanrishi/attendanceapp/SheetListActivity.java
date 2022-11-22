package com.shuvaranjanrishi.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SheetListActivity extends AppCompatActivity {

    private static final String TAG = SheetListActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton backBtn, pickDateBtn, saveBtn, studentAddBtn;
    private TextView titleTv, dateTv;
    private RecyclerView studentsRv;
    private List<String> dateList;
    private StudentAdapter adapter;
    private MyDBHelper dbHelper;
    private MyCalender calender;
    private ListView sheetListView;
    private ArrayAdapter<String> arrayAdapter;
    //intent data
    private String className, subjectName;
    private long cid, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        initViews();

        initVariables();

        getIntentData();

        getDateList();

        populateListView();

        initListeners();
    }

    private void getIntentData() {
        cid = getIntent().getLongExtra("CID",-1);
    }

    private void getDateList() {
        Cursor cursor = dbHelper.getDistinctMonths(cid);
        while (cursor.moveToNext()){
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("STATUS_DATE"));
            dateList.add(date.substring(3));
        }
    }

    private void populateListView() {
        arrayAdapter = new ArrayAdapter<>(mActivity,R.layout.item_sheet_list,R.id.monthTv,dateList);
        sheetListView.setAdapter(arrayAdapter);
    }

    private void initViews() {
        titleTv = findViewById(R.id.titleTv);
        dateTv = findViewById(R.id.dateTv);
        backBtn = findViewById(R.id.backBtn);
        pickDateBtn = findViewById(R.id.pickDateBtn);
        saveBtn = findViewById(R.id.saveBtn);
        sheetListView = findViewById(R.id.sheetListView);
        studentAddBtn = findViewById(R.id.studentAddBtn);
        studentsRv = findViewById(R.id.studentsRv);
    }

    private void initVariables() {
        mActivity=SheetListActivity.this;
        dbHelper = new MyDBHelper(mActivity);
        dateList = new ArrayList();
    }

    private void initListeners() {
        backBtn.setOnClickListener(v->onBackPressed());

        sheetListView.setOnItemClickListener((parent,view,position,id)-> openSheetActivity(position));
    }

    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        long[] rollArray = getIntent().getLongArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        Intent intent = new Intent(mActivity,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("month",dateList.get(position));
        startActivity(intent);
    }


}