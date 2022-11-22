package com.shuvaranjanrishi.attendanceapp;

/*
    Created by Shuva Ranjan Rishi on 11/18/2022
*/

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Student> studentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public StudentAdapter(Context mContext, List<Student> studentList) {
        this.mContext = mContext;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (studentList != null) {
            Student student = studentList.get(position);

            holder.rollTv.setText("" + student.getRoll());
            holder.nameTv.setText("" + student.getName());
            holder.statusTv.setText("" + student.getStatus());
            holder.itemView.setBackgroundColor(getColor(student.getStatus()));
        }
    }

    private int getColor(String status) {
        if (status.equals("P"))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.presentColor)));
        else if (status.equals("A"))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.absentColor)));
        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.white)));
    }

    @Override
    public int getItemCount() {
        return studentList == null ? 0 : studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        public TextView rollTv, nameTv, statusTv;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            rollTv = itemView.findViewById(R.id.rollTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            statusTv = itemView.findViewById(R.id.statusTv);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(getAdapterPosition(),0,0,"Edit");
            contextMenu.add(getAdapterPosition(),1,0,"Delete");
        }
    }
}
