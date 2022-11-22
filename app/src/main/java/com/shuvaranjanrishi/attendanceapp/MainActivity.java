package com.shuvaranjanrishi.attendanceapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton classAddBtn;
    private RecyclerView classRv;
    private List<ClassItem> classItemList;
    private ClassAdapter adapter;
    private MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initVariables();

        getClassList();

        populateRecyclerView();

        initListeners();
    }

    private void getClassList() {
        Cursor cursor = dbHelper.getClassList();

        classItemList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String className = cursor.getString(1);
            String subjectName = cursor.getString(2);

            ClassItem classItem = new ClassItem(id, className, subjectName);
            classItemList.add(classItem);
        }
    }

    private void initListeners() {
        classAddBtn.setOnClickListener(view -> showClassAddDialog());
    }

    private void showClassAddDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener(this::addClass);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addClass(String className, String subjectName) {
        long cid = dbHelper.addClass(className, subjectName);
        ClassItem classItem = new ClassItem(cid, className, subjectName);
        Log.d(TAG, "addClass: " + classItem);
        classItemList.add(classItem);
        adapter.notifyDataSetChanged();
    }

    private void populateRecyclerView() {
        Log.d(TAG, "populateRecyclerView: " + classItemList.toString());
        adapter = new ClassAdapter(mActivity, classItemList);
        classRv.setAdapter(adapter);
        if(classItemList.size()>1) classRv.setLayoutManager(new GridLayoutManager(this, 2));
        if(classItemList.size()>2) classRv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter.setListener(this::gotoStudentActivity);
    }

    private void gotoStudentActivity(int position) {
        Intent intent = new Intent(mActivity, StudentsActivity.class);
        intent.putExtra("ClassName", classItemList.get(position).getClassName());
        intent.putExtra("SubjectName", classItemList.get(position).getSubjectName());
        intent.putExtra("CID", classItemList.get(position).getCid());
        intent.putExtra("Position", position);
        startActivity(intent);
    }

    private void initViews() {
        classAddBtn = findViewById(R.id.classAddBtn);
        classRv = findViewById(R.id.classRv);
    }

    private void initVariables() {
        mActivity = MainActivity.this;
        classItemList = new ArrayList<>();
        dbHelper = new MyDBHelper(mActivity);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showClassUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showClassUpdateDialog(int position) {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setListener((className, subjectName) -> updateClass(position, className, subjectName));
    }

    private void updateClass(int position, String className, String subjectName) {
        int rowId = dbHelper.updateClass(classItemList.get(position).getCid(), className, subjectName);
        if (rowId > 0) {
            classItemList.get(position).setClassName(className);
            classItemList.get(position).setSubjectName(subjectName);
            adapter.notifyItemChanged(position);
            Toast.makeText(mActivity, classItemList.get(position).getCid()+"Class Successfully Updated...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mActivity, "Class Not Updated...", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteClass(int position) {
        int rowId = dbHelper.deleteClass(classItemList.get(position).getCid());
        if (rowId > 0) {
            classItemList.remove(position);
//            adapter.notifyItemChanged(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(mActivity, "Class Successfully Deleted...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mActivity, "Class Not Deleted...", Toast.LENGTH_LONG).show();
        }
    }
}