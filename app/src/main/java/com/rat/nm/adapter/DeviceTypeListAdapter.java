package com.rat.nm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rat.networkmanager.R;
import com.rat.nm.entity.model.Device;
import com.rat.nm.entity.model.DeviceType;

import java.util.List;

/**
 * author : L.jinzhu
 * date : 2015/09/14
 * introduce : 设备列表adapter
 */
public class DeviceTypeListAdapter extends BaseAdapter {
    private Context context;
    private List<DeviceType> list;
    private ViewHolder viewHolder;

    public DeviceTypeListAdapter(Context context, List<DeviceType> list) {
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void modifyData(List<DeviceType> ls, boolean isClean) {
        if (isClean) {
            this.list.clear();
            this.list = ls;
        } else {
            for (DeviceType dt : ls) {
                this.list.add(dt);
            }
        }
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_device_type_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.indexBtn = (Button) convertView.findViewById(R.id.indexBtn);
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.nameTV);
            viewHolder.statusTV = (TextView) convertView.findViewById(R.id.statusTV);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DeviceType deviceType = list.get(position);
        if (null == deviceType)
            return convertView;
        viewHolder.indexBtn.setText(String.valueOf(position));
        viewHolder.nameTV.setText(deviceType.getName4Show());
        viewHolder.statusTV.setText(deviceType.getDescribe());
        viewHolder.statusTV.setTextColor(context.getResources().getColor(R.color.blue));
        return convertView;
    }

    private class ViewHolder {
        private Button indexBtn;
        private TextView nameTV;
        private TextView statusTV;
    }
}