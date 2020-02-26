package com.example.honeysucklelib.HoneysuckleLib;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.example.honeysucklelib.R;

import java.util.Arrays;

public class PrivacyNoticeCenterActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pnc_activity);

        TextView PNCHeaderTitleView = findViewById(R.id.PNC_title);
        PNCHeaderTitleView.setText("Privacy Notice Center");
        TextView PNCHeaderDescriptionView = findViewById(R.id.PNC_description);
        PNCHeaderDescriptionView.setText(
                "This privacy notice center contains all sensitive data access records from the past week");

        ListView dataGroupListView = findViewById(R.id.data_group_notice_list);
        PrivacyNoticeCenterListAdapter dataGroupListAdapter = new PrivacyNoticeCenterListAdapter(
                this, Arrays.asList(HSStatus.getMyAnnotationInfoMap().getAccessedDataGroups()));
        dataGroupListView.setAdapter(dataGroupListAdapter);
        dataGroupListAdapter.notifyDataSetChanged();
    }
}