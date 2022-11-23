package com.shuvaranjanrishi.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClassHomeActivity extends AppCompatActivity {

    private static final String TAG = ClassHomeActivity.class.getCanonicalName();

    private Activity mActivity;
    private TextView titleTv;
    private CardView studentsCv, attendanceCv;
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

        initListener();
    }

    private void initListener() {
        studentsCv.setOnClickListener(v -> gotoStudentListActivity());
        attendanceCv.setOnClickListener(v -> gotoTakeAttendanceActivity());
    }

    private void gotoStudentListActivity() {
        Intent intent = new Intent(mActivity, StudentListActivity.class);
        intent.putExtra("ClassName", className);
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
        titleTv = findViewById(R.id.titleTv);
        studentsCv = findViewById(R.id.studentsCv);
        attendanceCv = findViewById(R.id.attendanceCv);
    }
}