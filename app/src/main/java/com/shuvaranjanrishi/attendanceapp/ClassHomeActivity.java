package com.shuvaranjanrishi.attendanceapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public class ClassHomeActivity extends AppCompatActivity {

    private static final String TAG = ClassHomeActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton backBtn;
    private TextView titleTv;
    private CardView attendanceSheetCv, studentsCv, attendanceCv;
    private MyDBHelper dbHelper;
    private List<Student> studentList;
    //intent data
    private String className, subjectName;
    private long cid, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_home);

        initViews();

        initVariables();

        getIntentData();

        getStudentList();

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

    private void initListener() {
        backBtn.setOnClickListener(v -> onBackPressed());
        attendanceSheetCv.setOnClickListener(v -> goToSheetListActivity());
        studentsCv.setOnClickListener(v -> gotoStudentListActivity());
        attendanceCv.setOnClickListener(v -> gotoTakeAttendanceActivity());
    }

    private void goToSheetListActivity() {
        long[] idArray, rollArray;
        String[] nameArray;
        idArray = new long[studentList.size()];
        rollArray = new long[studentList.size()];
        nameArray = new String[studentList.size()];

        for (int i = 0; i < studentList.size(); i++) {
            idArray[i] = studentList.get(i).getSid();
            rollArray[i] = studentList.get(i).getRoll();
            nameArray[i] = studentList.get(i).getName();
        }
        Intent intent = new Intent(mActivity, SheetListActivity.class);
        intent.putExtra("CID", cid);
        intent.putExtra("idArray", idArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("nameArray", nameArray);
        startActivity(intent);
    }

    private void gotoStudentListActivity() {
        Intent intent = new Intent(mActivity, StudentListActivity.class);
        intent.putExtra("ClassName", className);
        intent.putExtra("CID", cid);
        startActivity(intent);
    }

    private void gotoTakeAttendanceActivity() {
        Intent intent = new Intent(mActivity, TakeAttendanceActivity.class);
        intent.putExtra("ClassName", className);
        intent.putExtra("SubjectName", subjectName);
        intent.putExtra("CID", cid);
        intent.putExtra("Position", position);
        startActivity(intent);
    }

    private void initVariables() {
        mActivity = ClassHomeActivity.this;
        studentList = new ArrayList<>();
        dbHelper = new MyDBHelper(mActivity);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        className = intent.getStringExtra("ClassName");
        subjectName = intent.getStringExtra("SubjectName");
        cid = intent.getLongExtra("CID", -1);
        position = intent.getIntExtra("Position", -1);
        titleTv.setText(className);
    }

    private void initViews() {
        backBtn = findViewById(R.id.backBtn);
        titleTv = findViewById(R.id.titleTv);
        attendanceSheetCv = findViewById(R.id.attendanceSheetCv);
        studentsCv = findViewById(R.id.studentsCv);
        attendanceCv = findViewById(R.id.attendanceCv);
    }
}