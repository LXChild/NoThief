package com.demo.location;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * * GetLocation
 * 
 * @author LXChild
 */
public class LocationInfo implements LocationListener {

	private Context cxt;
	private LocationManager lm;
	private Location location;

	public LocationInfo(Context cxt) {
		// TODO Auto-generated constructor stub
		this.cxt = cxt;
	}

	public boolean getLocation() {
		initGPS();
		lm = (LocationManager) cxt.getSystemService(Context.LOCATION_SERVICE);
		// 获得当前的位置提供者
		String provider = getProvider(lm);
		// 注册一个周期性的位置更新,从GPS获取位置信息，并且是每隔1000ms更新一次
		lm.requestLocationUpdates(provider, 1000, 0, this);
		// 成功注册了一个周期性坐标更新以后，就随时可以通过下面的方法来取得当前的坐标了
		location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = lm
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location != null) {
			saveLocInfo();
			return true;
		}
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
			saveLocInfo();
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		// Provider被enable时触发此函数，比如GPS被打开
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		// Provider被disable时触发此函数，比如GPS被关闭
	}

	private String getProvider(LocationManager manager) {
		// 设置查询条件
		Criteria criteria = new Criteria();
		// 定位精准度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 对手机耗电性能要求（获取频率）
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		// 是否运行产生开销（费用）
		criteria.setCostAllowed(true);
		// 如果置为ture只会返回当前打开的gps设备
		// 如果置为false如果设备关闭也会返回
		return manager.getBestProvider(criteria, true);
	}

	/**
	 * initGPS
	 * */
	private void initGPS() {
		SharedPreferences sp = cxt.getSharedPreferences("GPSStatus",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		boolean GPSStatus = isOPen(cxt);
		editor.putBoolean("GPSStatus", GPSStatus);
		editor.commit();
		if (!GPSStatus) {
			openGPS(cxt);
		}
		Log.d("ss", GPSStatus + "");
	}

	/**
	 * SaveLocInfo
	 * */
	private void saveLocInfo() {
		String latitude = location.getLatitude() + ""; // 获取纬度
		String longitude = location.getLongitude() + ""; // 获取精度
		String altitude = location.getAltitude() + "";
		String accuracy = location.getAccuracy() + "";
		String bearing = location.getBearing() + "";// 获取方位
		String speed = location.getSpeed() + "";// 获取速度

		SharedPreferences sp = cxt.getSharedPreferences("LocInfo",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("latitude", latitude);
		editor.putString("longitude", longitude);
		editor.putString("altitude", altitude);
		editor.putString("accuracy", accuracy);
		editor.putString("bearing", bearing);
		editor.putString("speed", speed);
		editor.commit();
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	private boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps && network) {
			return true;
		}

		return false;
	}

	/**
	 * 强制帮用户打开GPS
	 * 
	 * @param context
	 */
	public void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}
}
