package com.shuvaranjanrishi.attendanceapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private static final String TAG = StudentListActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton backBtn;
    private TextView titleTv, totalStudentTv;
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
        Log.d(TAG, "getStudentList: " + studentList.toString());
        totalStudentTv.setText("Total: " + studentList.size());
        cursor.close();
    }

    private void populateRecyclerView() {
        Log.d(TAG, "populateRecyclerView: " + studentList.toString());
        Toast.makeText(mActivity, "" + studentList.size(), Toast.LENGTH_SHORT).show();
        adapter = new StudentAdapter(mActivity, studentList);
        studentListRv.setAdapter(adapter);
        adapter.setListener(this::onItemClick);
    }

    private void onItemClick(int position) {
        Intent intent = new Intent(mActivity,StudentDetailsActivity.class);
        intent.putExtra("ID",studentList.get(position).getSid());
        intent.putExtra("CID",studentList.get(position).getSid());
        startActivity(intent);
    }

    private void initListener() {
        backBtn.setOnClickListener(v -> onBackPressed());
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
            totalStudentTv.setText("Total: " + studentList.size());
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
        backBtn = findViewById(R.id.backBtn);
        titleTv = findViewById(R.id.titleTv);
        totalStudentTv = findViewById(R.id.totalStudentTv);
        studentAddBtn = findViewById(R.id.studentAddBtn);
        studentListRv = findViewById(R.id.studentListRv);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showStudentUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showStudentUpdateDialog(int position) {
        MyDialog dialog = new MyDialog(String.valueOf(studentList.get(position).getRoll()), studentList.get(position).getName());
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll, name) -> updateStudent(position, name));
    }

    private void updateStudent(int position, String name) {
        int rowId = dbHelper.updateStudent(studentList.get(position).getSid(), name);
        if (rowId > 0) {
            studentList.get(position).setName(name);
            adapter.notifyItemChanged(position);
            Toast.makeText(mActivity, "Student Successfully Updated...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mActivity, "Student Not Updated...", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteStudent(int position) {
        int rowId = dbHelper.deleteStudent(studentList.get(position).getSid());
        if (rowId > 0) {
            studentList.remove(position);
            totalStudentTv.setText("Total: " + studentList.size());
            adapter.notifyDataSetChanged();
            Toast.makeText(mActivity, "Student Successfully Deleted...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mActivity, "Student Not Deleted...", Toast.LENGTH_LONG).show();
        }
    }
}