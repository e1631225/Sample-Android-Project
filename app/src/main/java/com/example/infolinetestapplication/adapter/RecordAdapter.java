package com.example.infolinetestapplication.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infolinetestapplication.R;
import com.example.infolinetestapplication.entity.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suleyman on 31.3.2016.
 *
 */
public class RecordAdapter extends BaseAdapter {
    List<Record> recordList = new ArrayList<>();
    Activity activity;

    public RecordAdapter(List<Record> recordList, Activity activity) {
        this.recordList = recordList;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.record_item_layout, null);
        }

        ImageView snapshot = (ImageView) view.findViewById(R.id.recordItemSnapshot);
        TextView text = (TextView) view.findViewById(R.id.recordItemText);
        Record record = recordList.get(position);
        snapshot.setImageBitmap(BitmapFactory.decodeFile(record.getSnapshotPath()));
        text.setText(record.getVideoPath().substring(record.getVideoPath().lastIndexOf("/")+1));
        return view;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public long getItemId(int position) {
        return recordList.get(position).getId();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    public List<Record> getRecordList() {
        return recordList;
    }
}
