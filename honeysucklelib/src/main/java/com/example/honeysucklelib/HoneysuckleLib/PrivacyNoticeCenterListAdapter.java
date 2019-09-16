package com.example.honeysucklelib.HoneysuckleLib;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.honeysucklelib.R;

import java.util.List;

public class PrivacyNoticeCenterListAdapter extends ArrayAdapter<PersonalDataGroup> {
    private Context mContext;
    private List<PersonalDataGroup> dataGroupList;
    public static final String dataGroupIntentName = "dataGroup";

    public PrivacyNoticeCenterListAdapter(@NonNull Context context, List<PersonalDataGroup> dataGroupList) {
        super(context, R.layout.data_group_item_layout, dataGroupList);
        this.dataGroupList = dataGroupList;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.data_group_item_layout, parent, false);

        TextView dataGroupNameTextView = view.findViewById(R.id.data_group_name);
        dataGroupNameTextView.setText(dataGroupList.get(position).toString().replace("_", " "));
        LinearLayout dataGroupItem = view.findViewById(R.id.data_group_item);
        dataGroupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DataGroupNoticeActivity.class);
                intent.putExtra(dataGroupIntentName, dataGroupList.get(position).toString());
                mContext.startActivity(intent);
            }
        });
        return view;
    }
}
