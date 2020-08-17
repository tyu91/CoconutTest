package com.example.honeysucklelib.HoneysuckleLib;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.example.honeysucklelib.R;

public class PrivacyPreferenceFragment extends PreferenceFragmentCompat {
    public static final long ONE_DAY_TIME = 24 * 60 * 60 * 1000;
    public static final String TITLE = "title";
    public static final String XML_NAME = "xmlName";
    public static final String DATA_USE_KEY = "dataUseKey";
    BarChart barChart;

    public static int getXmlId (Context context, String resourceName) {
        return context.getResources().getIdentifier(resourceName, "xml", context.getPackageName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int xmlId = getXmlId(HSStatus.getApplicationContext(),"settings_privacy_center");
        if (getArguments() != null && getArguments().containsKey(XML_NAME)) {
            xmlId = getXmlId(HSStatus.getApplicationContext(),
                    String.format("settings_privacy_center_%s", getArguments().get(XML_NAME)));
        }
        addPreferencesFromResource(xmlId);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleAndChart();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        setTitleAndChart();
    }

    private void setTitleAndChart() {

//        String title = "Data use overview & control";
//        String title = "Control data collected by the app";
        String title = getString(R.string.privacy_center_name);

        if (getArguments() != null && getArguments().containsKey(TITLE)) {
            DataDiagramPreference preference = findPreference(getResources().getString(R.string.data_diagram_key));
            if (preference != null) {
                preference.setCallback(()->{
                    barChart = preference.barChart;
                    loadChart(barChart);
                });
            }
            title = getArguments().getString(TITLE);
        }/* else {
            barChart.clear();
            barChart.invalidate();
            barChart.setVisibility(View.GONE);
        }*/

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    void loadChart(BarChart barChart) {
        if (barChart == null) {
            return;
        }
        barChart.setVisibility(View.VISIBLE);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        barChart.getDescription().setEnabled(false);

//        // if more than 60 entries are displayed in the chart, no values will be
//        // drawn
//        chart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
//        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

//        ValueFormatter yAxisLeftFormatter = new UnitValueFormatter(accessType == AccessType.CONTINUOUS ? "minutes" : "times");
        ValueFormatter yAxisLeftFormatter = new UnitValueFormatter("times");


        YAxis leftAxis = barChart.getAxisLeft();
//        leftAxis.setLabelCount(8, false);
        leftAxis.setGranularity(1f);
        leftAxis.setValueFormatter(yAxisLeftFormatter);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.disableAxisLineDashedLine();
        leftAxis.disableGridDashedLine();
        leftAxis.removeAllLimitLines();

        ValueFormatter yAxisRightFormatter = new ClearValueFormatter();
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.disableAxisLineDashedLine();
        rightAxis.disableGridDashedLine();
        rightAxis.setValueFormatter(yAxisRightFormatter);
        rightAxis.removeAllLimitLines();

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv); // Set the marker to the chart

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis() - ONE_DAY_TIME * 7);
        float start = c.get(Calendar.DAY_OF_WEEK);
        int count = 7;
        ArrayList<BarEntry> values = new ArrayList<>();

        List<Integer> accessCounterList = Arrays.asList(10, 8, 0, 0, 0, 0, 0);
//        List<Float> accessCounterList = AccessHistory.getInstance()
//                .getAccessCountersInLastWeek(accessType, annotationInfo.ID);

        for (int i = 0; i < count; i++) {
            float val = accessCounterList.get(i);

            values.add(new BarEntry(start + i, val));
        }

        BarDataSet set1 = new BarDataSet(values, "Foreground access to text input for this purpose");

        int startColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
        int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);

        List<GradientColor> gradientColors = new ArrayList<>();
        gradientColors.add(new GradientColor(startColor1, endColor1));

        set1.setGradientColors(gradientColors);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        barChart.setData(data);

    }

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

    public class UnitValueFormatter extends ValueFormatter
    {

        private final DecimalFormat mFormat;
        private String suffix;

        public UnitValueFormatter(String suffix) {
            mFormat = new DecimalFormat("###,###,###,##0");
            this.suffix = suffix;
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + suffix;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            if (axis instanceof XAxis) {
                return mFormat.format(value);
            } else if (value > 0) {
                return mFormat.format(value) + suffix;
            } else {
                return mFormat.format(value);
            }
        }
    }

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

    public class XYMarkerView extends MarkerView {

        private final TextView tvContent;
        private final ValueFormatter xAxisValueFormatter;

        private final DecimalFormat format;

        public XYMarkerView(Context context, ValueFormatter xAxisValueFormatter) {
            super(context, R.layout.custom_marker_view);

            this.xAxisValueFormatter = xAxisValueFormatter;
            tvContent = findViewById(R.id.tvContent);
            format = new DecimalFormat("##0");
        }

        // runs every time the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            tvContent.setText(String.format("x: %s, y: %s", xAxisValueFormatter.getFormattedValue(e.getX()), format.format(e.getY())));

            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }
}
