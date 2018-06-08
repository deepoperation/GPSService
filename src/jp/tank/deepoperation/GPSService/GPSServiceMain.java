package jp.tank.deepoperation.GPSService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class GPSServiceMain extends Service implements LocationListener {
	private LocationManager		lm;			// ロケーションマネージャ
	Handler  handler = new Handler();


	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	// 開始時「onStart」メソッド
	@Override
	public int onStartCommand(Intent intent, int flag, int startID) {
		super.onStartCommand(intent, flag, startID);

		//開始時にトーストを表示
		Toast.makeText(this, "GPSサービスを開始しました！", Toast.LENGTH_SHORT).show();

		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);

		//ノティフィケーションマネージャの取得(4)
		NotificationManager nm;
		nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

		//ノティフィケーションオブジェクトの生成(5)
		Notification notification = new Notification(R.drawable.icon,
				"GPSサービスを開始しました",System.currentTimeMillis());

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, GPSMainActivity.class), 0);
		notification.setLatestEventInfo(this, "GPSサービス", "", pendingIntent);
		nm.notify(0,notification);

		Thread t = new Thread() {
			public void run() {
				try {

					ServerSocket		server;	//サーバソケット
					Socket				socket;	//ソケット
					GPSServerThread		thread;

					server = new ServerSocket(52001);
					while(true) {
						//接続待機
						socket = server.accept();

						//サーバスレッド開始
						thread = new GPSServerThread(socket);
						thread.start();

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		return START_REDELIVER_INTENT;
	}

	// 終了時「onDestroy」メソッド
	@Override
	public void onDestroy() {
		super.onDestroy();
		//ノティフィケーションマネージャの取得
		NotificationManager nm;
		nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

		//ノティフィケーションのキャンセル
		nm.cancel(0);

		Toast.makeText(this, "GPSサービスを終了しました！", Toast.LENGTH_SHORT).show();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onLocationChanged(Location location) {
		String latitude  = Double.toString(location.getLatitude());
		String longitude = Double.toString(location.getLongitude());
		StringBuffer buff = new StringBuffer();
		try {
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			for(Address addr : addresses) {
				int index = addr.getMaxAddressLineIndex();
				for(int i = 1; i <= index; i++) {
					buff.append(addr.getAddressLine(1));
				}
			}
		} catch (IOException e) {

		}
		GPSServerThread.sendMessageAll(latitude + "," + longitude + "," + buff + "\n");
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public void callActivity(final Intent intent) {
		handler.post(new Runnable() {
			public void run() {
				startActivity(intent);
			}
		});
	}
}
