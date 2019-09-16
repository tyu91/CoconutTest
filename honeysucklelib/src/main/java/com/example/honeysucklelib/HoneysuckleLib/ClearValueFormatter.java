package com.example.honeysucklelib.HoneysuckleLib;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class ClearValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return "";
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return "";
    }

}
