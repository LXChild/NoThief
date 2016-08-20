package com.demo.adapter;

import com.example.nothief.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OptionAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private String[] item;
	public OptionAdapter(Context cxt) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(cxt);
		item = new String[]{"�ҵĶ�λ", "��������", "����С��ʿ", "һ������", "˯��ģʽ", "�˳����"};
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
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.option_adapter_item, parent, false);
		}
		TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
		tv_item.setText(item[position]);
		
		return convertView;
	}

}
