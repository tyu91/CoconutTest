package com.example.honeysucklelib.HoneysuckleLib;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Calendar;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter extends ValueFormatter
{

    private final String[] mDaysOfWeek = new String[]{
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };

    private final BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {

//        long timeInMillis = (long) value;
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(timeInMillis);
//        return mDaysOfWeek[c.get(Calendar.DAY_OF_WEEK)];
        int day = (int) value;
        return mDaysOfWeek[day % 7];
    }

}