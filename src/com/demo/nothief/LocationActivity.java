package com.demo.nothief;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ListView;

import com.demo.adapter.LocationInfoAdapter;
import com.demo.location.LocationInfo;
import com.example.nothief.R;

public class LocationActivity extends Activity {

	private ListView lv_locinfo;
	private static LocationInfoAdapter adapter;
	private LocationInfo locInfo;
	private static ProgressDialog pd;
	private static boolean isLoced = false;
	private static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pd.dismiss();
			adapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		initData();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	private void initView() {
		setContentView(R.layout.location_info);
		lv_locinfo = (ListView) findViewById(R.id.lv_locinfo);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("位置");

		pd = ProgressDialog.show(this, "提示", "正在定位中...");
		pd.setCancelable(true);
	}

	private void initData() {
		adapter = new LocationInfoAdapter(this);
		lv_locinfo.setAdapter(adapter);
		lv_locinfo.setEnabled(false);

		getLocation();
	}

	private void getLocation() {
		locInfo = new LocationInfo(this);
		isLoced = locInfo.getLocation();
		new Thread() {
			public void run() {
				while (!isLoced) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finish();
		return super.onOptionsItemSelected(item);
	}
}
