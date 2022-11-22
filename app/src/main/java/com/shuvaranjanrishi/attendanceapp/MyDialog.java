package com.shuvaranjanrishi.attendanceapp;

/*
    Created by Shuva Ranjan Rishi on 11/18/2022
*/

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {

    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String CLASS_UPDATE_DIALOG = "updateClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";
    private OnClickListener listener;

    private String text1, text2;

    public MyDialog() {
    }

    public MyDialog(String text1, String text2) {
        this.text1 = text1;
        this.text2 = text2;
    }

    public interface OnClickListener {
        void onClick(String text1, String text2);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;

        if (getTag().equals(CLASS_ADD_DIALOG)) dialog = getClassAddDialog();
        if (getTag().equals(CLASS_UPDATE_DIALOG)) dialog = getClassUpdateDialog();
        if (getTag().equals(STUDENT_ADD_DIALOG)) dialog = getStudentAddDialog();
        if (getTag().equals(STUDENT_UPDATE_DIALOG)) dialog = getStudentUpdateDialog();

        return dialog;
    }

    private Dialog getStudentUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.my_dialog, null);
        builder.setView(view);

        TextView titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText("Update Student");

        EditText rollEt = view.findViewById(R.id.edittext1);
        rollEt.setInputType(InputType.TYPE_CLASS_NUMBER);
        rollEt.setHint("Roll");
        rollEt.setText("Roll: " + text1);
        rollEt.setEnabled(false);

        EditText nameEt = view.findViewById(R.id.edittext2);
        nameEt.setHint("Name");
        nameEt.setText("" + text2);

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setText("Update");

        cancelBtn.setOnClickListener(view1 -> dismiss());

        saveBtn.setOnClickListener(view12 -> {
            String roll = rollEt.getText().toString().trim();
            String name = nameEt.getText().toString().trim();

            if (listener != null) {
                listener.onClick(roll, name);
            }
            dismiss();
        });

        return builder.create();
    }

    private Dialog getClassUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.my_dialog, null);
        builder.setView(view);

        TextView titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText("Update Class");

        EditText classNameEt = view.findViewById(R.id.edittext1);
        classNameEt.setHint("Class Name");

        EditText subjectNameEt = view.findViewById(R.id.edittext2);
        subjectNameEt.setHint("Subject Name");

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setText("Update");

        cancelBtn.setOnClickListener(view1 -> dismiss());

        saveBtn.setOnClickListener(view12 -> {
            String className = classNameEt.getText().toString().trim();
            String subjectName = subjectNameEt.getText().toString().trim();

            if (listener != null) {
                listener.onClick(className, subjectName);
            }
            dismiss();
        });

        return builder.create();
    }

    private Dialog getClassAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.my_dialog, null);
        builder.setView(view);

        TextView titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText("Add New Class");

        EditText classNameEt = view.findViewById(R.id.edittext1);
        classNameEt.setHint("Class Name");

        EditText subjectNameEt = view.findViewById(R.id.edittext2);
        subjectNameEt.setHint("Subject Name");

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        cancelBtn.setOnClickListener(view1 -> dismiss());

        saveBtn.setOnClickListener(view12 -> {
            String className = classNameEt.getText().toString().trim();
            String subjectName = subjectNameEt.getText().toString().trim();

            if (listener != null) {
                listener.onClick(className, subjectName);
            }
            dismiss();
        });

        return builder.create();
    }

    private Dialog getStudentAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.my_dialog, null);
        builder.setView(view);

        TextView titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText("Add New Student");

        EditText rollEt = view.findViewById(R.id.edittext1);
        rollEt.setInputType(InputType.TYPE_CLASS_NUMBER);
        rollEt.setHint("Roll");

        EditText nameEt = view.findViewById(R.id.edittext2);
        nameEt.setHint("Name");

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        cancelBtn.setOnClickListener(view1 -> dismiss());

        saveBtn.setOnClickListener(view12 -> {
            String roll = rollEt.getText().toString().trim();
            String name = nameEt.getText().toString().trim();
            rollEt.setText(String.valueOf(Integer.parseInt(roll) + 1));
            nameEt.setText("");

            if (listener != null) {
                listener.onClick(roll, name);
            }
        });

        return builder.create();
    }
}
