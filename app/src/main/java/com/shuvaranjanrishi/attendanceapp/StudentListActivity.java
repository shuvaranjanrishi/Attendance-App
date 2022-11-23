package com.shuvaranjanrishi.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private static final String TAG = StudentListActivity.class.getCanonicalName();

    private Activity mActivity;
    private TextView titleTv;
    private ImageButton studentAddBtn;
    private List<Student> studentList;
    private StudentAdapter adapter;
    private RecyclerView studentListRv;
    private MyDBHelper dbHelper;
    private MyCalender calender;
    //intent data
    private String className, subjectName;
    private long cid, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        initViews();

        initVariables();

        getIntentData();

        getStudentList();

        populateRecyclerView();

        initListener();

    }

    private void getStudentList() {
        Cursor cursor = dbHelper.getStudentList(cid);

        studentList.clear();
        while (cursor.moveToNext()) {
            long sid = cursor.getLong(0);
            String name = cursor.getString(2);
            int roll = cursor.getInt(3);
            Student student = new Student(sid, roll, name);
            studentList.add(student);
        }
        Log.d(TAG,"getStudentList: "+studentList.toString());
        cursor.close();
    }

    private void populateRecyclerView() {
        Log.d(TAG, "populateRecyclerView: " + studentList.toString());
        adapter = new StudentAdapter(mActivity, studentList);
        studentListRv.setAdapter(adapter);
        adapter.setListener(this::onItemClick);
    }

    private void onItemClick(int position) {

    }

    private void initListener() {
        studentAddBtn.setOnClickListener(view -> showStudentAddDialog());
    }

    private void showStudentAddDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener(this::addStudent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addStudent(String rollString, String name) {
        int roll = Integer.valueOf(rollString);
        long sid = dbHelper.addStudent(cid, roll, name);
        if (sid > 0) {
            studentList.add(new Student(sid, roll, name));
            adapter.notifyDataSetChanged();
            Log.d(TAG, "addStudent: "+studentList.toString());
            Toast.makeText(mActivity, "New Student Added...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mActivity, "Failed to Add...", Toast.LENGTH_LONG).show();
        }

    }

    private void initVariables() {
        mActivity = StudentListActivity.this;
        studentList = new ArrayList<>();
        dbHelper = new MyDBHelper(mActivity);
        calender = new MyCalender();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        className = intent.getStringExtra("ClassName");
        cid = intent.getLongExtra("CID", -1);
        titleTv.setText(className);
    }

    private void initViews() {
        titleTv = findViewById(R.id.titleTv);
        studentAddBtn = findViewById(R.id.studentAddBtn);
        studentListRv = findViewById(R.id.studentListRv);
    }
}