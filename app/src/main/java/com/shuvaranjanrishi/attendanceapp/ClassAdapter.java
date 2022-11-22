package com.shuvaranjanrishi.attendanceapp;

/*
    Created by Shuva Ranjan Rishi on 11/18/2022
*/

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private final Context mContext;
    private final List<ClassItem> classItemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ClassAdapter(Context mContext, List<ClassItem> classItemList) {
        this.mContext = mContext;
        this.classItemList = classItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_class, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (classItemList != null) {
            ClassItem classItem = classItemList.get(position);

            holder.classNameTv.setText("" + classItem.getClassName());
            holder.subjectNameTv.setText("" + classItem.getSubjectName());
        }
    }

    @Override
    public int getItemCount() {
        return classItemList == null ? 0 : classItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView classNameTv, subjectNameTv;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            classNameTv = itemView.findViewById(R.id.classNameTv);
            subjectNameTv = itemView.findViewById(R.id.subjectNameTv);

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
