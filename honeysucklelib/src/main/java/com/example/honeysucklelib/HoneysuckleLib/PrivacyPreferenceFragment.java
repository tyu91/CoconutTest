package com.example.honeysucklelib.HoneysuckleLib;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.honeysucklelib.R;

import static com.example.honeysucklelib.HoneysuckleLib.HSUtils.notificationConfigKeyPattern;

public class PrivacyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final long ONE_DAY_TIME = 24 * 60 * 60 * 1000;
    public static final String TITLE = "title";
    public static final String DATA_USE_KEY = "dataUseKey";
    BarChart barChart;
    SharedPreferences sharedPrefs;

    public static int getXmlId (Context context, String resourceName) {
        return context.getResources().getIdentifier(resourceName, "xml", context.getPackageName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HSStatus.getApplicationContext());
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        if (getArguments() != null && getArguments().containsKey(DATA_USE_KEY) &&
                "view_data_activities".equals(getArguments().getString(DATA_USE_KEY))) {
            Context activityContext = getActivity();

            PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(activityContext);
            setPreferenceScreen(preferenceScreen);

            PreferenceCategory recentPreferenceCategory = new PreferenceCategory(activityContext);
            recentPreferenceCategory.setTitle("Recent data activities");
            recentPreferenceCategory.setIconSpaceReserved(false);
            getPreferenceScreen().addPreference(recentPreferenceCategory);

            ArrayList<AccessHistory.AccessRecord> latestAccessRecordList =
                    AccessHistory.getInstance().getLatestAccessRecordList();

            if (latestAccessRecordList.isEmpty()) {
                Preference preference = new Preference(activityContext);
                preference.setTitle("No data activities recorded");
                preference.setIconSpaceReserved(false);
                recentPreferenceCategory.addPreference(preference);
            } else {
                for (AccessHistory.AccessRecord accessRecord : latestAccessRecordList) {
                    PrivacyInfo privacyInfo =
                            HSStatus.getMyPrivacyInfoMap().getPrivacyInfoByID(accessRecord.ID);
                    if (privacyInfo == null) {
                        continue;
                    }
                    Preference preference = new Preference(activityContext);
                    if (privacyInfo instanceof SourcePrivacyInfo) {
                        SourcePrivacyInfo sourcePrivacyInfo = (SourcePrivacyInfo) privacyInfo;
                        preference.setTitle(String.format("Accessed %s",
                                HSUtils.getDataString(sourcePrivacyInfo.dataGroup, true)));
                        preference.setSummary(String.format("%s\nData collected %s",
                                sourcePrivacyInfo.purposes[0],
                                HSUtils.getRelativeOrAbsoluteTimeString(accessRecord.beginTimestamp)));
                        preference.setIcon(HSUtils.getDataGroupIconId(sourcePrivacyInfo.dataGroup));
                        preference.setSelectable(false);
                        recentPreferenceCategory.addPreference(preference);
                    } else if (privacyInfo instanceof SinkPrivacyInfo) {
                        SinkPrivacyInfo sinkPrivacyInfo = (SinkPrivacyInfo) privacyInfo;
                        if (sinkPrivacyInfo.accessType == AccessType.STORED_ON_DEVICE) {
                            preference.setTitle(String.format("Stored %s on device", sinkPrivacyInfo.dataGroup.toLowerCase()));
                            preference.setSummary(String.format("%s\nData stored %s",
                                    sinkPrivacyInfo.purposes[0],
                                    HSUtils.getRelativeOrAbsoluteTimeString(accessRecord.beginTimestamp)));
                        } else if (sinkPrivacyInfo.accessType == AccessType.STORED_ON_CLOUD) {
                            preference.setTitle(String.format("Stored %s on cloud", sinkPrivacyInfo.dataGroup.toLowerCase()));
                            preference.setSummary(String.format("%s\nData stored %s",
                                    sinkPrivacyInfo.purposes[0],
                                    HSUtils.getRelativeOrAbsoluteTimeString(accessRecord.beginTimestamp)));
                        } else if (sinkPrivacyInfo.accessType == AccessType.SENT_OFF_DEVICE) {
                            preference.setTitle(String.format("Sent %s off device", sinkPrivacyInfo.dataGroup.toLowerCase()));
                            preference.setSummary(String.format("%s\nData sent out %s",
                                    sinkPrivacyInfo.purposes[0],
                                    HSUtils.getRelativeOrAbsoluteTimeString(accessRecord.beginTimestamp)));
                        }
                        preference.setSelectable(false);
                        recentPreferenceCategory.addPreference(preference);
                    }
                }
            }
        } else {
            Context activityContext = getActivity();

            PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(activityContext);
            setPreferenceScreen(preferenceScreen);

            if (getArguments() != null && getArguments().containsKey(DATA_USE_KEY)) {
                SourcePrivacyInfo privacyInfo =
                        (SourcePrivacyInfo) HSStatus.getMyPrivacyInfoMap().getPrivacyInfoByID(getArguments().getString(DATA_USE_KEY));
                DataDiagramPreference dataDiagramPreference = new DataDiagramPreference(activityContext);
                dataDiagramPreference.setTitle("Last week data access history");
                dataDiagramPreference.setKey("data_diagram_key");
                dataDiagramPreference.setLayoutResource(R.layout.data_use_history_diagram_preference);
                dataDiagramPreference.setSelectable(false);
                dataDiagramPreference.setCallback(() -> {
                    barChart = dataDiagramPreference.barChart;
                    loadChart(barChart, privacyInfo.ID);
                });

                preferenceScreen.addPreference(dataDiagramPreference);

                PreferenceCategory noticeConfigPreferenceCategory = new PreferenceCategory(activityContext);
                noticeConfigPreferenceCategory.setIconSpaceReserved(false);
                noticeConfigPreferenceCategory.setTitle("Configure data collection alert");
                preferenceScreen.addPreference(noticeConfigPreferenceCategory);

                String noticeConfigPreferenceKey =
                        String.format(notificationConfigKeyPattern, privacyInfo.ID);
                ListPreference noticeConfigPreference = new ListPreference(activityContext);
                noticeConfigPreferenceCategory.addPreference(noticeConfigPreference);

                noticeConfigPreference.setKey(noticeConfigPreferenceKey);
                noticeConfigPreference.setTitle("Alert notification frequency");
                noticeConfigPreference.setIconSpaceReserved(false);
                noticeConfigPreference.setEntries(R.array.notification_frequency);
                noticeConfigPreference.setEntryValues(R.array.notification_frequency);
                String defaultJitNoticeFrequencyString;
                JitNoticeFrequency jitNoticeFrequency = privacyInfo.jitNoticeFrequency;
                switch (jitNoticeFrequency) {
                    case NOTIFICATION_ALWAYS_POP_OUT:
                        defaultJitNoticeFrequencyString = "Always pop up";
                        break;
                    case NOTIFICATION_POP_OUT_FIRST_TIME_ONLY:
                        defaultJitNoticeFrequencyString = "Pop up on first collection";
                        break;
                    case SEND_NOTIFICATION_SILENTLY:
                        defaultJitNoticeFrequencyString = "Show data icon on stats bar";
                        break;
                    case DO_NOT_SEND_NOTIFICATION:
                        defaultJitNoticeFrequencyString = "No alert";
                        break;
                    case UNKNOWN:
                    default:
                        defaultJitNoticeFrequencyString = "Not configured";
                        break;
                }
                noticeConfigPreference.setSummary(sharedPrefs.getString(noticeConfigPreferenceKey, defaultJitNoticeFrequencyString));
            } else {
                Preference viewDataActivitiesPreference = new Preference(activityContext);
                viewDataActivitiesPreference.setKey("view_data_activities");
                viewDataActivitiesPreference.setTitle("View data activities");
                viewDataActivitiesPreference.setIconSpaceReserved(false);
                viewDataActivitiesPreference.setFragment("com.example.honeysucklelib.HoneysuckleLib.PrivacyPreferenceFragment");

                preferenceScreen.addPreference(viewDataActivitiesPreference);

                HashMap<PersonalDataGroup, ArrayList<SourcePrivacyInfo>> personalDataGroupIdHashMap = new HashMap<>();
                for (Map.Entry<String, PrivacyInfo> entry : HSStatus.getMyPrivacyInfoMap().privacyInfoMap.entrySet()) {
                    if (entry.getValue() instanceof SourcePrivacyInfo) {
                        SourcePrivacyInfo sourcePrivacyInfo = (SourcePrivacyInfo) entry.getValue();
                        if (!personalDataGroupIdHashMap.containsKey(sourcePrivacyInfo.dataGroup)) {
                            personalDataGroupIdHashMap.put(sourcePrivacyInfo.dataGroup, new ArrayList<>());
                        }
                        personalDataGroupIdHashMap.get(sourcePrivacyInfo.dataGroup).add(sourcePrivacyInfo);
                    }
                }
                for (Map.Entry<PersonalDataGroup, ArrayList<SourcePrivacyInfo>> entry : personalDataGroupIdHashMap.entrySet()) {
                    PreferenceCategory preferenceCategory = new PreferenceCategory(activityContext);
                    preferenceCategory.setTitle(HSUtils.getDataString(entry.getKey(), false));
                    preferenceCategory.setIconSpaceReserved(false);
                    preferenceScreen.addPreference(preferenceCategory);

                    ArrayList<SourcePrivacyInfo> privacyInfoList = entry.getValue();
                    for (SourcePrivacyInfo sourcePrivacyInfo : privacyInfoList) {
                        Preference preference = new Preference(activityContext);
                        preferenceCategory.addPreference(preference);
                        preference.setTitle(sourcePrivacyInfo.purposes[0]);
                        preference.setKey(sourcePrivacyInfo.ID);
                        preference.setIconSpaceReserved(false);
                        preference.setFragment("com.example.honeysucklelib.HoneysuckleLib.PrivacyPreferenceFragment");
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleAndPreference();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        setTitleAndChart();
    }

    private void setTitleAndPreference() {

//        String title = "Data use overview & control";
//        String title = "Control data collected by the app";
        String title = getString(R.string.privacy_center_name);

        if (getArguments() != null && getArguments().containsKey(TITLE)) {
            String ID = getArguments().getString(DATA_USE_KEY);
            SourcePrivacyInfo privacyInfo =
                    (SourcePrivacyInfo) HSStatus.getMyPrivacyInfoMap().getPrivacyInfoByID(ID);

            if (privacyInfo != null) {
                title = String.format("%s (%s)", HSUtils.getDataString(privacyInfo.dataGroup, false), getArguments().getString(TITLE).toLowerCase());
            } else {
                title = getArguments().getString(TITLE);
            }
        } else {
            for (Map.Entry<String, PrivacyInfo> entry :
                    HSStatus.getMyPrivacyInfoMap().privacyInfoMap.entrySet()) {
                String ID = entry.getKey();
                if (entry.getValue() instanceof SourcePrivacyInfo) {
                    SourcePrivacyInfo sourcePrivacyInfo = (SourcePrivacyInfo) entry.getValue();
                    Preference preference = findPreference(ID);
                    if (preference != null) {
                        if (sourcePrivacyInfo.enableAccessTracker) {
                            AccessHistory.AccessRecord record =
                                    AccessHistory.getInstance().getMostRecentAccessRecord(ID);
                            if (record == null) {
                                preference.setSummary("Never accessed");
                            } else {
                                String currentTime = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm",
                                        HSStatus.getApplicationContext().getResources().getConfiguration().locale)
                                        .format(new java.util.Date(record.beginTimestamp));
                                preference.setSummary(String.format("Last accessed at %s", currentTime));
                            }
                        }
                    }
                }
            }
        }

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(HSStatus.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissionGranted(PersonalDataGroup dataGroup) {
        switch (dataGroup) {
            case BodySensor:
                return checkPermission(Manifest.permission.BODY_SENSORS);
            case Calendar:
                return checkPermission(Manifest.permission.READ_CALENDAR);
            case CallLogs:
                return checkPermission(Manifest.permission.READ_CALL_LOG);
            case Camera:
                return checkPermission(Manifest.permission.CAMERA);
            case Contacts:
                return checkPermission(Manifest.permission.READ_CONTACTS);
            case Location:
                return checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) || checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            case Microphone:
                return checkPermission(Manifest.permission.RECORD_AUDIO);
            case Sms:
                return checkPermission(Manifest.permission.READ_SMS);
            case UserAccount:
                return checkPermission(Manifest.permission.GET_ACCOUNTS);
            case UserFile:
                return checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return true;
    }

    void loadChart(BarChart barChart, String ID) {
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

        PrivacyInfo privacyInfo = HSStatus.getMyPrivacyInfoMap().getPrivacyInfoByID(ID);
        if (privacyInfo == null) {
            return;
        }
        AccessType accessType;
        if (privacyInfo instanceof SourcePrivacyInfo) {
            accessType = ((SourcePrivacyInfo) privacyInfo).accessType;
        } else if (privacyInfo instanceof SinkPrivacyInfo) {
            accessType = ((SinkPrivacyInfo) privacyInfo).accessType;
        } else {
            return;
        }
            ValueFormatter yAxisLeftFormatter = new UnitValueFormatter(" times");
//        ValueFormatter yAxisLeftFormatter = new UnitValueFormatter("times");


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

        List<Float> accessCounterList =
                AccessHistory.getInstance().getAccessCountersInLastWeek(accessType, ID);
//        List<Float> accessCounterList = AccessHistory.getInstance()
//                .getAccessCountersInLastWeek(accessType, privacyInfo.ID);

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        } else if (pref instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) pref;
            pref.setSummary(editTextPreference.getText());
        }
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
