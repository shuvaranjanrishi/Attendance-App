package com.shuvaranjanrishi.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class StudentListActivity extends AppCompatActivity {

    private static final String TAG = StudentListActivity.class.getCanonicalName();

    private Activity mActivity;
    private TextView titleTv;
    private ImageButton studentAddBtn;
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

        initListener();

    }

    private void initListener() {
        studentAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initVariables() {
        mActivity = StudentListActivity.this;
    }

    private void getIntentData() {
        Intent intent = getIntent();
        className = intent.getStringExtra("ClassName");
        titleTv.setText(className);
    }

    private void initViews() {
        titleTv = findViewById(R.id.titleTv);
        studentAddBtn = findViewById(R.id.studentAddBtn);
    }
}