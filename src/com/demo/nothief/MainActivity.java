package com.demo.nothief;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.adapter.OptionAdapter;
import com.demo.location.LocationInfo;
import com.example.nothief.R;

/**
 * main
 * 
 * @author LXChild
 * */
public class MainActivity extends Activity implements OnItemClickListener {

	private ListView lv_option;
	private OptionAdapter adapter;
	private String TAG = "MainActivity";
	private boolean isExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initView();
		initData();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	private void initView() {
		setContentView(R.layout.main);
		lv_option = (ListView) findViewById(R.id.lv_option);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
	}

	private void initData() {
		adapter = new OptionAdapter(this);
		lv_option.setAdapter(adapter);
		lv_option.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Item" + position + "has benn clicked!");
		switch (position) {
		case 0:
			Intent intent = new Intent();
			intent.setClass(this, LocationActivity.class);
			startActivity(intent);
			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		case 5:
			closeGPS();
			finish();
			System.exit(0);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
		}
		return false;
	}

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			closeGPS();
			finish();
			System.exit(0);
		}
	}

	private void closeGPS() {
		SharedPreferences sp = getSharedPreferences("GPSStatus",
				Context.MODE_PRIVATE);
		boolean GPSStatus = sp.getBoolean("GPSStatus", false);
		if (!GPSStatus) {
			LocationInfo locInfo = new LocationInfo(this);
			locInfo.openGPS(this);
		}
	}
}
