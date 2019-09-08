package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.coconuttest.tyu91.coconuttest.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataAccessRecordListAdapter extends ArrayAdapter<AnnotationInfo> implements SeekBar.OnSeekBarChangeListener {
    public static final long ONE_DAY_TIME = 24 * 60 * 60 * 1000;
    Context mContext;
    List<AnnotationInfo> mAccessRecordList;
    public DataAccessRecordListAdapter(@NonNull Context context, @NonNull List<AnnotationInfo> accessRecordLList) {
        super(context, R.layout.data_access_item_layout, accessRecordLList);
        mContext = context;
        this.mAccessRecordList = accessRecordLList;
    }

    private BarChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AnnotationInfo annotationInfo = mAccessRecordList.get(position);
        View view = inflater.inflate(R.layout.data_access_item_layout, parent, false);
        TextView purposeView = view.findViewById(R.id.data_access_purposes);
        TextView egressDescriptionView = view.findViewById(R.id.data_egress_description);
        TextView viewSharedDataView = view.findViewById(R.id.view_shared_data);

        String purposesString = HSUtils.generatePurposesString(annotationInfo.purposes, "\n");

        String destinationsString = HSUtils.generateEgressInfoString(annotationInfo.destinations);
        String dataTypesString = HSUtils.generateEgressInfoString(annotationInfo.leakedDataTypes);
        AccessType accessType = annotationInfo.accessType;

        purposeView.setText(purposesString);
        String egressDescriptionText;
        if (annotationInfo.dataLeaked) {
            if (destinationsString == null && dataTypesString == null) {
                egressDescriptionText = String.format("This app may send the %s data or information derived from the %s data out of the phone.", annotationInfo.dataGroup, annotationInfo.dataGroup);
            } else if (destinationsString == null && dataTypesString != null) {
                egressDescriptionText = String.format("This app will send %s out of the phone.", dataTypesString, destinationsString);
            } else if (destinationsString != null && dataTypesString == null) {
                egressDescriptionText = String.format("This app will send the %s data or information derived from the %s data to %s.", annotationInfo.dataGroup, annotationInfo.dataGroup);
            } else {
                egressDescriptionText = String.format("This app will send %s to %s.", dataTypesString, destinationsString);
            }
        } else {
            egressDescriptionText = String.format("This app will not send the %s data or information derived from the %s data out of the phone.", annotationInfo.dataGroup, annotationInfo.dataGroup);
            viewSharedDataView.setVisibility(View.INVISIBLE);
        }
        egressDescriptionView.setText(egressDescriptionText);

//        seekBarX = view.findViewById(R.id.seekBar1);
//        seekBarY = view.findViewById(R.id.seekBar2);
//
//        tvX = view.findViewById(R.id.tvXMax);
//        tvY = view.findViewById(R.id.tvYMax);
        chart = view.findViewById(R.id.chart1);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

//        // if more than 60 entries are displayed in the chart, no values will be
//        // drawn
//        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
//        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        ValueFormatter yAxisLeftFormatter = new UnitValueFormatter(accessType == AccessType.CONTINUOUS ? "minutes" : "times");

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setLabelCount(8, false);
        leftAxis.setGranularity(1f);
        leftAxis.setValueFormatter(yAxisLeftFormatter);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.disableAxisLineDashedLine();
        leftAxis.disableGridDashedLine();
        leftAxis.removeAllLimitLines();

        ValueFormatter yAxisRightFormatter = new ClearValueFormatter();
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.disableAxisLineDashedLine();
        rightAxis.disableGridDashedLine();
        rightAxis.setValueFormatter(yAxisRightFormatter);
        rightAxis.removeAllLimitLines();

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(mContext, xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis() - ONE_DAY_TIME * 7);
        float start = c.get(Calendar.DAY_OF_WEEK);
        int count = 7;
        ArrayList<BarEntry> values = new ArrayList<>();

        List<Float> accessCounterList = AccessHistory.getInstance()
                .getAccessCountersInLastWeek(accessType, annotationInfo.ID);

        for (int i = 0; i < count; i++) {
            float val = accessCounterList.get(i);

            values.add(new BarEntry(start + i, val));
        }

        BarDataSet set1 = new BarDataSet(values, "Summary of data access records for this purpose");

        int startColor1 = ContextCompat.getColor(mContext, android.R.color.holo_blue_light);
        int endColor1 = ContextCompat.getColor(mContext, android.R.color.holo_blue_light);

        List<GradientColor> gradientColors = new ArrayList<>();
        gradientColors.add(new GradientColor(startColor1, endColor1));

        set1.setGradientColors(gradientColors);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        chart.setData(data);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));

//        setData(seekBarX.getProgress(), seekBarY.getProgress());
        chart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
