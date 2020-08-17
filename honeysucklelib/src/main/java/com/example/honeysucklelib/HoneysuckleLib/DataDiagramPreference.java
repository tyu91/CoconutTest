package com.example.honeysucklelib.HoneysuckleLib;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.example.honeysucklelib.R;
import com.github.mikephil.charting.charts.BarChart;

public class DataDiagramPreference extends Preference {
    public BarChart barChart;
    private UpdateBarChartCallback callback;

    public DataDiagramPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DataDiagramPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DataDiagramPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataDiagramPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        barChart = (BarChart) holder.findViewById(R.id.data_chart);
        callback.update();
    }

    interface UpdateBarChartCallback {
        void update();
    }

    public void setCallback(UpdateBarChartCallback callback) {
        this.callback = callback;
    }
}
