package com.demo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nothief.R;

public class LocationInfoAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] item;

	public LocationInfoAdapter(Context cxt) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(cxt);

		SharedPreferences preferences = cxt.getSharedPreferences("LocInfo",
				Context.MODE_PRIVATE);
		String latitude = preferences.getString("latitude", "");
		String longitude = preferences.getString("longitude", "");
		String altitude = preferences.getString("altitude", "");
		String accuracy = preferences.getString("accuracy", "");
		String bearing = preferences.getString("bearing", "");
		String speed = preferences.getString("speed", "");

		item = new String[] { "经度：" + longitude, "纬度：" + latitude,
				"海拔：" + altitude, "精度：" + accuracy, "方位：" + bearing,
				"速度：" + speed };
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.loc_adapter_item, parent,
					false);
		}
		TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
		tv_item.setText(item[position]);
		return convertView;
	}

}
