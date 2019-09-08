package com.coconuttest.tyu91.coconuttest.HoneysuckleLib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coconuttest.tyu91.coconuttest.R;

import java.util.List;

public class DataAccessRecordListAdapter extends ArrayAdapter<AnnotationInfo> {
    Context mContext;
    List<AnnotationInfo> mAccessRecordList;
    public DataAccessRecordListAdapter(@NonNull Context context, @NonNull List<AnnotationInfo> accessRecordLList) {
        super(context, R.layout.data_access_item_layout, accessRecordLList);
        mContext = context;
        this.mAccessRecordList = accessRecordLList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.data_access_item_layout, parent, false);
        return view;
    }
}
