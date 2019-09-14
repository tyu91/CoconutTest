package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.coconuttest.tyu91.coconuttest.R;

import java.util.Arrays;

import static com.coconuttest.tyu91.coconuttest.HoneysuckleLib.PrivacyNoticeCenterListAdapter.dataGroupIntentName;

public class DataGroupNoticeActivity extends Activity {
    PersonalDataGroup mDataGroup;
    String mDataGroupString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnc_data_group_activity);

        Intent intent = getIntent();
        String dataGroupValue = intent.getStringExtra(dataGroupIntentName);
        mDataGroup = PersonalDataGroup.valueOf(dataGroupValue);
        mDataGroupString = mDataGroup.toString().replace("_", " ");
        TextView PNCHeaderTitleView = findViewById(R.id.allysiqi_configure_permission_title);
        PNCHeaderTitleView.setText(String.format("%s Access History", mDataGroupString));
        TextView PNCHeaderDescriptionView = findViewById(R.id.allysiqi_configure_permission_description);
        PNCHeaderDescriptionView.setText(String.format(
                "This privacy notice center contains all %s data access records from the past week",
                mDataGroupString));

        ListView dataAccessRecordListView = findViewById(R.id.data_access_record_list);
        DataAccessRecordListAdapter dataAccessRecordListAdapter =
                new DataAccessRecordListAdapter(this,
                        Arrays.asList(HSStatus.getMyAnnotationInfoMap().getAnnotationInfoListByDataGroup(mDataGroup)));
        dataAccessRecordListView.setAdapter(dataAccessRecordListAdapter);
        dataAccessRecordListAdapter.notifyDataSetChanged();

    }
}
