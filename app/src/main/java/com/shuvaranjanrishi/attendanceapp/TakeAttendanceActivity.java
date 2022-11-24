package com.shuvaranjanrishi.attendanceapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TakeAttendanceActivity extends AppCompatActivity {

    private static final String TAG = TakeAttendanceActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton backBtn, saveBtn, showAttendanceBtn, moreBtn;
    private TextView titleTv, dateTv;
    private RecyclerView studentsRv;
    private List<Student> studentList;
    private AttendanceAdapter adapter;
    private MyDBHelper dbHelper;
    private MyCalender calender;
    //intent data
    private String className, subjectName;
    private long cid, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        initViews();

        initVariables();

        getIntentData();

        getStudentList();

        populateRecyclerView();

        loadStatus();

        initListeners();
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

    private void getIntentData() {
        Intent intent = getIntent();
        className = intent.getStringExtra("ClassName");
        subjectName = intent.getStringExtra("SubjectName");
        cid = intent.getLongExtra("CID", -1);
        position = intent.getIntExtra("Position", -1);

        titleTv.setText(className);
        dateTv.setText(subjectName + " | " + calender.getDate());
    }

    private void initListeners() {
        backBtn.setOnClickListener(view -> onBackPressed());

        saveBtn.setOnClickListener(view -> onSaveBtnClickAction());

        moreBtn.setOnClickListener(v -> showPopupMenu());

    }

    private void showPopupMenu() {
        PopupMenu popup = new PopupMenu(mActivity, moreBtn);
        popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.presentAllMenu) {

            }
            if (item.getItemId() == R.id.datePickMenu) {
                showCalenderDialog();
            }

            return true;
        });

        popup.show();
    }

    private void onSaveBtnClickAction() {
        int totalPresent = 0;
        int totalAbsent = 0;

        for (Student student : studentList) {
            String status = student.getStatus();
            if (status.equals("P")) totalPresent++;
            if (!status.equals("P")) totalAbsent++;
        }

        MyDialog dialog = new MyDialog("Save Attendance","Total Present: "+totalPresent+"\n\nTotal Absent: "+totalAbsent);
        dialog.show(getSupportFragmentManager(),MyDialog.CONFIRM_DIALOG);
        dialog.setConfirmListener(this::onConfirmClick);
    }

    private void saveAttendanceStatus() {
        for (Student student : studentList) {
            String status = student.getStatus();
            if (!status.equals("P")) status = "A";
            Log.e(TAG,"saveAttendanceStatus: sid: "+student.getSid()+" cid: "+cid+" date: "+calender.getDate()+" status: "+status);
            long rowId = dbHelper.addStatus(student.getSid(), cid, calender.getDate(), status);
            if (rowId == -1) {
                dbHelper.updateStatus(student.getSid(), calender.getDate(), status);
            }
        }
    }

    private void onConfirmClick(boolean confirmation) {
        if(confirmation){
            saveAttendanceStatus();
        }
    }

    private void loadStatus() {
        for (Student student : studentList) {
            String status = dbHelper.getStatus(student.getSid(), calender.getDate());
            if (status != null) student.setStatus(status);
            else student.setStatus("");
        }
        adapter.notifyDataSetChanged();
    }

    private void showCalenderDialog() {
        calender.show(getSupportFragmentManager(), "");
        calender.setListener(this::pickDate);
    }

    private void pickDate(int year, int month, int day) {
        calender.setDate(year, month, day);
        dateTv.setText(subjectName + " | " + calender.getDate());
        loadStatus();
    }

    private void populateRecyclerView() {
        Log.d(TAG, "populateRecyclerView: " + studentList.toString());
        adapter = new AttendanceAdapter(mActivity, studentList);
        studentsRv.setAdapter(adapter);
        adapter.setListener(this::changeAttendanceStatus);
    }

    private void changeAttendanceStatus(int position) {
        String status = studentList.get(position).getStatus();
        if (status.equals("P")) status = "A";
        else status = "P";
        studentList.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void initViews() {
        titleTv = findViewById(R.id.titleTv);
        dateTv = findViewById(R.id.dateTv);
        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);
        moreBtn = findViewById(R.id.moreBtn);
        studentsRv = findViewById(R.id.studentsRv);
    }

    private void initVariables() {
        mActivity = TakeAttendanceActivity.this;
        studentList = new ArrayList<>();
        dbHelper = new MyDBHelper(mActivity);
        calender = new MyCalender();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() == "Yellow") {
            Toast.makeText(mActivity, "yellow", Toast.LENGTH_SHORT).show();

        } else if (item.getTitle() == "Gray") {

        } else if (item.getTitle() == "Cyan") {

        }
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
//            adapter.notifyItemChanged(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(mActivity, "Student Successfully Deleted...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mActivity, "Student Not Deleted...", Toast.LENGTH_LONG).show();
        }
    }
}