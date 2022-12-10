package com.shuvaranjanrishi.attendanceapp;

/*
    Created by Shuva Ranjan Rishi on 11/18/2022
*/

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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
    public static final String CONFIRM_DIALOG = "confirmDialog";
    private OnClickListener listener;
    private OnStudentClickListener studentClickListener;
    private OnConfirmListener confirmListener;

    private String text1, text2, text3;

    public MyDialog() {
    }

    public MyDialog(String text1, String text2) {
        this.text1 = text1;
        this.text2 = text2;
    }

    public MyDialog(String text1, String text2, String text3) {
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
    }

    public interface OnClickListener {
        void onClick(String text1, String text2);
    }

    public interface OnStudentClickListener {
        void onClick(String text1, String text2, String text3);
    }

    public interface OnConfirmListener {
        void onConfirmClick(boolean confirmation);
    }

    public void setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setStudentClickListener(OnStudentClickListener studentClickListener) {
        this.studentClickListener = studentClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;

        assert getTag() != null;

        if (getTag().equals(CLASS_ADD_DIALOG)) dialog = getClassAddDialog();
        if (getTag().equals(CLASS_UPDATE_DIALOG)) dialog = getClassUpdateDialog();
        if (getTag().equals(STUDENT_ADD_DIALOG)) dialog = getStudentAddDialog();
        if (getTag().equals(STUDENT_UPDATE_DIALOG)) dialog = getStudentUpdateDialog();
        if (getTag().equals(CONFIRM_DIALOG)) dialog = getGetConfirmDialog();

        return dialog;
    }

    private Dialog getStudentUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_student_dialog, null);
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

        EditText addressEt = view.findViewById(R.id.edittext3);
        addressEt.setHint("Address");
        addressEt.setText("" + text3);

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setText("Update");

        cancelBtn.setOnClickListener(view1 -> dismiss());

        saveBtn.setOnClickListener(view12 -> {
            String roll = rollEt.getText().toString().trim();
            String name = nameEt.getText().toString().trim();
            String address = addressEt.getText().toString().trim();

            if (!validate(roll, rollEt) || !validate(name, nameEt) || !validate(address, addressEt)) {
                return;
            }
            if (studentClickListener != null) {
                studentClickListener.onClick(roll, name,address);
            }
            dismiss();
        });

        return builder.create();
    }

    private boolean validate(String text, EditText editText) {
        if (TextUtils.isEmpty(text)) {
            editText.setError("Field should not be empty...");
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private Dialog getClassUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.my_dialog, null);
        builder.setView(view);

        TextView titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText("Update Class");

        EditText classNameEt = view.findViewById(R.id.edittext1);
        classNameEt.setHint("Class Name");
        classNameEt.setText(text1);

        EditText subjectNameEt = view.findViewById(R.id.edittext2);
        subjectNameEt.setHint("Subject Name");
        subjectNameEt.setText(text2);

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setText("Update");

        cancelBtn.setOnClickListener(view1 -> dismiss());

        saveBtn.setOnClickListener(view12 -> {
            String className = classNameEt.getText().toString().trim();
            String subjectName = subjectNameEt.getText().toString().trim();

            if (!validate(className, classNameEt)) {
                return;
            }
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

            if (!validate(className, classNameEt)) {
                return;
            }
            if (listener != null) {
                listener.onClick(className, subjectName);
            }
            dismiss();
        });

        return builder.create();
    }

    private Dialog getStudentAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_student_dialog, null);
        builder.setView(view);

        TextView titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText("Add New Student");

        EditText rollEt = view.findViewById(R.id.edittext1);
        rollEt.setInputType(InputType.TYPE_CLASS_NUMBER);
        rollEt.setHint("Roll");

        EditText nameEt = view.findViewById(R.id.edittext2);
        nameEt.setInputType(InputType.TYPE_CLASS_TEXT);
        nameEt.setHint("Name");

        EditText addressEt = view.findViewById(R.id.edittext3);
        addressEt.setInputType(InputType.TYPE_CLASS_TEXT);
        addressEt.setHint("Address");

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        cancelBtn.setOnClickListener(view1 -> dismiss());

        saveBtn.setOnClickListener(view12 -> {
            String roll = rollEt.getText().toString().trim();
            String name = nameEt.getText().toString().trim();
            String address = addressEt.getText().toString().trim();

            if (!validate(roll, rollEt) || !validate(name, nameEt) || !validate(address, addressEt)) {
                return;
            }
            rollEt.setText(String.valueOf(Integer.parseInt(roll) + 1));
            nameEt.setText("");
            addressEt.setText("");

            if (studentClickListener != null) {
                studentClickListener.onClick(roll, name,address);
            }
        });

        return builder.create();
    }

    private Dialog getGetConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.my_confirm_dialog, null);
        builder.setView(view);

        TextView titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText(text1);

        TextView messageTv = view.findViewById(R.id.messageTv);
        messageTv.setText(text2);

        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        Button yesBtn = view.findViewById(R.id.yesBtn);

        cancelBtn.setOnClickListener(view1 -> {
            if (confirmListener != null) {
                confirmListener.onConfirmClick(false);
            }
            dismiss();
        });

        yesBtn.setOnClickListener(view12 -> {
            if (confirmListener != null) {
                confirmListener.onConfirmClick(true);
            }
            dismiss();
        });

        return builder.create();
    }
}
