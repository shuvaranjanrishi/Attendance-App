package com.shuvaranjanrishi.attendanceapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {

    private static final String TAG = SheetActivity.class.getCanonicalName();

    private Activity mActivity;
    private ImageButton backBtn;
    private TextView titleTv, dateTv;
    private RecyclerView studentsRv;
    private MyDBHelper dbHelper;
    //intent data
    private long[] idArray, rollArray;
    private String[] nameArray;
    private String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        initViews();

        initVariables();

        getIntentData();

        showTable();

        initListeners();
    }

    private void showTable() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        int dayInMonth = getDayOfMonth(month);

        //row setup
        int rowSize = idArray.length + 1;
        TableRow[] rows = new TableRow[rowSize];
        TextView[] rollTvs = new TextView[rowSize];
        TextView[] nameTvs = new TextView[rowSize];
        TextView[][] statusTvs = new TextView[rowSize][dayInMonth + 1];

        for (int i = 0; i < rowSize; i++) {
            rollTvs[i] = new TextView(this);
            nameTvs[i] = new TextView(this);
            for (int j = 0; j <= dayInMonth; j++) {
                statusTvs[i][j] = new TextView(this);
            }
        }

        //headers
        rollTvs[0].setText("Roll");
        rollTvs[0].setTypeface(rollTvs[0].getTypeface(), Typeface.BOLD);
        nameTvs[0].setText("Name");
        nameTvs[0].setTypeface(nameTvs[0].getTypeface(), Typeface.BOLD);
        for (int i = 0; i <= dayInMonth; i++) {
            statusTvs[0][i].setText(String.valueOf(i));
            statusTvs[0][i].setTypeface(statusTvs[0][i].getTypeface(), Typeface.BOLD);
        }

        for (int i = 1; i < rowSize; i++) {
            rollTvs[i].setText(String.valueOf(rollArray[i - 1]));
            nameTvs[i].setText(nameArray[i - 1]);
            for (int j = 0; j < dayInMonth; j++) {
                String day = String.valueOf(j);
                if (day.length() == 1) day = "0" + i;
                String date = day + "-" + month;
                String status = dbHelper.getStatus(idArray[i - 1], date);
                statusTvs[i][j].setText(status);
            }
        }

        for (int i = 0; i < rowSize; i++) {
            rows[i] = new TableRow(this);

            if (i % 2 == 0) rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            else rows[i].setBackgroundColor(Color.parseColor("#E4E4E4"));

            rollTvs[i].setPadding(16, 16, 16, 16);
            nameTvs[i].setPadding(16, 16, 16, 16);

            rows[i].addView(rollTvs[i]);
            rows[i].addView(nameTvs[i]);
            rows[i].setOverScrollMode();

            for (int j = 0; j <= dayInMonth; j++) {
                statusTvs[i][j].setPadding(16, 16, 16, 16);
                rows[i].addView(statusTvs[i][j]);
            }
            tableLayout.addView(rows[i]);
        }
    }

    private int getDayOfMonth(String month) {
        int monthIndex = Integer.parseInt(month.split("-")[0]);
        int year = Integer.parseInt(month.split("-")[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void getIntentData() {
        idArray = getIntent().getLongArrayExtra("idArray");
        rollArray = getIntent().getLongArrayExtra("rollArray");
        nameArray = getIntent().getStringArrayExtra("nameArray");
        month = getIntent().getStringExtra("month");
        dateTv.setText(month);
    }

    private void initViews() {
        titleTv = findViewById(R.id.titleTv);
        dateTv = findViewById(R.id.dateTv);
        backBtn = findViewById(R.id.backBtn);
        studentsRv = findViewById(R.id.studentsRv);
    }

    private void initVariables() {
        mActivity = SheetActivity.this;
        dbHelper = new MyDBHelper(mActivity);
    }

    private void initListeners() {
        backBtn.setOnClickListener(v -> onBackPressed());
    }
}