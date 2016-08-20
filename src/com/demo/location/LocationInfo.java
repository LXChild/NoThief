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
		// ��õ�ǰ��λ���ṩ��
		String provider = getProvider(lm);
		// ע��һ�������Ե�λ�ø���,��GPS��ȡλ����Ϣ��������ÿ��1000ms����һ��
		lm.requestLocationUpdates(provider, 1000, 0, this);
		// �ɹ�ע����һ����������������Ժ󣬾���ʱ����ͨ������ķ�����ȡ�õ�ǰ��������
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
		// Provider��ת̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		// Provider��enableʱ�����˺���������GPS����
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		// Provider��disableʱ�����˺���������GPS���ر�
	}

	private String getProvider(LocationManager manager) {
		// ���ò�ѯ����
		Criteria criteria = new Criteria();
		// ��λ��׼��
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// ���ֻ��ĵ�����Ҫ�󣨻�ȡƵ�ʣ�
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		// �Ƿ����в������������ã�
		criteria.setCostAllowed(true);
		// �����Ϊtureֻ�᷵�ص�ǰ�򿪵�gps�豸
		// �����Ϊfalse����豸�ر�Ҳ�᷵��
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
		String latitude = location.getLatitude() + ""; // ��ȡγ��
		String longitude = location.getLongitude() + ""; // ��ȡ����
		String altitude = location.getAltitude() + "";
		String accuracy = location.getAccuracy() + "";
		String bearing = location.getBearing() + "";// ��ȡ��λ
		String speed = location.getSpeed() + "";// ��ȡ�ٶ�

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
	 * �ж�GPS�Ƿ�����GPS����AGPS����һ������Ϊ�ǿ�����
	 * 
	 * @param context
	 * @return true ��ʾ����
	 */
	private boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// ͨ��GPS���Ƕ�λ����λ������Ծ�ȷ���֣�ͨ��24�����Ƕ�λ��������Ϳտ��ĵط���λ׼ȷ���ٶȿ죩
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// ͨ��WLAN���ƶ�����(3G/2G)ȷ����λ�ã�Ҳ����AGPS������GPS��λ����Ҫ���������ڻ��ڸ������Ⱥ��ï�ܵ����ֵȣ��ܼ��ĵط���λ��
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps && network) {
			return true;
		}

		return false;
	}

	/**
	 * ǿ�ư��û���GPS
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
